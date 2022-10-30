package com.alex.usercenter.service.impl;

import com.alex.usercenter.exception.BusinessException;
import com.alex.usercenter.common.ErrorCode;
import com.alex.usercenter.constant.UserConstant;
import com.alex.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alex.usercenter.model.domain.User;
import com.alex.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author alex
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-10-19 14:16:53
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Resource
    private UserMapper usermapper;

    private static final String SALT = "alex";

    @Override
    public int userRegister(String userAccount, String userPassword, String checkPassword) {
        // Not null
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            // TODO modify to custom exception
//            return -1;
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "参数为空");
        }
        // length restrict
        if (userAccount.length() < 4 || userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "帐户或密码过短");
        }
        // The user account shouldn't contain special character
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "帐户含特殊字符");
        }
        // Check user password and the check password
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        // Not allow repeat account
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.count(queryWrapper);
        if (count > 0) {
//            return -1;
            throw new BusinessException(ErrorCode.PARAM_ERROR, "该帐户已存在");
        }
        // Encode password
        String encodedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // Insert data
        User newUser = new User();
        newUser.setUserAccount(userAccount);
        newUser.setUserPassword(encodedPassword);
        boolean saveResult = this.save(newUser);

        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return newUser.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // Not null
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "输入不能为空");

        }
        // length restrict
        if (userAccount.length() < 4 || userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "帐户或密码长度不足");
        }
        // The user account shouldn't contain special character
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "帐户中存在特殊字符");

        }
        // Encode password
        String encodedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // Check the user is exists
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encodedPassword);
        User queryUser = usermapper.selectOne(queryWrapper);
        if (queryUser == null) {
            log.info("User login failed, user account or password not match.");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "帐户不存在");
        }
        //  Data masking
        User safetyUser = getSafetyUser(queryUser);
        // Record the user status by session
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUserRole(originUser.getUserRole());

        return safetyUser;
    }

    @Override
    public Integer userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }
}




