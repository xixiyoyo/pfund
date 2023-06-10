package com.xiyo.pfund.core.service;

import com.xiyo.pfund.core.pojo.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 账户资金表 服务类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
public interface AccountService extends IService<Account> {

    //@ApiOperation("通过用户id找到对应的账户")
    public Account selectOneByUserId(Long userId);

    void Pay(Long userId, Double money);
}
