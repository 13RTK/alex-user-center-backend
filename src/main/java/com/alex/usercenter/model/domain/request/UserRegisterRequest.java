package com.alex.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 5809915077832012533L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;

}
