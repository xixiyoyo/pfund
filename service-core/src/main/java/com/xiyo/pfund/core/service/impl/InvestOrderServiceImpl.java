package com.xiyo.pfund.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiyo.pfund.core.mapper.*;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.pojo.entity.InvestOrder;
import com.xiyo.pfund.core.pojo.entity.Investment;
import com.xiyo.pfund.core.pojo.entity.User;
import com.xiyo.pfund.core.pojo.vo.InvestorVO;
import com.xiyo.pfund.core.service.CashFlowService;
import com.xiyo.pfund.core.service.InvestOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 投资订单表 服务实现类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Service
public class InvestOrderServiceImpl extends ServiceImpl<InvestOrderMapper, InvestOrder> implements InvestOrderService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private InvestmentMapper investmentMapper;

    @Resource
    private CashFlowService cashFlowService;

    @Override
    public List<InvestorVO> getNumerousByIId(Long id) {

        QueryWrapper<InvestOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("i_id",id);
        wrapper.eq("is_delete",0);
        wrapper.orderByDesc("create_time");
        List<InvestorVO> investorVOS = new ArrayList<>();
        List<InvestOrder> list = baseMapper.selectList(wrapper);
        if(list.isEmpty()){
            return investorVOS;
        }

//        HashMap<Long, BigDecimal> map = new HashMap<Long, BigDecimal>();
        list.forEach(item ->{
            Account account = accountMapper.selectById(item.getAId());
            User user = userMapper.selectById(account.getId());
            //Long userid = userMapper.selectById(item.getId()).getId();
//            String name = userMapper.selectById(item.getId()).getNickName();
//            if(map.containsKey(userid)){
//                BigDecimal account = map.get(userid);
//                account = account.add(item.getInvestPay());
//                map.put(userid,account);
//            }else {
//                map.put(userid,item.getInvestPay());
//            }
            InvestorVO investorVO = new InvestorVO();
            investorVO.setInvestName(user.getNickName());
            investorVO.setInvestAmount(item.getInvestPay());
            investorVO.setInvestTime(item.getCreateTime());
            investorVOS.add(investorVO);
        });

//        map.forEach((key,value) ->{
//            User user = userMapper.selectById(key);
//            InvestorVO investorVO = new InvestorVO();
//            investorVO.setInvestName(user.getNickName());
//            investorVO.setInvestAmount(value);
//            investorVOS.add(investorVO);
//        });

        return investorVOS;
    }

    @Override
    @Transactional
    public Boolean Purchase(Long iid, Double money, Account account) {
        //检查是否已经购买过
        QueryWrapper<InvestOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("i_id",iid);
        wrapper.eq("a_id",account.getAId());
        wrapper.eq("is_delete",0);
        InvestOrder investOrder = baseMapper.selectOne(wrapper);
        Investment investment = investmentMapper.selectById(iid);
        if(money > account.getAmount().doubleValue()){
            return false;
        }
        if(investOrder == null ){
            //减去账户余额 增加投资品总额
            BigDecimal nowAmount = transToInvest(money,account,investment);
            //创建订单信息
            InvestOrder investOrder1 = new InvestOrder();
            investOrder1.setIId(investment.getIId());
            investOrder1.setAId(account.getAId());
            investOrder1.setAip(new BigDecimal(0));
            investOrder1.setDailyYield(new BigDecimal(0));
            investOrder1.setInvestPay(new BigDecimal(money));
            investOrder1.setInvestRate(0.00);
            baseMapper.insert(investOrder1);
            //生成一条流水记录
            cashFlowService.Grant(account.getAId(),investment.getInvestName(),new BigDecimal(money),nowAmount);

        }else {
            //向投资品转账
            BigDecimal nowAmount = transToInvest(money,account,investment);
            //更新订单金额
            BigDecimal investPay = investOrder.getInvestPay();
            investPay = investPay.add(new BigDecimal(money));
            investOrder.setInvestPay(investPay);
            baseMapper.updateById(investOrder);
            //生成一条记录流水
            cashFlowService.Grant(account.getAId(),investment.getInvestName(),new BigDecimal(money),nowAmount);
        }
        return true;
    }

    private BigDecimal transToInvest(Double money,Account account,Investment investment){
        //减去账户余额
        BigDecimal decMoney = new BigDecimal(money);
        BigDecimal nowMoney = account.getAmount();
        nowMoney = nowMoney.subtract(decMoney);
        account.setAmount(nowMoney);
        accountMapper.updateById(account);
        //更新投资品
        BigDecimal investAmount = investment.getInvestAmount();
        investAmount = investAmount.add(decMoney);
        investment.setInvestAmount(investAmount);
        investmentMapper.updateById(investment);
        return nowMoney;
    }

    public Boolean sellInvestment(Long iid, Double money, Account account){
        QueryWrapper<InvestOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("i_id",iid);
        wrapper.eq("a_id",account.getAId());
        wrapper.eq("is_delete",0);
        InvestOrder investOrder = baseMapper.selectOne(wrapper);
        Investment investment = investmentMapper.selectById(iid);
        if(investOrder == null){
            return false;
        }
        if(investOrder.getInvestPay().doubleValue() < money){
            return false;
        }
        if(investment.getInvestAmount().doubleValue() < money){
            return false;
        }
        BigDecimal nowAmount = transToAmount(money,account,investment);
        //跟新订单金额
        BigDecimal investPay = investOrder.getInvestPay();
        investPay = investPay.subtract(new BigDecimal(money));
        investOrder.setInvestPay(investPay);
        baseMapper.updateById(investOrder);
        //生成一条收入流水
        cashFlowService.inCome(account.getAId(),investment.getInvestName(),new BigDecimal(money),nowAmount);
        return true;
    }

    @Override
    public InvestOrder getMyOrder(Long id, Account account) {

        QueryWrapper<InvestOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("i_id",id);
        wrapper.eq("a_id",account.getAId());
        InvestOrder investOrder = baseMapper.selectOne(wrapper);
        if(investOrder == null){
            return null;
        }
        return investOrder;
    }

    @Override
    public List<InvestOrder> getAll(Account account) {
        QueryWrapper<InvestOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("a_id",account.getAId());
        List<InvestOrder> investOrders = baseMapper.selectList(wrapper);
        return investOrders;
    }

    @Override
    public void updateEarn(Long iid, Double rate) {
        //通过iid找到所有的订单，然后根据rate进行更新
        Investment investment = investmentMapper.selectById(iid);
        QueryWrapper<InvestOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("i_id",iid);
        wrapper.gt("invest_pay",0);
        List<InvestOrder> investOrders = baseMapper.selectList(wrapper);
        //然后根据rate进行更新
        for (InvestOrder order :
            investOrders) {
            BigDecimal investPay = order.getInvestPay();
            BigDecimal aip = order.getAip();
            double amount = investPay.doubleValue() * (1 + rate);
            double dayEarn = investPay.doubleValue() * rate;
            order.setInvestPay(new BigDecimal(amount));
            order.setDailyYield(new BigDecimal(dayEarn));
            aip = aip.add(new BigDecimal(dayEarn));
            order.setAip(aip);
            double earned = aip.doubleValue();
            Double earnYield =  new BigDecimal(earned/(amount - earned)).setScale(2,BigDecimal.ROUND_UP).doubleValue();
            order.setInvestRate(earnYield);
            order.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(order);
            //更新账户的收益
            Account account = accountMapper.selectById(order.getAId());
            BigDecimal profit = account.getProfit();
            profit = profit.add(new BigDecimal(dayEarn));
            account.setProfit(profit);
            account.setUpdateTime(LocalDateTime.now());
            accountMapper.updateById(account);
        }
    }

    private BigDecimal transToAmount(Double money, Account account, Investment investment) {

        BigDecimal addMoney = new BigDecimal(money);
        BigDecimal nowMoney = account.getAmount();
        nowMoney = nowMoney.add(addMoney);
        account.setAmount(nowMoney);
        accountMapper.updateById(account);
        //更新投资品
        BigDecimal investAmount = investment.getInvestAmount();
        investAmount = investAmount.subtract(addMoney);
        investment.setInvestAmount(investAmount);
        investmentMapper.updateById(investment);
        return nowMoney;
    }
}
