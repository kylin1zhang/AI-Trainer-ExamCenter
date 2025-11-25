package com.zhixuewujie.examcenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhixuewujie.examcenter.common.Result;
import com.zhixuewujie.examcenter.dto.LoginDTO;
import com.zhixuewujie.examcenter.dto.RegisterDTO;
import com.zhixuewujie.examcenter.service.IAuthService;
import com.zhixuewujie.examcenter.util.JwtUtil;
import com.zhixuewujie.examcenter.vo.LoginResponseVO;
import com.zhixuewujie.examcenter.vo.UserVO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 * 
 * 功能：
 * - 用户登录（账号密码）
 * - 用户注册
 * - 刷新 Token
 * - 退出登录
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public Result<LoginResponseVO> login(@RequestBody LoginDTO loginDTO) {
        try {
            LoginResponseVO response = authService.login(loginDTO);
            return Result.success(response);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户注册
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody RegisterDTO registerDTO) {
        try {
            UserVO user = authService.register(registerDTO);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     * GET /api/auth/user
     */
    @GetMapping("/user")
    public Result<UserVO> getCurrentUser(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                Long userId = jwtUtil.getUserIdFromToken(token);
                UserVO user = authService.getCurrentUser(userId);
                return Result.success(user);
            }
            return Result.error("未登录");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

}

