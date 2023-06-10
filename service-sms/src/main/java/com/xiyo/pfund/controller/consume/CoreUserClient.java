package com.xiyo.pfund.controller.consume;

import com.xiyo.pfund.controller.consume.fallback.CoreUserClientFallback;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-core",fallback = CoreUserClientFallback.class)
public interface CoreUserClient {

    @ApiOperation("检查手机号是否被注册")
    @GetMapping("api/core/user/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile);
}
