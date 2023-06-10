package com.xiyo.pfund.core.service.impl;

import com.xiyo.pfund.core.pojo.entity.News;
import com.xiyo.pfund.core.mapper.NewsMapper;
import com.xiyo.pfund.core.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * 新闻表 服务实现类
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */
@Service

public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

}
