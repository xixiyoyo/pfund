package com.xiyo.pfund.core.pojo.query;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("用户信息查询对象")
@Data
public class UserInfoQuery {

    private String mobile;

    private Integer status;

    private String name;

}
