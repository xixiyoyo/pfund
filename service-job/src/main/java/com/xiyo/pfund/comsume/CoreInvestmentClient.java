package com.xiyo.pfund.comsume;

import com.xiyo.pfund.comsume.fallback.CoreInvestmentClientFallback;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "service-core",fallback = CoreInvestmentClientFallback.class)
public interface CoreInvestmentClient {

    @ApiOperation("job调用：获取所有的投资品id")
    @GetMapping("api/core/investment/jobGetAllId")
    public List<Long> jobGetAllInvestInfo();

    @ApiOperation("job调用：每日更新投资品信息")
    @PostMapping("api/core/investment/jobUpdate")
    public void jobUpdateInvestment(@RequestParam(value = "iid") Long iid, @RequestParam(value = "rate") Double rate);
}
