package com.xiyo.pfund.core.pojo.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("返回的用户信息")
@Data
public class ResponseUserInfoVO {


    private String nickName;

    private String mobile;

    private String email;

    private String headImg;

    private String token;

}
