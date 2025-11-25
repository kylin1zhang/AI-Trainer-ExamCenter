package com.zhixuewujie.examcenter.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 注册请求DTO
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    private String nickname;

    private String email;

}

