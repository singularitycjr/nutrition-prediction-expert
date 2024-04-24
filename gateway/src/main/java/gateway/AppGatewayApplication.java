package gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
public class AppGatewayApplication {

    public static void main(String[] args) {

        SpringApplication.run(AppGatewayApplication.class, args);
        log.info("gateway启动成功");
    }

}
