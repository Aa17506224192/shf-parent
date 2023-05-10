package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

//<a class="J_menuItem" href="/dict" data-index="0">数据字典</a>
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController {

    @Reference
    private DictService dictService;

    private final static String PAGE_INDEX = "dict/index";

    /**
     * 根据父节点，获取所有的字节点
     */
    @RequestMapping("/findListByParentId/{parentId}")
    @ResponseBody
    public Result<List<Dict>> findListByParentId(@PathVariable Long parentId){
        List<Dict> list = dictService.findListByParentId(parentId);
        return Result.ok(list);
    }

    /**
     * 根据北京获取北京下面的区域，根据区域获取下面的板块
     */
    @RequestMapping("/findListByDictCode/{dictCode}")
    @ResponseBody
    public Result<List<Dict>> findListByDictCode(@PathVariable String dictCode){
        List<Dict> list = dictService.findListByDictCode(dictCode);
        return Result.ok(list);
    }






    /**
     * 获取树控件所有的节点
     * @ResponseBody : 因为树控件返回的是json数据，所以这个地方需要添加@ResponseBody这个注解
     * @Target(ElementType.PARAMETER) : 表示@RequestParam这个注解只能放到参数身上
     * @Documented ： doc文档
     * @RequestParam ；表示请求参数，如果添加了这个注解，那么这个参数必须得传值，如果没有给值，给一个默认值是0
     */
    @RequestMapping("/findZnodes")
    @ResponseBody
    public Result findZnodes(@RequestParam(value = "id",defaultValue = "0") Long id){
       List<Map<String,Object>> zNodes =  dictService.findZnodes(id);
       return Result.ok(zNodes);
    }

    @RequestMapping
    public String index(){
        return PAGE_INDEX;
    }
}
