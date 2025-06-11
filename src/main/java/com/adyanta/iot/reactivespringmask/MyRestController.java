package com.adyanta.iot.reactivespringmask;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MyRestController {
    @GetMapping("/users/{id}")
    public Mono<UserDTO> getUser(@PathVariable String id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setMaskedEmail("SSSSS");
        userDTO.setUsername("adyanta");

        return Mono.just(userDTO);
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "****";
        }
        String[] parts = email.split("@");
        String namePart = parts[0];
        String domainPart = parts[1];

        String maskedName = namePart.length() > 2
                ? namePart.substring(0, 2) + "***"
                : "***";

        return maskedName + "@" + domainPart;
    }

}
