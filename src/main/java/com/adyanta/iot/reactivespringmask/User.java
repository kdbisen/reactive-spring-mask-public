package com.adyanta.iot.reactivespringmask;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    
    @JsonIgnore
    private String password;
    
    @JsonProperty("maskedEmail")
    public String getMaskedEmail() {
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

    private String email;
}
