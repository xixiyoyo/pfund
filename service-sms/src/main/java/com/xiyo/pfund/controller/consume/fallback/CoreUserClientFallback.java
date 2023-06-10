package com.xiyo.pfund.controller.consume.fallback;

import com.xiyo.pfund.controller.consume.CoreUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoreUserClientFallback implements CoreUserClient {
    @Override
    public boolean checkMobile(String mobile) {
        log.error("远程调用失败，服务熔断");
        return false;
    }
}
