package com.xiyo.pfund.core.service;

import com.xiyo.pfund.core.pojo.entity.Account;
import com.xiyo.pfund.core.pojo.entity.InvestOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiyo.pfund.core.pojo.vo.InvestorVO;

import java.util.List;

/**
 * <p>
 * 投资订单表 服务类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
public interface InvestOrderService extends IService<InvestOrder> {

    List<InvestorVO> getNumerousByIId(Long id);

    Boolean Purchase(Long iid, Double money, Account account);

    Boolean sellInvestment(Long iid, Double money, Account account);

    InvestOrder getMyOrder(Long id, Account account);

    List<InvestOrder> getAll(Account account);

    void updateEarn(Long iid, Double rate);
}
