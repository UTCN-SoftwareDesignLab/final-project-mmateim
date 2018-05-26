package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@ComponentScan({"demo.repository", "demo.dto", "demo.entity","demo.service", "demo.controller","demo.config", "demo.websocket"})
@PropertySource({"classpath:application.properties"})
@EntityScan(basePackages ={"demo.entity"})
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"demo.repository"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("hello world");
    }
}
