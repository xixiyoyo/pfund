package com.xiyo.pfund.core.controller.api;


import com.xiyo.common.result.Result;
import com.xiyo.pfund.base.util.JwtUtils;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 账户资金表 前端控制器
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@RestController
@RequestMapping("api/core/account")
@Api("获取庄户数据")
public class AccountController {

    @Resource
    private AccountService accountService;

    @GetMapping("getOne")
    @ApiOperation("得到用户账户信息")
    public Result GetAccount(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        Account account = accountService.selectOneByUserId(userId);
        return Result.ok().data("account",account);
    }

    @PostMapping("recharge")
    @ApiOperation("充值")
    public Result Recharge(Double money,HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        accountService.Pay(userId,money);
        return Result.ok().message("充值成功");
    }

}

