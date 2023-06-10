package com.xiyo.pfund.core.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 投资订单表
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="InvestOrder对象", description="投资订单表")
public class InvestOrder implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "io_id", type = IdType.AUTO)
    private Long ioId;

    private Long iId;

    private Long aId;
    @ApiModelProperty("累计收益")
    private BigDecimal aip;
    @ApiModelProperty("昨日收益")
    private BigDecimal dailyYield;
    @ApiModelProperty("投资金额")
    private BigDecimal investPay;

    private Double investRate;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;


}
