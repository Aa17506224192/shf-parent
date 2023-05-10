package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.util.SMSUtils;
import com.atguigu.vo.LoginVo;
import com.atguigu.vo.RegisterVo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    private UserInfoService userInfoService;


//    axios.get('/userInfo/logout')
    @RequestMapping("/logout")
    public Result logout(HttpServletRequest request){
        request.getSession().removeAttribute("USER");
        return Result.ok();
    }

    /**
     *  axios.post('/userInfo/login', this.loginVo)
     */
     @RequestMapping("/login")
     public Result login(@RequestBody LoginVo loginVo,HttpServletRequest request){
         String phone = loginVo.getPhone();
         String password = loginVo.getPassword();
         // 判断手机号码和密码是否为null
         if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)){
             return Result.build(null, ResultCodeEnum.PARAM_ERROR);
         }
         // 根据手机号码查询
         UserInfo userInfo = userInfoService.getByPhone(phone);
         if (userInfo == null){
             return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
         }

         if (!MD5.encrypt(password).equals(userInfo.getPassword())){
             return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
         }
         // 在首页回显数据
         request.getSession().setAttribute("USER",userInfo);
         Map<String, Object> map = new HashMap<>();
         map.put("phone",userInfo.getPhone());
         map.put("nickName",userInfo.getNickName());
         return Result.ok(map);
     }



    /**
     * 注册：注册的本质实际上往数据库里面插入一条数据
     * 思路：
     * ① 获取数据
     * ② 判断前端传递过来的数据是否有值
     * ③ 校验验证码
     * ④ 保存到数据库
     * @param registerVo
     * @return
     */
//    axios.post('/userInfo/register', this.registerVo)
    @RequestMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo,HttpServletRequest request){
        // ① 获取数据
        String phone = registerVo.getPhone();
        // 这个验证码是用户输入的
        String code = registerVo.getCode();
        String password = registerVo.getPassword();
        String nickName = registerVo.getNickName();
        //② 判断前端传递过来的数据是否有值
        if (StringUtils.isEmpty(phone)||StringUtils.isEmpty(code)||
           StringUtils.isEmpty(password) ||StringUtils.isEmpty(nickName)){
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        // ③ 校验验证码
        // 获取手机号码发送的验证码
        String currentCode  = (String) request.getSession().getAttribute("CODE");
        if (!code.equals(currentCode)){
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }

        // ④ 保存到数据库,数据库能发重复注册
        UserInfo userInfo =  userInfoService.getByPhone(phone);
        if (null!=userInfo){
            // 说明当前用户已经注册
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }
        // 保存数据
        userInfo = new UserInfo();
        userInfo.setPhone(phone);
        userInfo.setPassword(MD5.encrypt(password));
        userInfo.setNickName(nickName);
        userInfo.setStatus(1);
        userInfoService.insert(userInfo);
        return Result.ok();
    }


    // 往手机号码上面发送验证码
//    axios.get('/userInfo/sendCode/'+this.registerVo.phone).then(function (response)
    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone, HttpServletRequest request) throws Exception{
        // 随机4位或者6位数字
        // 随机生成了一个验证码
        String code ="1111";
//        String code = generateValidateCode(4).toString();
        // 需要把验证码存下来，往哪里存都可以，可以存到session里面，也可以存到redis，也可以存到mysql
        request.getSession().setAttribute("CODE",code);
        // 需要把验证码发送给用户
      //  SMSUtils.sendShortMessage(phone,code);
//        System.out.println("已经发送了验证码");
        return Result.ok(code);
    }
    /**
     * 生成短信验证码
     */
    private Integer generateValidateCode(int length) {
        Integer code = null;
        // 判断验证码的位数
        if (length == 4){
            code = new Random().nextInt(9000);
            code = code+1000;
        }else if (length==6){
            code = new Random().nextInt(900000);
            code = code+100000;
        }else {
            throw new RuntimeException("只能生成4位或者6位验证码");
        }
        return code;
    }



}
