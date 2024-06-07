package com.example.kursach.service;

import com.example.kursach.entity.User;
import com.example.kursach.entity.UserAuthority;
import com.example.kursach.entity.UserRole;
import com.example.kursach.repository.UserRepository;
import com.example.kursach.repository.UserRolesRepository;
import com.example.kursach.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.io.TempDirFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    UserDetailsService userDetailsService;
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    UserRolesRepository userRolesRepository;
    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId(15L);
        user.setUsername("testUser");
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setUserAuthority(UserAuthority.FULL);

        lenient().when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        lenient().when(userRolesRepository.findByUserId(15L)).thenReturn(userRole);

        String result = userService.deleteUser("user",  "admin");

        assertEquals("Пользователь успешно удалён", result);
        verify(userRepository, times(1)).deleteById(1L);
    }
    @Test
    public void testGetInfo() {
        UserRepository userRepository = mock(UserRepository.class);
        UserRolesRepository userRolesRepository = mock(UserRolesRepository.class);

        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);
        lenient().when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.getPrincipal()).thenReturn(userDetails);

        User user = new User();
        user.setUsername("testUser");
        lenient().when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserRole userRole = new UserRole();
        userRole.setUserAuthority(UserAuthority.FULL);
        userRole.setUser(user);
        lenient().when(userRolesRepository.findByUserId(user.getId())).thenReturn(userRole);

        ResponseEntity<?> response = userService.getInfo("testUser", "testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@example.com")
                .phone(1234567890L)
                .password("password")
                .enabled(true)
                .expired(false)
                .locked(false)
                .build();
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        String result = userService.updateUser(1L, "newadmin", "newadmin@example.com", 9876543210L, "newpassword", "admin");

        assertEquals("Пользователь успешно изменён", result);
    }
    @Test
    public void testUpdateUserFailure() {

        User user = User.builder()
                .id(2L)
                .username("user")
                .email("user@example.com")
                .phone(9876543210L)
                .password("password")
                .enabled(true)
                .expired(false)
                .locked(false)
                .build();
        lenient().when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));

        String result = userService.updateUser(2L, "newuser", "newuser@example.com", 1234567890L, "newpassword", "user1");

        assertEquals("Убедитесь, что Вы вводите верный username или обладаете правами на изменение других пользователей", result);
    }
}
