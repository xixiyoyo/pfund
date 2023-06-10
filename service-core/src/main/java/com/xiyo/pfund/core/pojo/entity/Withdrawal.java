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
 * 提现申请表
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Withdrawal对象", description="提现申请表")
public class Withdrawal implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "w_id", type = IdType.AUTO)
    private Long wId;

    private Long aId;

    private BigDecimal applyAmount;

    private BigDecimal remainAmount;

    private Integer auditStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDelete;


}
