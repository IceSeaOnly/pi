package com.binghai.pi;

import com.pi4j.io.gpio.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        
    }
}
