package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//<a class="J_menuItem" href="/community" data-index="0">小区管理</a>
@RequestMapping("/community")
@Controller
public class CommunityController extends BaseController {

    @Reference
    private DictService dictService;

    @Reference
    private CommunityService communityService;


    private final static String LIST_ACTION = "redirect:/community";

    private final static String PAGE_INDEX = "community/index";
    private final static String PAGE_SHOW = "community/show";
    private final static String PAGE_CREATE = "community/create";
    private final static String PAGE_EDIT = "community/edit";
    private final static String PAGE_SUCCESS = "common/successPage";

    /**
     * 根据id删除小区
     *  opt.confirm('/community/delete/'+id);
     */
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        communityService.delete(id);
        return LIST_ACTION;
    }



    /**
     * 更新小区数据
     * th:action="@{/community/update}"
     */
     @RequestMapping("/update")
     public String update(Community community){
         communityService.update(community);
         return PAGE_SUCCESS;
     }



    /**
     * 回显小区数据
     *  opt.openWin('/community/edit/' + id,'修改',580,430);
     *  实现思路
     *  ① 获取前端传递过来的小区id
     *  ② 根据小区id查询小区，通过区域id和板块id，查询区域和板块的名称
     *  ③ 返回数据给前端
     */
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id,ModelMap modelMap){
        // 根据id进行查询，目的是回显数据
        Community community = communityService.getById(id);
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        modelMap.addAttribute("areaList",areaList);
        modelMap.addAttribute("community",community);
        return PAGE_EDIT;

    }




    /**
     * 增加小区数据
     * action="/community/save"
     */
    @RequestMapping("/save")
    public String save(Community community){
        communityService.insert(community);
        return PAGE_SUCCESS;
    }


    /**
     * 展示新增页面
     * opt.openWin('/community/create','新增',630,430)
     * 思路：
     * ① 获取北京市的区域和板块
     * ② 展示到页面
     */
    @RequestMapping("/create")
    public String create(ModelMap modelMap){
        List<Dict> areaList = dictService.findListByDictCode("beijing");
        modelMap.addAttribute("areaList",areaList);
        return PAGE_CREATE;
    }






    /**
     * 实现小区分页管理实现思路
     * ① 点击菜单的小区管理 /community
     * ② 获取分页数据调用getFilters(request)
     * ③ 调用分页findPage，返回分页数据，把分页数据返回给前端
     * ④ 返回区域集合数据给前端
     * ⑤ 页面回显小区名字
     *
     * @param modelMap
     * @param request
     * @return
     */
    @RequestMapping
    public String index(ModelMap modelMap, HttpServletRequest request){
        // 获取分页参数
        Map<String, Object> filters = getFilters(request);
        // 实现分页
        PageInfo<Community> page = communityService.findPage(filters);



        // 获取前端传递过来的区域id和板块id
        if (!filters.containsKey("areaId")){
            filters.put("areaId","");
        }

        if (!filters.containsKey("plateId")){
            filters.put("plateId","");
        }
        // 根据北京获取北京下面所有的区域
        // http://localhost:8000/dict/findListByDictCode/beijing
        List<Dict> areaList = dictService.findListByDictCode("beijing");

        modelMap.addAttribute("page",page);
        modelMap.addAttribute("filters",filters);
        modelMap.addAttribute("areaList",areaList);

        return PAGE_INDEX;

    }

}
