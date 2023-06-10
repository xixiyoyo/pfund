package com.xiyo.pfund.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiyo.pfund.base.util.JwtUtils;
import com.xiyo.pfund.core.mapper.AccountMapper;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.pojo.entity.User;
import com.xiyo.pfund.core.mapper.UserMapper;
import com.xiyo.pfund.core.pojo.query.UserInfoQuery;
import com.xiyo.pfund.core.pojo.vo.*;
import com.xiyo.pfund.core.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiyo.common.exception.Assert;
import com.xiyo.common.result.ResponseEnum;
import com.xiyo.common.uitl.MD5;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 用户账号表 服务实现类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public void Register(RegisterVO info) {
        //校验手机号是否被注册
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("mobile",info.getMobile());
        int count = baseMapper.selectCount(userQueryWrapper);
        Assert.isTrue(count == 0, ResponseEnum.MOBILE_EXIST_ERROR);



        //生成用户表
        User user = new User();
        user.setMobile(info.getMobile());
        user.setPassword(MD5.encrypt(info.getPassword()));
        baseMapper.insert(user);

    }

    @Override
    public ResponseUserInfoVO Login(LoginFormVO loginForm) {
        //检验密码、用户是否存在、是否停用、
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.eq("mobile",loginForm.getMobile());
        wrapper.eq("status",0);
        User user = baseMapper.selectOne(wrapper);
        Assert.notNull(user,ResponseEnum.MOBILE_ERROR);
        Assert.isTrue(user.getPassword().equals(MD5.encrypt(loginForm.getPassword())),ResponseEnum.LOGIN_PASSWORD_ERROR);

        //生成token
        String token = JwtUtils.createToken(user.getId(), user.getNickName());

        //返回用户信息
        ResponseUserInfoVO userInfoVO = new ResponseUserInfoVO();
        userInfoVO.setToken(token);
        userInfoVO.setMobile(user.getMobile());
        if(user.getNickName() != null){
            userInfoVO.setNickName(user.getNickName());
        }else {
            userInfoVO.setNickName(user.getMobile());
        }
        userInfoVO.setEmail(user.getEmail());
        userInfoVO.setHeadImg(user.getHeadImg());

        return userInfoVO;
    }

    @Override
    public IPage<User> GetUserInfo(Page<User> userPage, UserInfoQuery userInfoQuery) {
        if(userInfoQuery == null){
            return baseMapper.selectPage(userPage,null);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(userInfoQuery.getMobile()),"mobile",userInfoQuery.getMobile())
            .eq(userInfoQuery.getStatus() != null,"status",userInfoQuery.getStatus())
            .like(StringUtils.isNotBlank(userInfoQuery.getName()),"nick_name",userInfoQuery.getName());
        Page<User> selectPage = baseMapper.selectPage(userPage, wrapper);

        return selectPage;
    }

    @Override
    public void lock(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        baseMapper.updateById(user);

    }

    @Override
    public boolean checkMobile(String mobile) {

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(userQueryWrapper);
        return count > 0;
    }

    @Override
    public void bindUserInfo(BindUserVO userVO) {
        //校验当前身份证合法同时是否在数据库中存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id_card",userVO.getIdCard());
        Integer count = baseMapper.selectCount(userQueryWrapper);
        Assert.isTrue(count == 0,ResponseEnum.USER_BIND_IDCARD_EXIST_ERROR);

        //根据传入参数进行信息完善（同时将用户设为绑定状态）
        User user = new User();
        user.setId(userVO.getId());
        user.setName(userVO.getName());
        user.setIdCard(userVO.getIdCard());
        if(StringUtils.isNotBlank(userVO.getEmail())){
            user.setEmail(userVO.getEmail());
        }
        if(StringUtils.isNotBlank(userVO.getNickName())){
            user.setNickName(userVO.getNickName());
        }
        user.setBindStatus(1);
        baseMapper.updateById(user);
        //为用户创建一个账户
        Account account = new Account();
        account.setId(user.getId());
        account.setAmount(new BigDecimal(0));
        account.setFreezeAmount(new BigDecimal(0));
        account.setProfit(new BigDecimal(0));
        accountMapper.insert(account);
    }

    @Override
    public UserInfoMateVO getAllInfo(Long id) {
        UserInfoMateVO  userInfoMate = new UserInfoMateVO();
        User user = baseMapper.selectById(id);
        //Assert.isTrue(user.getBindStatus() == 1,ResponseEnum.USER_NO_BIND_ERROR);
        if(user.getBindStatus() != 1){
            userInfoMate.setUser(user);
            return userInfoMate;
        }
        userInfoMate.setUser(user);
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("id",user.getId());
        Account account = accountMapper.selectOne(wrapper);
        //填充数据
        userInfoMate.setAccount(account);
        return userInfoMate;
    }
}
