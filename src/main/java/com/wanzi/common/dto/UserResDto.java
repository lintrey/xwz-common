package com.wanzi.common.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-08-04
 **/
@Data
public class UserResDto implements Serializable {
    private static final long serialVersionUID = -8532893337746271845L;

    private Integer userId;

    @NotBlank(message = "用户名不能为空")
    private String userName;

    private Integer age;

    private Float kk;

}
