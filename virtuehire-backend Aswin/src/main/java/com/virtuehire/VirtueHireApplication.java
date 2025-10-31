package com.virtuehire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class VirtueHireApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(VirtueHireApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        // Serve uploaded files from project root uploads folder
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/Users/aswin/Desktop/VirtueHire-backend Aswin/uploads/");

        // Serve HR files from the same uploads folder
        registry.addResourceHandler("/hrs/file/**")
                .addResourceLocations("file:C:/Users/aswin/Desktop/VirtueHire-backend Aswin/uploads/");
    }
}