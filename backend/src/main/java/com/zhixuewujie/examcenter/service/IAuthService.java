package com.zhixuewujie.examcenter.service;

import com.zhixuewujie.examcenter.dto.LoginDTO;
import com.zhixuewujie.examcenter.dto.RegisterDTO;
import com.zhixuewujie.examcenter.vo.LoginResponseVO;
import com.zhixuewujie.examcenter.vo.UserVO;

/**
 * 认证服务接口
 *
 * @author 智学无界
 * @since 2025-11-24
 */
public interface IAuthService {

    /**
     * 用户登录
     */
    LoginResponseVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     */
    UserVO register(RegisterDTO registerDTO);

    /**
     * 获取当前用户信息
     */
    UserVO getCurrentUser(Long userId);

}

