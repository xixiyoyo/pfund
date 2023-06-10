package com.xiyo.pfund.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiyo.common.uitl.RandomUtils;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.pojo.entity.CashFlow;
import com.xiyo.pfund.core.mapper.CashFlowMapper;
import com.xiyo.pfund.core.pojo.vo.IncomeOrOutcomeVO;
import com.xiyo.pfund.core.service.AccountService;
import com.xiyo.pfund.core.service.CashFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * <p>
 * 资金流水表 服务实现类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Service
public class CashFlowServiceImpl extends ServiceImpl<CashFlowMapper, CashFlow> implements CashFlowService {

    @Resource
    private AccountService accountService;

    @Override
    public void Grant(Long aId, String investName, BigDecimal decMoney,BigDecimal nowMoney) {
        CashFlow cashFlow = new CashFlow();
        String fourBitRandom = RandomUtils.getFourBitRandom();
        String randoms = ""+ LocalDate.now().getYear()+LocalDate.now().getMonthValue()+LocalDate.now().getDayOfMonth()+ LocalDateTime.now().getHour()+
            LocalDateTime.now().getMinute()+LocalDateTime.now().getSecond() + fourBitRandom;
        cashFlow.setCashNo("Cash"+ randoms);
        cashFlow.setCashAmount(decMoney);
        cashFlow.setNowAmount(nowMoney);
        cashFlow.setCashTypeName("支付");
        cashFlow.setCashType(0);
        cashFlow.setCashName(investName);
        cashFlow.setAId(aId);
        baseMapper.insert(cashFlow);
    }

    @Override
    public void inCome(Long aId, String investName, BigDecimal bigDecimal, BigDecimal nowAmount) {
        CashFlow cashFlow = new CashFlow();
        String fourBitRandom = RandomUtils.getFourBitRandom();
        String randoms = ""+ LocalDate.now().getYear()+LocalDate.now().getMonthValue()+LocalDate.now().getDayOfMonth()+ LocalDateTime.now().getHour()+
            LocalDateTime.now().getMinute()+LocalDateTime.now().getSecond() + fourBitRandom;
        cashFlow.setCashNo("Cash"+ randoms);
        cashFlow.setAId(aId);
        cashFlow.setCashType(1);
        cashFlow.setCashTypeName("收入");
        cashFlow.setCashName(investName);
        cashFlow.setCashAmount(bigDecimal);
        cashFlow.setNowAmount(nowAmount);
        baseMapper.insert(cashFlow);
    }

    @Override
    public List<CashFlow> getAll(Long userId) {
        Account account = accountService.selectOneByUserId(userId);
        QueryWrapper<CashFlow> wrapper = new QueryWrapper<>();
        wrapper.eq("a_id",account.getAId());
        wrapper.orderByDesc("create_time");
        List<CashFlow> list = baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public void earn(Long userId, IncomeOrOutcomeVO record) {
        Account account = accountService.selectOneByUserId(userId);
        BigDecimal amount = account.getAmount();
        BigDecimal money = record.getAmount();
        amount = amount.add(money);
        account.setAmount(amount);
        accountService.updateById(account);
        inCome(account.getAId(),"收入进账",money,amount,record.getMemo());
    }

    @Override
    public void lostMoney(Long userId, IncomeOrOutcomeVO record) {
        Account account = accountService.selectOneByUserId(userId);
        BigDecimal amount = account.getAmount();
        BigDecimal money = record.getAmount();
        amount = amount.subtract(money);
        account.setAmount(amount);
        accountService.updateById(account);
        Grant(account.getAId(),"支出出账",money,amount,record.getMemo());
    }

    public void inCome(Long aId, String investName, BigDecimal bigDecimal, BigDecimal nowAmount,String memo) {
        CashFlow cashFlow = new CashFlow();
        String fourBitRandom = RandomUtils.getFourBitRandom();
        String randoms = ""+ LocalDate.now().getYear()+LocalDate.now().getMonthValue()+LocalDate.now().getDayOfMonth()+ LocalDateTime.now().getHour()+
            LocalDateTime.now().getMinute()+LocalDateTime.now().getSecond() + fourBitRandom;
        cashFlow.setCashNo("Cash"+ randoms);
        cashFlow.setAId(aId);
        cashFlow.setCashType(1);
        cashFlow.setCashTypeName("收入");
        cashFlow.setCashName(investName);
        cashFlow.setCashAmount(bigDecimal);
        cashFlow.setNowAmount(nowAmount);
        cashFlow.setMemo(memo);
        baseMapper.insert(cashFlow);
    }

    public void Grant(Long aId, String investName, BigDecimal decMoney,BigDecimal nowMoney,String memo) {
        CashFlow cashFlow = new CashFlow();
        String fourBitRandom = RandomUtils.getFourBitRandom();
        String randoms = ""+ LocalDate.now().getYear()+LocalDate.now().getMonthValue()+LocalDate.now().getDayOfMonth()+ LocalDateTime.now().getHour()+
            LocalDateTime.now().getMinute()+LocalDateTime.now().getSecond() + fourBitRandom;
        cashFlow.setCashNo("Cash"+ randoms);
        cashFlow.setCashAmount(decMoney);
        cashFlow.setNowAmount(nowMoney);
        cashFlow.setCashTypeName("支付");
        cashFlow.setCashType(0);
        cashFlow.setCashName(investName);
        cashFlow.setAId(aId);
        cashFlow.setMemo(memo);
        baseMapper.insert(cashFlow);
    }
}
