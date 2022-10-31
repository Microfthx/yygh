package jw.hospital.yygh.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author HXLY
 * @PackageName: jw.hospital.yygh.gateway
 * @Description:
 * @date 2022/10/27 16:14
 */
@SpringBootApplication
public class ServerGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class,args);
    }
}
