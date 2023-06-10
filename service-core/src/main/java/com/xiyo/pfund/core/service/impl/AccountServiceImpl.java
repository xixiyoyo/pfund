package com.xiyo.pfund.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiyo.common.exception.Assert;
import com.xiyo.common.result.ResponseEnum;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.mapper.AccountMapper;
import com.xiyo.pfund.core.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiyo.pfund.core.service.CashFlowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 账户资金表 服务实现类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    private CashFlowService cashFlowService;

    @Override
    public Account selectOneByUserId(Long userId) {
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("id",userId);
        Account account = baseMapper.selectOne(wrapper);
        if(account == null){
            return null;
        }
        return account;
    }

    @Override
    public void Pay(Long userId, Double money) {
        //更改余额
        Account account = selectOneByUserId(userId);
        Assert.notNull(account, ResponseEnum.BIND_ERROE);
        BigDecimal amount = account.getAmount();
        BigDecimal bit = new BigDecimal(money);
        amount = amount.add(bit);
        account.setAmount(amount);
        baseMapper.updateById(account);
        //创建流水
        cashFlowService.inCome(account.getAId(),"充值",bit,account.getAmount());
    }
}
