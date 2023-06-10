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
 * 账户资金表
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Account对象", description="账户资金表")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "a_id", type = IdType.AUTO)
    private Long aId;

    private Long id;

    private BigDecimal amount;

    private BigDecimal freezeAmount;

    private BigDecimal profit;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDelete;

    private Integer version;


}
