package de.marcusjanke.examples.spring.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

    @GetMapping("/authenticated")
    public String checkAuthenticated(Principal user) {
        return "You're authenticated, " + user.getName() + "!";
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('SCOPE_my-hello-resource/hello.read')")
    public String hello(Principal user) {
        return "Hello " + user.getName();
    }

    @GetMapping("/user-info")
    @PreAuthorize("hasAuthority('SCOPE_my-hello-resource/user.read')")
    public String userInfo(Principal user) {
        return "Confidential user data";
    }
}