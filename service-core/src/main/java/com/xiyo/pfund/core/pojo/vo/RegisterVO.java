package com.xiyo.pfund.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "注册信息")
public class RegisterVO {

    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("验证码")
    private String code;
    @ApiModelProperty("密码")
    private String password;


}
