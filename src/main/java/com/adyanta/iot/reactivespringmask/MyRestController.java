package com.adyanta.iot.reactivespringmask;

import com.adyanta.iot.reactivespringmask.annotation.Mask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MyRestController {

    @Mask({"email", "password"})
    @GetMapping("/users/{id}")
    public Mono<User> getUser(@PathVariable String id) {
        User userDTO = new User();
        userDTO.setId(id);
        userDTO.setEmail("email");
        userDTO.setUsername("adyanta");
        userDTO.setPassword("password");

        return Mono.just(userDTO);
    }


}
