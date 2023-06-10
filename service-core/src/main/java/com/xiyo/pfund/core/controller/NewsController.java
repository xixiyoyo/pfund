package com.xiyo.pfund.core.controller;


import com.xiyo.pfund.core.pojo.entity.News;
import com.xiyo.pfund.core.service.NewsService;
import com.xiyo.common.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 新闻表 前端控制器
 * </p>
 *
 * @author xiyo
 * @since 2022-02-22
 */

//@Api(tags = "新闻管理")
//@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/api/core/news")
public class NewsController {
    @Resource
    private NewsService newsService;

    @ApiOperation("获取所有的新闻信息")
    @GetMapping("list")
    public Result GetAllNews(){
        return Result.ok().data("list",newsService.list());
    }

    @ApiOperation("删除一条新闻记录")
    @DeleteMapping("delete/{id}")
    public Result DeleteNews(@PathVariable long id){
        if(newsService.removeById(id)){
            return Result.ok().message("删除成功");
        }
        return Result.error().message("删除失败");
    }

    @ApiOperation("创建或更新一条数据")
    @PostMapping("createOrUpdate")
    public Result CreateNewsOrUpdateNews(News news){
        if(newsService.saveOrUpdate(news)){
            return Result.ok().message("操作成功");
        }else {
            return  Result.error().message("操作失败");
        }
    }
    @ApiOperation("创建一条数据")
    @PostMapping("create")
    public Result CreateNews(News news){
        if(newsService.save(news)){
            return Result.ok().message("操作成功");
        }
        return  Result.error().message("操作失败");
    }
    @ApiOperation("更新一条数据")
    @PutMapping("update")
    public Result UpdateNews(News news){
        if(newsService.updateById(news)){
            return Result.ok().message("操作成功");
        }
        return  Result.error().message("操作失败");
    }
    @ApiOperation("获取一条新闻数据")
    @GetMapping("getOne/{id}")
    public Result GetNewsById(@PathVariable(value = "id") Integer nid){
        return Result.ok().data("news",newsService.getById(nid));
    }
}

