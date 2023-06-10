package com.xiyo.pfund.core.pojo.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("投资品详情页返回数据")
@Data
public class InvestDetailVO {

    private Investment investment;

    private Account account;

    private Integer numerous;

    private Double amountMoeny;
}
