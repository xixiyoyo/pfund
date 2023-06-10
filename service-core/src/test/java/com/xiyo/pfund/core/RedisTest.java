package com.xiyo.pfund.core;

import com.xiyo.pfund.core.mapper.NewsMapper;
import com.xiyo.pfund.core.pojo.entity.News;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private NewsMapper newsMapper;

    @Test
    public void readTest(){
        News news = (News) redisTemplate.opsForValue().get("news");
        System.out.println(news);
    }

    @Test
    public void writeTest(){
        System.out.println("ttttttttttttt");
        //Assert.isNull(newsMapper,ResponseEnum.WEIXIN_FETCH_USERINFO_ERROR);
        News news = newsMapper.selectById(1);
        System.out.println(news);
        //向redis中存入数据库的数据，以string的方式存入键值对，过期时间为5分钟
        redisTemplate.opsForValue().set("news",news,5, TimeUnit.MINUTES);
    }

}
