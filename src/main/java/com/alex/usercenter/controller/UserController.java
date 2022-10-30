package com.alex.usercenter.controller;

import com.alex.usercenter.exception.BusinessException;
import com.alex.usercenter.common.BaseResponse;
import com.alex.usercenter.common.ErrorCode;
import com.alex.usercenter.common.ResultUtils;
import com.alex.usercenter.constant.UserConstant;
import com.alex.usercenter.model.domain.User;
import com.alex.usercenter.model.domain.request.UserLoginRequest;
import com.alex.usercenter.model.domain.request.UserRegisterRequest;
import com.alex.usercenter.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Integer> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // Request not null
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAM_ERROR);
            throw new BusinessException(ErrorCode.PARAM_ERROR, "请求为空");
        }

        // All parameters can't be null
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "参数不能为空");
        }

        int result = userService.userRegister(userAccount, userPassword, checkPassword);

        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // Not allow null
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }

        // User parameter can't be null
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        // Only allow user access
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "用户不是管理员");
        }
        // Query specified user
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        // Return masked user
        List<User> safetyUserList = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());

        return ResultUtils.success(safetyUserList);
    }

    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody int id, HttpServletRequest request) {
        // Only allow user access
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "用户不是管理员");
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.NO_AUTH, "用户不是管理员");
        }
        boolean result = userService.removeById(id);

        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObject;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.OUT_LOGIN);
        }

        Integer userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);

        return ResultUtils.success(safetyUser);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "用户不是管理员");
        }

        Integer integer = userService.userLogout(request);
        return ResultUtils.success(integer);
    }

    /**
     * Judge whether be admin
     *
     * @param request http request
     * @return the result of judgement
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObject;
        return user == null || user.getUserRole() != UserConstant.ADMIN_ROLE;
    }
}
