package com.site.andrewsfood.Model.service;

import com.site.andrewsfood.Model.domain.enums.Contradictions;
import com.site.andrewsfood.Model.domain.CustomUserDetails;
import com.site.andrewsfood.Model.domain.enums.Role;
import com.site.andrewsfood.Model.domain.User;
import com.site.andrewsfood.Dao.CustomUserDetailsRepo;
import com.site.andrewsfood.Dao.UserRepo;
import com.site.andrewsfood.Service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CustomUserDetailsRepo customUserDetailsRepo;

    @Test
    void findAllUsers() {
        userService.deleteAll();
        Set<Contradictions> contradictions = new HashSet();
        CustomUserDetails customUserDetails = new CustomUserDetails("some@gmail.com", "someactive",
                "Чоловіча", 20, 180, 80,
                "звичайний", "сидячий", "ектоморф",
                "спортивний", "нема", 2000, 1, 1,
                4, 10, contradictions);
        CustomUserDetails customUserDetails2 = new CustomUserDetails("some2@gmail.com", "someactive2",
                "Чоловіча", 20, 180, 80,
                "звичайний", "сидячий", "ектоморф",
                "спортивний", "нема", 2000, 1, 1,
                4, 10, contradictions);
        User user = new User("test", "112233", false, customUserDetails, Collections.singleton(Role.USER));
        User user2 = new User("test2", "112233", false, customUserDetails2, Collections.singleton(Role.USER));
        userService.saveUser(user);
        userService.saveUser(user2);
        List<User> users = userService.findAllUsers();
        boolean match = (user.getUsername().equals(users.get(0).getUsername())) &&
                (user2.getUsername().equals(users.get(1).getUsername()));
        Assert.assertTrue(match);
    }

    @Test
    void allUsers() {
        userService.deleteAll();
        Set<Contradictions> contradictions = new HashSet();
        CustomUserDetails customUserDetails = new CustomUserDetails("some@gmail.com", "someactive",
                "Чоловіча", 20, 180, 80,
                "звичайний", "сидячий", "ектоморф",
                "спортивний", "нема", 2000, 1, 1,
                4, 10, contradictions);
        CustomUserDetails customUserDetails2 = new CustomUserDetails("some2@gmail.com", "someactive2",
                "Чоловіча", 20, 180, 80,
                "звичайний", "сидячий", "ектоморф",
                "спортивний", "нема", 2000, 1, 1,
                4, 10, contradictions);
        User user = new User("test", "112233", false, customUserDetails, Collections.singleton(Role.USER));
        User user2 = new User("test2", "112233", false, customUserDetails2, Collections.singleton(Role.USER));
        userService.saveUser(user);
        userService.saveUser(user2);

        int count = userService.AllUsers();
        Assert.assertEquals(2, count);
    }

    @Test
    void deleteUserByName() {
        userService.deleteAll();
        Set<Contradictions> contradictions = new HashSet();
        CustomUserDetails customUserDetails = new CustomUserDetails("some@gmail.com", "someactive",
                "Чоловіча", 20, 180, 80,
                "звичайний", "сидячий", "ектоморф",
                "спортивний", "нема", 2000, 1, 1,
                4, 10, contradictions);
        User user = new User("test", "112233", false, customUserDetails, Collections.singleton(Role.USER));
        userService.saveUser(user);

        int count = userService.AllUsers();
        userService.deleteUserByName("test");
        int countAfter = userService.AllUsers();
        Assert.assertEquals(count - 1, countAfter);
    }

    @Test
    void activateUser() {
        userService.deleteAll();
        Set<Contradictions> contradictions = new HashSet();
        CustomUserDetails customUserDetails = new CustomUserDetails("some@gmail.com", "someactive",
                "Чоловіча", 20, 180, 80,
                "звичайний", "сидячий", "ектоморф",
                "спортивний", "нема", 2000, 1, 1,
                4, 10, contradictions);
        User user = new User("test", "112233", false, customUserDetails, Collections.singleton(Role.USER));
        userService.saveUser(user);

        boolean isUserActivated = userService.activateUser("someactive");
        Assert.assertTrue(isUserActivated);
    }
}