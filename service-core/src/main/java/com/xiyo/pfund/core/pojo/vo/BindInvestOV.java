package com.xiyo.pfund.core.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@ApiModel("投资品创建表单")
//@TableName(autoResultMap = true)
public class BindInvestOV {

    private BigDecimal investAmount;

    private String investName;

    private String investGoal;
}
