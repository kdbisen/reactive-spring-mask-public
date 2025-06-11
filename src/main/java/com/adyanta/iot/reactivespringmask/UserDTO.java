package com.adyanta.iot.reactivespringmask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    @Mask(type = MaskingType.PASSWORD)
    private String username;
    @Mask(type = MaskingType.EMAIL)
    private String maskedEmail;
}
