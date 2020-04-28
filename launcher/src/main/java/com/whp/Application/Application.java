package com.whp.Application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author : 张吉伟
 * @data : 2018/4/28 10:30
 * @descrpition :
 */
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"com.whp.*"})
@MapperScan("com.whp.*.*.Dao")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
