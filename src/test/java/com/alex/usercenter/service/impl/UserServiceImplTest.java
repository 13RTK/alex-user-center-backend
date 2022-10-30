package com.alex.usercenter.service.impl;

import com.alex.usercenter.model.domain.User;
import com.alex.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Resource
    UserService userService;

    @Test
    void addUserTest() {
        User user = new User();
        user.setUsername("HDAlex");
        user.setUserAccount("alex");
        user.setUserPassword("123456");
        user.setAvatarUrl("https://www.allkpop.com/upload/2022/08/content/041734/1659648842-image.png");
        user.setEmail("31242@qq.com");
        user.setPhone("123412412");
        user.setGender(0);
        boolean result = userService.save(user);

        System.out.println(user.getId());
        assertTrue(result);
    }

    @Test
    void userRegisterTest() {
        // User password is null
        String userAccount = "john";
        String userPassword = "";
        String checkPassword = "123456";
        int result = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println("password is null");
        assertEquals(-1, result);

        // Length restrict
        userAccount = "leo";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println("user account is too short");
        assertEquals(-1, result);

        // Password must be equals check password
        userAccount = "john";
        userPassword = "654421";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println("user password not equals check password");
        assertEquals(-1, result);

        // Not allow special character in account name
        userAccount = "alex john";
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println("user account contains special character");
        assertEquals(-1, result);

        // Not allow repeat user account
        userAccount = "alex";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println("user account is exists in the database");
        assertEquals(-1, result);
    }


    @Test
    void userLoginTest() {
        long count = userService.count();
        System.out.println("count = " + count);
    }

//    @Test
//    void addNewUser() {
//        int newUserId = userService.userRegister("alex-test", "12345678", "12345678");
//        assertTrue(newUserId > 0);
//    }
}