package jw.hospital.yygh.msm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.msm
 * @Description:
 * @date 2022/11/14 17:12
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源配置 这样启动时不会报错
@EnableDiscoveryClient
@ComponentScan("jw.hospital")
public class ServiceMsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceMsmApplication.class,args);
    }

}
