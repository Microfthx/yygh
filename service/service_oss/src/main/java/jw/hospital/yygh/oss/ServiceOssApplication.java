package jw.hospital.yygh.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.oss
 * @Description:
 * @date 2022/12/01 16:36
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源配置 这样启动时不会报错
@EnableDiscoveryClient
@ComponentScan("jw.hospital")
public class ServiceOssApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOssApplication.class,args);
    }
}
