package com.alex.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * Pack the user account and user password as a class
 *
 * @author alex
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1943287615957113260L;

    private String userAccount;
    private String userPassword;
}
