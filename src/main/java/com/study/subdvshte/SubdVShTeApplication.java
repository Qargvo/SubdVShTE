package com.study.subdvshte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SubdVShTeApplication {
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SubdVShTeApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SubdVShTeApplication.class, args);
    }

}
