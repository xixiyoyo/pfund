package com.xiyo.pfund.comsume.fallback;

import com.xiyo.pfund.comsume.CoreInvestmentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CoreInvestmentClientFallback implements CoreInvestmentClient {
    @Override
    public List<Long> jobGetAllInvestInfo() {
        log.error("远程调用失败，服务熔断");
        return null;
    }

    @Override
    public void jobUpdateInvestment(Long iid, Double rate) {
        log.error("远程调用失败，服务熔断");
    }
}
