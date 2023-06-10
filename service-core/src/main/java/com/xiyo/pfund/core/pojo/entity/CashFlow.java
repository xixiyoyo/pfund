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
 * 资金流水表
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="CashFlow对象", description="资金流水表")
public class CashFlow implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "c_id", type = IdType.AUTO)
    private Long cId;

    private Long aId;

    private String cashNo;

    private Integer cashType;

    private String cashTypeName;

    private String cashName;

    private BigDecimal cashAmount;

    private BigDecimal nowAmount;

    private String memo;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDelete;


}
