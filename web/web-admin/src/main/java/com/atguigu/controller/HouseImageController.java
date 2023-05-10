package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialStruct;
import java.util.UUID;

//  opt.openWin('/houseImage/uploadShow/[[${house.id}]]/1','上传房源图片',580,430);
@Controller
@RequestMapping("/houseImage")
public class HouseImageController {

    private final static String LIST_ACTION = "redirect:/house/";
    private final static String PAGE_UPLOED_SHOW = "house/upload";

    @Reference
    private HouseImageService houseImageService;

    @RequestMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId,@PathVariable Integer id,ModelMap modelMap){
        // 根据图片id，找到图片
        HouseImage houseImage = houseImageService.getById(id);
        // 删除的是数据库的图片
        houseImageService.delete(id);
        // 删除云服务器
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageUrl());
        return LIST_ACTION + houseId;
    }




//    var BASE_UPLOAD = '/houseImage/upload/[[${houseId}]]/[[${type}]]';
    @RequestMapping("/upload/{houseId}/{type}")
    @ResponseBody
    public Result upload(@PathVariable Long houseId,
                         @PathVariable Integer type,
                         @RequestParam(value = "file") MultipartFile[] files) throws Exception {
        // 判断上传是否有图片
        if (files.length > 0){
            // 当前图片上传，可以一次性传多张图片，图片需要一张张的上传
            for (MultipartFile file : files) {
                // 如果防止图片覆盖
                String newFileName = UUID.randomUUID().toString();
                // abc.jpg,abc.jpg
                QiniuUtils.upload2Qiniu(file.getBytes(),newFileName);
//                http://ru7qt46f8.hd-bkt.clouddn.com/0d9ca827-e54c-4919-846e-4bbc6a46e273.jpg

                String url = "http://ru829shg9.hn-bkt.clouddn.com/" + newFileName;

                // 插入图片
                HouseImage houseImage = new HouseImage();
                houseImage.setHouseId(houseId);
                houseImage.setImageName(newFileName);
                houseImage.setImageUrl(url);
                houseImage.setType(type);
                houseImageService.insert(houseImage);
            }
        }
        return Result.ok();

    }



    // 显示图片上传的对话框
     //1：房源图片 2：房产图片
    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable Long houseId, @PathVariable Integer type, ModelMap modelMap){
        modelMap.addAttribute("houseId",houseId);
        modelMap.addAttribute("type",type);
        return PAGE_UPLOED_SHOW;
    }
}
