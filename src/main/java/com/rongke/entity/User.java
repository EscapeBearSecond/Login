package com.rongke.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@ApiModel("用户实体")
public class User {
    @ApiModelProperty("用户账号")
    private String username;
    @ApiModelProperty("用户密码")
    private String password;
    @ApiModelProperty("账号状态，1可用，0不可用")
    private int statue;
    @ApiModelProperty("尝试次数")
    private int attempt;
    @ApiModelProperty("解锁时间")
    private long time;
}
