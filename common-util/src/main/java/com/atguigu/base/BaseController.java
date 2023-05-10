package com.atguigu.base;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    protected Map<String, Object> getFilters(HttpServletRequest request) {
        // 获取前端传递过来的参数
        // 每次只能获取一个name
//        String pageNum = request.getParameter("pageNum");
//        String pageSize = request.getParameter("pageSize");
//        String roleName = request.getParameter("roleName");
        // 一次性获取页面所有的name,返回值是一个集合
        Enumeration parameterNames = request.getParameterNames();
        HashMap map = new HashMap<>();
        // 判断页面是否有数据，如果数据不为null，并且有下一个数据，获取下一个数据
        while (parameterNames!=null && parameterNames.hasMoreElements()){
            // 获取下一个元素
            String parameterName = (String) parameterNames.nextElement();
            // 根据name获取值
            String[] parameterValues = request.getParameterValues(parameterName);

            if (parameterValues!=null && parameterValues.length!=0){
                map.put(parameterName,parameterValues[0]);
            }
        }
        // 如果要实现分页，pageNum和pageSize必须传过来
        // 保证值一定得从前端传递到后端
        if (!map.containsKey("pageNum")){
            // 如果实在是前端没有传递过来数据，至少给一个默认值
            map.put("pageNum",1);
        }
        if (!map.containsKey("pageSize")){
            // 如果实在是前端没有传递过来数据，至少给一个默认值
            map.put("pageSize",2);
        }

        return map;
    }
}
