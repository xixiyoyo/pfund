package com.xiyo.pfund.core.controller;


import com.xiyo.common.exception.Assert;
import com.xiyo.common.result.ResponseEnum;
import com.xiyo.common.result.Result;
import com.xiyo.pfund.base.util.JwtUtils;
import com.xiyo.pfund.core.pojo.entity.CashFlow;
import com.xiyo.pfund.core.pojo.vo.IncomeOrOutcomeVO;
import com.xiyo.pfund.core.service.CashFlowService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 资金流水表 前端控制器
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@RestController
@RequestMapping("api/core/cashFlow")
public class CashFlowController {

    @Resource
    private CashFlowService cashFlowService;

    @GetMapping("getall")
    @ApiOperation("用户获取自己的流水信息")
    public Result getAll(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        List<CashFlow> list = cashFlowService.getAll(userId);
        return Result.ok().data("cashList",list);
    }

    @PostMapping("inOrOut")
    @ApiOperation("当日的收入或支出")
    public Result inOrOutcome(IncomeOrOutcomeVO record,HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Assert.notNull(record, ResponseEnum.PARAMETER_NOT_GET);
        if(record.getSource() == 1){
            cashFlowService.earn(userId,record);
            return Result.ok().message("充值成功");
        }else if(record.getSource() == 2){
            cashFlowService.lostMoney(userId,record);
            return Result.ok().message("取钱成功");
        }else {
            return Result.error().message("操作失败");
        }
    }

}

