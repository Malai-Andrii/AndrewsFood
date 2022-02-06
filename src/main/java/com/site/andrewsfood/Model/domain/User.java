package com.site.andrewsfood.Model.domain;

import com.site.andrewsfood.Model.domain.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long user_id;

    @NotBlank(message = "Введіть ім'я користувача!")
    private String username;
    @Length(min=6, message = "Надійний пароль мусить мати 6 або більше символів")
    private String password;

    private boolean active;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CustomUserDetails userDetails;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "usr_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(@NotBlank(message = "Введіть ім'я користувача!") String username,
                @Length(min = 6, message = "Надійний пароль мусить мати 6 або більше символів") String password,
                boolean active,
                CustomUserDetails userDetails,
                Set<Role> role) {
        this.username = username;
        this.password = password;
        this.active = active;
        this.userDetails = userDetails;
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return user_id;
    }

    public void setId(Long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomUserDetails getCustomUserDetails() {
        return userDetails;
    }

    public void setCustomUserDetails(CustomUserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
