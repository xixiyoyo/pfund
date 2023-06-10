package com.xiyo.pfund.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("用户绑定账户信息")
@Data
public class BindUserVO {

    private Long id;

    private String mobile;

    private String password;

    private String nickName;

    private String name;

    private String idCard;

    private String email;

    private String headImg;
}
