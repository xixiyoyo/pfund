package com.xiyo.pfund.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 新闻表
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="News对象", description="新闻表")
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "n_id", type = IdType.AUTO)
    private Long nId;

    private String newNo;

    private String newTitle;

    private String newContext;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDelete;


}
