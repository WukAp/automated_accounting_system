package com.github.wukap.automatedAccountingSystem;

import com.github.wukap.automatedAccountingSystem.sqlDriver.UserRepository;
import com.github.wukap.automatedAccountingSystem.sqlDriver.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for Java Repository Template.
 */
@SpringBootApplication
public class Application
{
    @Autowired
    private static UserService userService;
    public static final String JAVA_REPOSITORY_TEMPLATE = "maven-template-repository";

    public static void main(String[] args) {
        var app = SpringApplication.run(Application.class, args);
        userService = app.getBean(UserService.class);
        System.out.println(app.getBean(UserRepository.class).getAllUsers());
        System.out.printf("Hello world, %s!%n",JAVA_REPOSITORY_TEMPLATE);
    }
}
