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
 * 投资类品表
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Investment对象", description="投资类品表")
public class Investment implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "i_id", type = IdType.AUTO)
    private Long iId;

    private String itemNo;

    private BigDecimal investAmount;

    private String investName;

    private String investGoal;

    private Double unitWorth;

    private Double dateRate;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDelete;


}
