package com.example.kursach.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String username;
    private String email;
    private Long phone;
    private String password;
    private boolean expired;
    private boolean locked;
    private boolean enabled;
    @OneToMany(mappedBy = "user", orphanRemoval =true, fetch = FetchType.EAGER)
    private List<UserRole> userRoles;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return userRoles.stream().map(UserRole::getUserAuthority).collect(Collectors.toList());
    }
    @Override
    public String getPassword(){return password;}
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired(){return !expired;}
    @Override
    public boolean isAccountNonLocked(){return !locked;}
    @Override
    public boolean isCredentialsNonExpired(){return !expired;}
    @Override
    public boolean isEnabled(){return enabled;}


}