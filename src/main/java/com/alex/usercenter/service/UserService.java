package com.alex.usercenter.service;

import com.alex.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author alex
* @description 针对表【user】的数据库操作Service
* @createDate 2022-10-19 14:16:53
*/
public interface UserService extends IService<User> {
    /**
     * User register
     *
     * @param userAccount user account
     * @param userPassword user's password
     * @param checkPassword check password
     * @return the id of new user
     */
    int userRegister(String userAccount, String userPassword, String checkPassword);


    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User originUser);

    Integer userLogout(HttpServletRequest request);
}