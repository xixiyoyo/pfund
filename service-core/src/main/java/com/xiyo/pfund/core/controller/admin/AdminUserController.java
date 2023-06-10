package com.xiyo.pfund.core.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiyo.common.exception.Assert;
import com.xiyo.common.result.ResponseEnum;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.pojo.entity.User;
import com.xiyo.pfund.core.pojo.query.UserInfoQuery;
import com.xiyo.pfund.core.service.AccountService;
import com.xiyo.pfund.core.service.UserService;
import com.xiyo.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户账号表 前端控制器
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@RestController
@RequestMapping("admin/core/user")
//@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.OPTIONS,RequestMethod.PUT})
@Slf4j
@Api("用户管理")
public class AdminUserController {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private AccountService accountService;

//    @ApiOperation("注册用户")
//    @PostMapping("register")
//    public Result Register(@RequestBody RegisterVO info){
//        //验证验证码
//        Assert.notEmpty(info.getCode(),ResponseEnum.CODE_NULL_ERROR);
//        Assert.notEmpty(info.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);
//        Assert.notEmpty(info.getMobile(),ResponseEnum.MOBILE_NULL_ERROR);
//        Assert.isTrue(RegexValidateUtils.checkCellphone(info.getMobile()),ResponseEnum.MOBILE_ERROR);
//        String rcode = (String) redisTemplate.opsForValue().get("pfund:sms:code:"+ info.getMobile());
//        Assert.equals(rcode,info.getCode(), ResponseEnum.CODE_ERROR);
//        //生成用户信息
//        userService.Register(info);
//        return Result.ok().message("注册成功");
//    }
//
//    @ApiOperation(value = "用户登录")
//    @GetMapping("login")
//    public Result Login(LoginFormVO loginForm){
//
//        Assert.notNull(loginForm.getMobile(),ResponseEnum.MOBILE_NULL_ERROR);
//        Assert.notNull(loginForm.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);
//        ResponseUserInfoVO userInfo =  userService.Login(loginForm);
//        return Result.ok().data("userInfo",userInfo);
//    }
//
//    @ApiOperation("校验token")
//    @GetMapping("checkToken")
//    public Result CheckToken(HttpServletRequest request){
//        String token = request.getHeader("token");
//        boolean token1 = JwtUtils.checkToken(token);
//        if(token1){
//            return Result.ok();
//        }else {
//            return Result.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
//        }
//    }

    @ApiOperation("分页用户信息")
    @GetMapping("userInfoPage/{pageIndex}/{pageSize}")
    public Result GetUserInfoPage(@PathVariable(required = true) Long pageIndex,
                                  @PathVariable(required = true) Long pageSize,
                                  UserInfoQuery userInfoQuery
    ){
        Page<User> userPage = new Page<>(pageIndex,pageSize);
        IPage<User> userPageInfo =  userService.GetUserInfo(userPage,userInfoQuery);
        return Result.ok().data("userPageInfo",userPageInfo);
    }

    @ApiOperation("用户锁定和解锁")
    @PutMapping("lock/{id}/{status}")
    public Result lock(@PathVariable Long id,
                       @PathVariable Integer status){
        userService.lock(id,status);
        return Result.ok().message(status == 0 ? "解锁成功" : "锁定成功" );
    }

    @GetMapping("getUserByAid/{aid}")
    @ApiOperation("通过账户找到用户信息")
    public Result getUserByAccount(@PathVariable Long aid){
        Account byId = accountService.getById(aid);
        Assert.notNull(byId, ResponseEnum.PARAMETER_NOT_GET);
        User user = userService.getById(byId.getId());
        return Result.ok().data("user",user);
    }

}

