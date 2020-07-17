package org.starichkov.java.spring.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories
public class SpringBootRedisExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRedisExampleApplication.class, args);
    }

}
