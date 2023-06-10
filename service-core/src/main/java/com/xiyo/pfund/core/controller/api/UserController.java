package com.xiyo.pfund.core.controller.api;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xiyo.pfund.base.util.JwtUtils;
import com.xiyo.pfund.core.pojo.vo.*;
import com.xiyo.pfund.core.service.UserService;
import com.xiyo.common.exception.Assert;
import com.xiyo.common.result.ResponseEnum;
import com.xiyo.common.result.Result;
import com.xiyo.common.uitl.RegexValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户账号表 前端控制器
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@RestController
@RequestMapping("api/core/user")
//@CrossOrigin(methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.OPTIONS})
@Slf4j
@Api("用户管理")
public class UserController {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    @ApiOperation("注册用户")
    @PostMapping("register")
    public Result Register(@RequestBody RegisterVO info){
        //验证验证码
        Assert.notEmpty(info.getCode(),ResponseEnum.CODE_NULL_ERROR);
        Assert.notEmpty(info.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);
        Assert.notEmpty(info.getMobile(),ResponseEnum.MOBILE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(info.getMobile()),ResponseEnum.MOBILE_ERROR);
        String rcode = (String) redisTemplate.opsForValue().get("pfund:sms:code:"+ info.getMobile());
        Assert.equals(rcode,info.getCode(), ResponseEnum.CODE_ERROR);
        //生成用户信息
        userService.Register(info);
        return Result.ok().message("注册成功");
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    public Result Login(@RequestBody LoginFormVO loginForm){

        Assert.notNull(loginForm.getMobile(),ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notNull(loginForm.getPassword(),ResponseEnum.PASSWORD_NULL_ERROR);
        ResponseUserInfoVO userInfo =  userService.Login(loginForm);
        return Result.ok().data("userInfo",userInfo);
    }

    @ApiOperation("校验token")
    @GetMapping("checkToken")
    public Result CheckToken(HttpServletRequest request){
        String token = request.getHeader("token");
        boolean token1 = JwtUtils.checkToken(token);
        if(token1){
            return Result.ok();
        }else {
            return Result.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }

    @ApiOperation("检查手机号是否被注册")
    @GetMapping("checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile){
        return userService.checkMobile(mobile);
    }

    @ApiOperation("用户绑定")
    @PutMapping("bindUser")
    public Result UpdateUserInfo(@RequestBody BindUserVO userVO,HttpServletRequest request){
        Assert.isTrue(StringUtils.isNotBlank(userVO.getName()) && StringUtils.isNotBlank(userVO.getIdCard()),ResponseEnum.BIND_ERROE);
        //绑定用户信息
        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        userVO.setId(userId);
        userService.bindUserInfo(userVO);
        return Result.ok().message("绑定成功");
    }

    @ApiOperation("用户所有信息")
    @GetMapping("userMate")
    public Result GetAllInfoForUser(HttpServletRequest request){

        String token = request.getHeader("token");
        Long userId = JwtUtils.getUserId(token);
        UserInfoMateVO userInfoMate =  userService.getAllInfo(userId);
        return  Result.ok().data("userMate",userInfoMate);
    }

}

