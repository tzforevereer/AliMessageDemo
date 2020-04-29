package com.demo.aliservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //避免不连接数据库报错
public class AliApplication {
    public static void main(String[] args) {
        SpringApplication.run(AliApplication.class,args);
    }
}
