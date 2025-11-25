package com.zhixuewujie.examcenter.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户视图对象
 *
 * @author 智学无界
 * @since 2025-11-24
 */
@Data
public class UserVO {

    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private LocalDateTime createTime;

}

