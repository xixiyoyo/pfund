package com.xiyo.pfund.core.controller.api;


import com.xiyo.common.exception.Assert;
import com.xiyo.common.result.ResponseEnum;
import com.xiyo.common.result.Result;
import com.xiyo.pfund.base.util.JwtUtils;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.pojo.entity.InvestOrder;
import com.xiyo.pfund.core.pojo.vo.InvestorVO;
import com.xiyo.pfund.core.service.AccountService;
import com.xiyo.pfund.core.service.InvestOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 投资订单表 前端控制器
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@RestController
@RequestMapping("api/core/investOrder")
@Api("投资订单管理")
public class InvestOrderController {

    @Resource
    private InvestOrderService investOrderService;

    @Resource
    private AccountService accountService;


//    @PostMapping("create")
//    @ApiOperation("创建一条投资订单")
//    public Result CreateOrder(InvestOrder investOrder){
//        boolean save = investOrderService.save(investOrder);
//        if(save){
//            return Result.ok().message("创建成功");
//        }else {
//            return Result.error().message("创建失败");
//        }
//    }

    @GetMapping("getNumerous/{id}")
    @ApiOperation("获取每个投资品下的所有投资人")
    public Result getNumerousByIId(@PathVariable Long id){
        Assert.notNull(id, ResponseEnum.PARAMETER_NOT_GET);
        List<InvestorVO> investors = investOrderService.getNumerousByIId(id);
        return Result.ok().data("numerous",investors);
    }

    @GetMapping("getMyOrder/{id}")
    @ApiOperation("拿到当前用户的投资品订单")
    public Result getMyOrder(@PathVariable Long id,HttpServletRequest request){
        Assert.notNull(id, ResponseEnum.PARAMETER_NOT_GET);
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Account account = accountService.selectOneByUserId(userId);
        Assert.notNull(account,ResponseEnum.USER_NO_BIND_ERROR);
        InvestOrder investOrder = investOrderService.getMyOrder(id,account);
        return Result.ok().data("investOrder",investOrder);
    }

    @PostMapping("purchase")
    @ApiOperation("用户购买投资品")
    public Result Purchase(@RequestParam("money") Double money,
                           @RequestParam("iid") Long iid, HttpServletRequest request){
        //获取用户信息
        Assert.notNull(request.getHeader("token"),ResponseEnum.LOGIN_AUTH_ERROR);
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Account account = accountService.selectOneByUserId(userId);
        Assert.notNull(account,ResponseEnum.USER_NO_BIND_ERROR);
        //买逻辑
        Boolean istrue = investOrderService.Purchase(iid,money,account);
        if(istrue){
            return Result.ok().message("购买成功");
        }else {
            return Result.error().message("购买失败");
        }

    }

    @PostMapping("sell")
    @ApiOperation("卖出投资品")
    public Result Sell(Double money, Long iid, HttpServletRequest request){
        //获取用户信息
        Assert.notNull(request.getHeader("token"),ResponseEnum.LOGIN_AUTH_ERROR);
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Account account = accountService.selectOneByUserId(userId);
        Assert.notNull(account,ResponseEnum.USER_NO_BIND_ERROR);
        Boolean isOk = investOrderService.sellInvestment(iid, money, account);
        if(isOk){
            return Result.ok().message("卖出成功");
        }else {
            return Result.error().message("卖出失败");
        }
    }

    @GetMapping("getall")
    @ApiOperation("用户得到自己所有的投资订单")
    public Result getAll(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Account account = accountService.selectOneByUserId(userId);
        List<InvestOrder> list = investOrderService.getAll(account);
        return Result.ok().data("orderList",list);
    }

    @PostMapping("updateEarnings")
    @ApiOperation("job更新我们的订单收益和账户收益")
    public void updateEarn(Long iid,Double rate){
        investOrderService.updateEarn(iid,rate);
    }

}

