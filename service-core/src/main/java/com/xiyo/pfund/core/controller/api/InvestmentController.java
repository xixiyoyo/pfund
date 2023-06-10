package com.xiyo.pfund.core.controller.api;


import com.xiyo.common.exception.Assert;
import com.xiyo.common.result.ResponseEnum;
import com.xiyo.common.result.Result;
import com.xiyo.pfund.base.util.JwtUtils;
import com.xiyo.pfund.core.pojo.entity.InvestDetailVO;
import com.xiyo.pfund.core.pojo.entity.Investment;
import com.xiyo.pfund.core.pojo.vo.BindInvestOV;
import com.xiyo.pfund.core.service.InvestmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 投资类品表 前端控制器
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@RestController
@RequestMapping("api/core/investment")
@Api("投资品管理")
@Slf4j
public class InvestmentController {

    @Resource
    private InvestmentService investmentService;

    @ApiOperation("创建投资品")
    @PostMapping("/create")
    public Result createInvestment(Investment investment){
        Assert.notNull(investment, ResponseEnum.PARAMETER_NOT_GET);
        boolean save = investmentService.save(investment);
        return Result.ok().message("创建成功");
    }

    @ApiOperation("获取所有的投资品信息")
    @GetMapping("/getAll")
    public Result getAllInvestInfo(){
        List<Investment> investMeta = investmentService.list();
        return Result.ok().data("investMeta",investMeta);
    }

    @ApiOperation("job调用：获取所有的投资品id")
    @GetMapping("/jobGetAllId")
    public List<Long> jobGetAllInvestInfo(){
        List<Long> investments = new LinkedList<>();
        List<Investment> list = investmentService.list();
        list.forEach(user ->{
            investments.add(user.getIId());
        });
        return investments;
    }

    @ApiOperation("job调用：每日更新投资品信息")
    @PostMapping("jobUpdate")
    public void jobUpdateInvestment(Long iid,Double rate){
        investmentService.jobUpdateInvestment(iid,rate);
    }

    @GetMapping("/getOne/{id}")
    @ApiOperation("返回投资产品信息")
    public Result getOneById(@PathVariable Long id, HttpServletRequest request){
        Investment investment = investmentService.getById(id);
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        InvestDetailVO investDetail = investmentService.getInvestDetail(investment,userId);
        return Result.ok().data("investInfo",investDetail);
    }

    @GetMapping("/getAdminOne/{id}")
    @ApiOperation("管理员返回投资产品信息")
    public Result getAdminOne(@PathVariable Long id){
        return Result.ok().data("investInfo",investmentService.getById(id));
    }

    @GetMapping("/getAccountAll")
    @ApiOperation("通过订单找到对应的投资品")
    public Result getAllInvestment(HttpServletRequest request){

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        List<Investment> list = investmentService.getAllInvestment(userId);
        return Result.ok().data("investmentList",list);
    }

}

