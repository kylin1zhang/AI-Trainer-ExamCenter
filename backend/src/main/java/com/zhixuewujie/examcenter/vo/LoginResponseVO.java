package com.zhixuewujie.examcenter.vo;

import lombok.Data;

/**
 * 登录响应VO
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
public class LoginResponseVO {

    private String token;
    private UserVO user;

}

