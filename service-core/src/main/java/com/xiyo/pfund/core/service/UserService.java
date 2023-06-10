package com.xiyo.pfund.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiyo.pfund.core.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiyo.pfund.core.pojo.query.UserInfoQuery;
import com.xiyo.pfund.core.pojo.vo.*;

/**
 * <p>
 * 用户账号表 服务类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
public interface UserService extends IService<User> {

    void Register(RegisterVO info);

    ResponseUserInfoVO Login(LoginFormVO loginForm);

    IPage<User> GetUserInfo(Page<User> userPage, UserInfoQuery userInfoQuery);

    void lock(Long id, Integer status);

    boolean checkMobile(String mobile);

    void bindUserInfo(BindUserVO userVO);

    UserInfoMateVO getAllInfo(Long id);

}
