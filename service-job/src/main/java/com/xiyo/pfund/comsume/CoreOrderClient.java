package com.xiyo.pfund.comsume;

import com.xiyo.pfund.comsume.fallback.CoreOrderClientFallback;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-core",fallback = CoreOrderClientFallback.class)
public interface CoreOrderClient {

    @PostMapping("api/core/investOrder/updateEarnings")
    @ApiOperation("job更新我们的订单收益和账户收益")
    public void updateEarn(@RequestParam(value = "iid") Long iid, @RequestParam(value = "rate") Double rate);
}
