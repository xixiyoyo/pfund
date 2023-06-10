package com.xiyo.pfund.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("投资者信息")
public class InvestorVO {

    @ApiModelProperty("投资者姓名")
    private String investName;
    @ApiModelProperty("投资者金额")
    private BigDecimal investAmount;
    @ApiModelProperty("投资时间")
    private LocalDateTime investTime;
}
