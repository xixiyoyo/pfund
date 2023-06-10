package com.xiyo.pfund.core.service;

import com.xiyo.pfund.core.pojo.entity.InvestDetailVO;
import com.xiyo.pfund.core.pojo.entity.Investment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 投资类品表 服务类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
public interface InvestmentService extends IService<Investment> {

    InvestDetailVO getInvestDetail(Investment investment, Long userId);

    List<Investment> getAllInvestment(Long userId);

    void jobUpdateInvestment(Long iid, Double rate);
}
