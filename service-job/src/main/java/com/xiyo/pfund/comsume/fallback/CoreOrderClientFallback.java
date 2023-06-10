package com.xiyo.pfund.comsume.fallback;

import com.xiyo.pfund.comsume.CoreOrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoreOrderClientFallback implements CoreOrderClient {
    @Override
    public void updateEarn(Long iid, Double rate) {
        log.error("远程调用失败，服务熔断");
    }
}
