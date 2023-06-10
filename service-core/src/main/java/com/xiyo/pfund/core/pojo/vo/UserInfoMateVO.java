package com.xiyo.pfund.core.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.pojo.entity.User;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("用户和账户显示信息")
@Data
public class UserInfoMateVO {

    private User user;

    private Account account;
}
