package com.xiyo.pfund.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiyo.pfund.core.mapper.InvestOrderMapper;
import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.pojo.entity.InvestDetailVO;
import com.xiyo.pfund.core.pojo.entity.InvestOrder;
import com.xiyo.pfund.core.pojo.entity.Investment;
import com.xiyo.pfund.core.mapper.InvestmentMapper;
import com.xiyo.pfund.core.service.AccountService;
import com.xiyo.pfund.core.service.InvestmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>
 * 投资类品表 服务实现类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Service
@Slf4j
public class InvestmentServiceImpl extends ServiceImpl<InvestmentMapper, Investment> implements InvestmentService {

    @Resource
    private AccountService accountService;

    @Resource
    private InvestOrderMapper investOrderMapper;

    @Override
    public InvestDetailVO getInvestDetail(Investment investment, Long userId) {

        InvestDetailVO investDetail = new InvestDetailVO();
        Account account = accountService.selectOneByUserId(userId);
        investDetail.setAccount(account);
        investDetail.setInvestment(investment);
        QueryWrapper<InvestOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("i_id",investment.getIId());
        wrapper.eq("is_delete",0);
        List<InvestOrder> investOrders = investOrderMapper.selectList(wrapper);
        if(investOrders.size() <= 0){
            return investDetail;
        }
//        Double money = null;
        investDetail.setNumerous(investOrders.size());
//        investOrders.forEach(item ->{
////            money += item.getInvestPay().doubleValue();
//            money = money +
//        }
        BigDecimal decimal = investOrders.stream().map(InvestOrder::getInvestPay).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        investDetail.setAmountMoeny(decimal.doubleValue());
        return investDetail;
    }

    @Override
    public List<Investment> getAllInvestment(Long userId) {
        Account account = accountService.selectOneByUserId(userId);
        QueryWrapper<InvestOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("a_id",account.getAId());
        List<InvestOrder> investOrders = investOrderMapper.selectList(wrapper);
        List<Investment> investments = new ArrayList<>();
//        HashMap<String, Object> map = new HashMap<>();
        investOrders.forEach(investOrder -> {
            Investment investment = baseMapper.selectById(investOrder.getIId());
            investments.add(investment);
        });
        return investments;
    }

    @Override
    public void jobUpdateInvestment(Long iid, Double rate) {
        Investment investment = baseMapper.selectById(iid);
        investment.setDateRate(rate);
        Double doubles = investment.getUnitWorth() * (1 + rate);
        log.info("unit will change:"+doubles);
        DecimalFormat df = new DecimalFormat("#.000");
        String format = df.format(doubles);
        Double unit = new Double(format);
        log.info("unit changed :"+unit);
        investment.setUnitWorth(unit);
        BigDecimal investAmount = investment.getInvestAmount();
        investAmount = investAmount.multiply(new BigDecimal(1+rate));
        investment.setInvestAmount(investAmount);
        investment.setUpdateTime(LocalDateTime.now());
        baseMapper.updateById(investment);
    }
}
