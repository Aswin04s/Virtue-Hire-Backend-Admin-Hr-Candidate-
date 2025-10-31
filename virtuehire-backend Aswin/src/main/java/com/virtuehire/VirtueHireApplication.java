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

        // Serve uploaded files
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/Users/aswin/Desktop/Virtue Hire/virtuehire-payment/virtuehire-backend Aswin/uploads/");

        // Serve HR files
        registry.addResourceHandler("/hrs/file/**")
                .addResourceLocations("file:C:/Users/aswin/Desktop/Virtue Hire/virtuehire-payment/virtuehire-backend Aswin/uploads/");
    }
}
