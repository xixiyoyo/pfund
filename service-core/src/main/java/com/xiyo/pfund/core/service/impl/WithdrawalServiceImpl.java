package com.xiyo.pfund.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiyo.common.exception.Assert;
import com.xiyo.common.result.ResponseEnum;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.pojo.entity.Withdrawal;
import com.xiyo.pfund.core.mapper.WithdrawalMapper;
import com.xiyo.pfund.core.service.AccountService;
import com.xiyo.pfund.core.service.CashFlowService;
import com.xiyo.pfund.core.service.WithdrawalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 提现申请表 服务实现类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Service
public class WithdrawalServiceImpl extends ServiceImpl<WithdrawalMapper, Withdrawal> implements WithdrawalService {

    @Resource
    private AccountService service;

    @Resource
    private CashFlowService cashFlowService;

    @Override
    @Transactional
    public void Create(Double money, Long userId) {
        //通过用户id找到账户
        Account account = service.selectOneByUserId(userId);
        //更改账户信息
        Assert.isTrue(money < account.getAmount().doubleValue(), ResponseEnum.NOT_SUFFICIENT_FUNDS_ERROR);
        BigDecimal oldMoney = account.getAmount();
        BigDecimal oldFreeze = account.getFreezeAmount();
        oldMoney = oldMoney.subtract(new BigDecimal(money));
        oldFreeze = oldFreeze.add(new BigDecimal(money));
        account.setAmount(oldMoney);
        account.setFreezeAmount(oldFreeze);
        service.updateById(account);
        //填充值
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setAId(account.getAId());
        withdrawal.setApplyAmount(new BigDecimal(money));
        withdrawal.setRemainAmount(oldMoney);
        withdrawal.setAuditStatus(0);//未审核
        int insert = baseMapper.insert(withdrawal);
        Assert.isTrue(insert > 0,ResponseEnum.CREATE_FAIL);
        cashFlowService.Grant(account.getAId(),"申请提现",new BigDecimal(money),oldMoney);
    }

    @Override
    public Boolean Aufit(Long wId, Boolean pass) {
        //通过id找到记录
        Withdrawal withdrawal = baseMapper.selectById(wId);
        Account account = service.getById(withdrawal.getAId());
        if(StringUtils.checkValNull(withdrawal) || withdrawal.getAuditStatus() != 0 || withdrawal.getIsDelete() != 0){
            return false;
        }
        if(pass){
            //将冻结的资金取消
            BigDecimal applyAmount = withdrawal.getApplyAmount();
            BigDecimal freezeAmount = account.getFreezeAmount();
            freezeAmount = freezeAmount.subtract(applyAmount);
            account.setFreezeAmount(freezeAmount);
            service.updateById(account);
            //改变记录的状态
            withdrawal.setAuditStatus(1);
            baseMapper.updateById(withdrawal);

        }else {
            //将冻结的资金返回余额
            BigDecimal applyAmount = withdrawal.getApplyAmount();
            BigDecimal freezeAmount = account.getFreezeAmount();
            BigDecimal amount = account.getAmount();
            freezeAmount = freezeAmount.subtract(applyAmount);
            amount = amount.add(applyAmount);
            account.setFreezeAmount(freezeAmount);
            account.setAmount(amount);
            service.updateById(account);
            //改变记录的状态（未通过：2）
            withdrawal.setAuditStatus(2);
            baseMapper.updateById(withdrawal);
            //生成一条流水记录
            cashFlowService.inCome(account.getAId(),"申请失败",applyAmount,amount);
        }
        return true;
    }

    @Override
    public Boolean Canel(Long wId) {
        //通过id找到记录
        Withdrawal withdrawal = baseMapper.selectById(wId);
        if(StringUtils.checkValNull(withdrawal) || withdrawal.getAuditStatus() != 0 || withdrawal.getIsDelete() != 0){
            return false;
        }
        Account account = service.getById(withdrawal.getAId());
        //将冻结的资金返回余额
        BigDecimal applyAmount = withdrawal.getApplyAmount();
        BigDecimal freezeAmount = account.getFreezeAmount();
        BigDecimal amount = account.getAmount();
        freezeAmount = freezeAmount.subtract(applyAmount);
        amount = amount.add(applyAmount);
        account.setFreezeAmount(freezeAmount);
        account.setAmount(amount);
        service.updateById(account);
        //改变记录状态
        withdrawal.setIsDelete(1);
        baseMapper.updateById(withdrawal);
        //生成一条流水记录
        cashFlowService.inCome(account.getAId(),"申请失败",applyAmount,amount);
        return true;
    }

    @Override
    public List<Withdrawal> getAll(Long userId) {
        Account account = service.selectOneByUserId(userId);
        QueryWrapper<Withdrawal> wrapper = new QueryWrapper<>();
        wrapper.eq("a_id",account.getAId());
        List<Withdrawal> withdrawals = baseMapper.selectList(wrapper);
        return withdrawals;
    }

    @Override
    public IPage<Withdrawal> getWithDrawPage(Page<Withdrawal> withdrawalPage) {
        QueryWrapper<Withdrawal> wrapper = new QueryWrapper<>();
        wrapper.eq("audit_status",0);
        wrapper.eq("is_delete",0);
        wrapper.orderByDesc("create_time");
        Page<Withdrawal> selectPage = baseMapper.selectPage(withdrawalPage, wrapper);
        return selectPage;
    }
}
