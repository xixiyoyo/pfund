package com.xiyo.pfund.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiyo.pfund.core.pojo.entity.Withdrawal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 提现申请表 服务类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
public interface WithdrawalService extends IService<Withdrawal> {
    //创建提现申请单
    void Create(Double money, Long userId);

    //审核（管理员）
    Boolean Aufit(Long wId,Boolean pass);
    //取消
    Boolean Canel(Long wId);

    List<Withdrawal> getAll(Long userId);

    IPage<Withdrawal> getWithDrawPage(Page<Withdrawal> withdrawalPage);
}
