package com.atguigu;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;

public class QiNiuTest {
    /**
     * 删除图片
     */
    @Test
    public void demo02() throws Exception{
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        String accessKey = "IbD8bslPdwv3LEAwMS8bz9n2tHFYimuzNOAoUCTw";
        String secretKey = "pa6-ZCMeUbDOXIVGa9tvuBh9voo8NwqyZwyLoDv1";
        // 空间的名称
        String bucket = "sz-java-230201";
        String key = "FoMAocn5A-LmDoDv6TqrVY2iPfUK";
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (Exception ex) {
        }
    }




    /**
     * 图片上传
     */
    @Test
    public void demo01() throws Exception{
        //构造一个带指定 Region 对象的配置类
        // 参数表示服务器存放的位置
        Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
        // 新建上传管理器
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "IbD8bslPdwv3LEAwMS8bz9n2tHFYimuzNOAoUCTw";
        String secretKey = "pa6-ZCMeUbDOXIVGa9tvuBh9voo8NwqyZwyLoDv1";
        // 空间的名称
        String bucket = "sz-java-230201";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "d:\\img\\abc.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        // 秘钥
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            // 上传数据
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (Exception ex2) {

        }
    }
}
