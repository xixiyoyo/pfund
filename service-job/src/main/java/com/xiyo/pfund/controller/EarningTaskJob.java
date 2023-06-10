package com.xiyo.pfund.controller;

import com.xiyo.common.uitl.RandomUtils;
import com.xiyo.pfund.comsume.CoreInvestmentClient;
import com.xiyo.pfund.comsume.CoreOrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;

@RestController

@Slf4j
public class EarningTaskJob {

    @Resource
    private CoreInvestmentClient coreInvestmentClient;

    @Resource
    private CoreOrderClient coreOrderClient;

    @Scheduled(cron = "0 0 20 ? * MON-FRI")
    public void DailyEarnings(){
        log.info("工作日每晚八点执行一次");
        //更新投资品的东西
        List<Long> ids = coreInvestmentClient.jobGetAllInvestInfo();
        for (Long id:ids) {
            Random random = new Random();
            Double doubles = random.nextDouble() * 0.2 - 0.1;
            log.info("改之前值:"+doubles);
//            Double rate = new BigDecimal(doubles).setScale(2,BigDecimal.ROUND_UP).doubleValue();
            DecimalFormat df = new DecimalFormat("#.00");
            String format = df.format(doubles);
            Double rate = new Double(format);
            log.info("改之后值:"+rate);
            coreInvestmentClient.jobUpdateInvestment(id,rate);
            coreOrderClient.updateEarn(id,rate);
        }
    }
}
