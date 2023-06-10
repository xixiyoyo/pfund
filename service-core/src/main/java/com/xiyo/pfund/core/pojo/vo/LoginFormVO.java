package com.xiyo.pfund.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户登录表单")
public class LoginFormVO {

    @ApiModelProperty("电话")
    private String mobile;

    @ApiModelProperty("密码")
    private String password;
}
