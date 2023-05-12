package com.atguigu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启权限注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 放行
        http.headers().frameOptions().sameOrigin();
        // 放行静态资源
        http.authorizeRequests().antMatchers("/static/**","/login").permitAll().anyRequest().authenticated();
        // 登录成功
        //  http.formLogin().loginPage("/login") : 表示自定义登录页面
        // defaultSuccessUrl("/") : 登录成功，进入到indexcontroller里面的/方法
        http.formLogin().loginPage("/login").defaultSuccessUrl("/");
        // 退出（登出）
        // http.logout().logoutUrl("/logout") : 表示退出
        // logoutSuccessUrl("/login") : 退出成功之后，重新进入到登录页面
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");
        // 关闭防火墙
        http.csrf().disable();
         //添加自定义异常入口
//        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeineHandler());
    }








    //<bean id="passwordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder">
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
