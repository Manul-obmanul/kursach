package com.example.kursach.service.impl;

import com.example.kursach.entity.User;
import com.example.kursach.entity.UserAuthority;
import com.example.kursach.entity.UserRole;
import com.example.kursach.exception.UsernameAlreadyExistsException;
import com.example.kursach.repository.UserRepository;
import com.example.kursach.repository.UserRolesRepository;
import com.example.kursach.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    public final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    @Autowired
    public PasswordEncoder encoder;
    @Override
    public void registration(String username, String email,Long phone, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = User.builder()
                    .username(username)
                    .email(email)
                    .phone(phone)
                    .password(encoder.encode(password))
                    .locked(false)
                    .expired(false)
                    .enabled(true).build();
            userRepository.save(user);
            userRolesRepository.save(new UserRole(null, user, UserAuthority.PLACE_ORDERS));
        }
        else {
            throw new UsernameAlreadyExistsException();
        }
    }

    @Override
    public String deleteUser(String username, String loadName) {
        Optional<User> user = userRepository.findByUsername(loadName);
        Optional<UserRole> userRole = Optional.ofNullable(userRolesRepository.findByUserId(user.get().getId()));
        if(user.isPresent() && userRole.isPresent()) {
            if(userRole.get().getUserAuthority().equals(UserAuthority.FULL) || user.get().getUsername().equals(username)) {
                userRepository.deleteById(userRepository.findByUsername(username).get().getId());
                return "Пользователь успешно удалён";
            } else return "Убедитесь, что Вы  вводите верный username";
        } else return "Убедитесь, что Вы вошли в аккаунт";
    }
    @Override
    public String updateUser(Long id, String username, String email, Long phone, String password, String loadName) {
        Optional<User> user = userRepository.findByUsername(loadName);
        Optional<UserRole> userRole = Optional.ofNullable(userRolesRepository.findByUserId(user.get().getId()));
        if(user.get().getUsername().equals(username) || userRole.get().getUserAuthority().equals(UserAuthority.FULL)) {
            User updatedUser = User.builder()
                        .id(id)
                        .username(username)
                        .email(email)
                        .phone(phone)
                        .password(password)
                        .enabled(user.get().isEnabled())
                        .expired(false)
                        .locked(false)
                        .build();
                updatedUser.setUserRoles(user.get().getUserRoles());
                userRepository.save(updatedUser);
                return "Пользователь успешно изменён";
        } else return "Убедитесь, что Вы  вводите верный email или обладаете правами на изменение других пользователей";

    }
    @Override
    public ResponseEntity<?> getInfo(String username, String loadName){
        Optional<User> user = userRepository.findByUsername(loadName);
        Optional<UserRole> userRole = Optional.ofNullable(userRolesRepository.findByUserId(user.get().getId()));
        if(userRole.isPresent()) {
            if(user.get().getUsername().equals(username) || userRole.get().getUserAuthority().equals(UserAuthority.FULL)) {
                return ResponseEntity.ok(userRepository.findByUsername(username));
            } else return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Убедитесь, что Вы вводите верный username или обладаете правами на запрос информации о другом пользователе");
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Убедитесь, пользователь с таким username существует");
    }
}
