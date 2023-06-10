package com.xiyo.pfund.core.controller.api;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiyo.common.result.Result;
import com.xiyo.pfund.base.util.JwtUtils;
import com.xiyo.pfund.core.pojo.entity.Withdrawal;
import com.xiyo.pfund.core.service.WithdrawalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 提现申请表 前端控制器
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@RestController
@RequestMapping("api/core/withdrawal")
@Api("提现申请")
public class WithdrawalController {

    @Resource
    private WithdrawalService withdrawalService;


    @PostMapping("create")
    @ApiOperation("创建一条提现记录")
    public Result Create(Double money, HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        withdrawalService.Create(money,userId);
        return Result.ok().message("提交成功");
    }

    @PostMapping("checkOrder")
    @ApiOperation("审核提现单")
    public Result Audit(Long wid,Boolean pass){
        Boolean aufit = withdrawalService.Aufit(wid, pass);
        if(aufit){
            return Result.ok().message("审核完成");
        }else {
            return Result.error().message("审核失败");
        }

    }

    @GetMapping("canelOrder")
    @ApiOperation("取消提现单")
    public Result canel(Long wid){
        Boolean canel = withdrawalService.Canel(wid);
        if(canel){
            return Result.ok().message("取消成功");
        }else {
            return Result.error().message("取消失败");
        }
    }

    @GetMapping("getAdminAll/{pageIndex}/{pageSize}")
    @ApiOperation("管理员的审核数据")
    public Result GetAllAdmin(@PathVariable(required = true) Long pageIndex,
                              @PathVariable(required = true) Long pageSize){
        Page<Withdrawal> withdrawalPage = new Page<>(pageIndex,pageSize);
        IPage<Withdrawal> withdrawalInfo = withdrawalService.getWithDrawPage(withdrawalPage);
        return Result.ok().data("withDrawMate",withdrawalInfo);
    }

    @GetMapping("getAll")
    @ApiOperation("用户查看的提现记录")
    public Result GetAll(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        List<Withdrawal> list = withdrawalService.getAll(userId);
        return Result.ok().data("withDrawList",list);
    }
}

