package com.xiyo.pfund.core.service;

import com.xiyo.pfund.core.pojo.entity.CashFlow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiyo.pfund.core.pojo.vo.IncomeOrOutcomeVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 资金流水表 服务类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
public interface CashFlowService extends IService<CashFlow> {

    void Grant(Long aId, String investName, BigDecimal decMoney,BigDecimal nowMoney);

    void inCome(Long aId, String investName, BigDecimal bigDecimal, BigDecimal nowAmount);

    List<CashFlow> getAll(Long userId);

    void earn(Long userId, IncomeOrOutcomeVO record);

    void lostMoney(Long userId, IncomeOrOutcomeVO record);
}
