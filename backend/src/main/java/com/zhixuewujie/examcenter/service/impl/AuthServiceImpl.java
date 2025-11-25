package com.zhixuewujie.examcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhixuewujie.examcenter.dto.LoginDTO;
import com.zhixuewujie.examcenter.dto.RegisterDTO;
import com.zhixuewujie.examcenter.entity.User;
import com.zhixuewujie.examcenter.mapper.UserMapper;
import com.zhixuewujie.examcenter.service.IAuthService;
import com.zhixuewujie.examcenter.util.JwtUtil;
import com.zhixuewujie.examcenter.util.PasswordUtil;
import com.zhixuewujie.examcenter.vo.LoginResponseVO;
import com.zhixuewujie.examcenter.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordUtil passwordUtil;

    @Override
    public LoginResponseVO login(LoginDTO loginDTO) {
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 验证密码
        if (!passwordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查账号状态
        if (user.getStatus() != null && user.getStatus() == 1) {
            throw new RuntimeException("账号已被禁用");
        }

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 构建响应
        LoginResponseVO response = new LoginResponseVO();
        response.setToken(token);
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        response.setUser(userVO);

        return response;
    }

    @Override
    public UserVO register(RegisterDTO registerDTO) {
        // 验证密码
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new RuntimeException("两次密码输入不一致");
        }

        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        User existUser = userMapper.selectOne(wrapper);
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordUtil.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setStatus(0);

        userMapper.insert(user);

        // 返回用户信息
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

}

