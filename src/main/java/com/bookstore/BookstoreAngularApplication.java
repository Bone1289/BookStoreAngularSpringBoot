package com.bookstore;

import com.bookstore.config.SecurityUtility;
import com.bookstore.domain.User;
import com.bookstore.domain.security.Role;
import com.bookstore.domain.security.UserRole;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BookstoreAngularApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreAngularApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        User user1 = new User();
        user1.setFirstName("Rata");
        user1.setLastName("Bogdan");
        user1.setUsername("brata");
        user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
        user1.setEmail("brata@ixiacom.com");
        Set<UserRole> userRoleSet = new HashSet<>();
        Role role1 = new Role();
        role1.setRoleId(1);
        role1.setName("ROLE_USER");
        userRoleSet.add(new UserRole(user1, role1));

        userService.createUser(user1, userRoleSet);
        userRoleSet.clear();


        User user2 = new User();
        user2.setFirstName("Rata1");
        user2.setLastName("Bogdan1");
        user2.setUsername("brata1");
        user2.setPassword(SecurityUtility.passwordEncoder().encode("pp"));
        user2.setEmail("brata1@ixiacom.com");
        Role role2 = new Role();
        role2.setRoleId(2);
        role2.setName("ROLE_ADMIN");
        userRoleSet.add(new UserRole(user2, role2));

        userService.createUser(user2, userRoleSet);
        userRoleSet.clear();
    }
}
