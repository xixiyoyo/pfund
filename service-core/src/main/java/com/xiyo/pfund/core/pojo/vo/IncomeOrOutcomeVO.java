package com.xiyo.pfund.core.pojo.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("收入和支出表单")
@Data
public class IncomeOrOutcomeVO {
    @ApiModelProperty("金额")
    private BigDecimal amount;
    @ApiModelProperty("备注")
    private String memo;

    @ApiModelProperty("金额来源：0是收入，1是支出")
    private Integer source;
}
