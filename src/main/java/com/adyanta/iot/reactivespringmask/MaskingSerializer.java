package com.adyanta.iot.reactivespringmask;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MaskingSerializer extends JsonSerializer<Object> {

    private final MaskingType type;

    public MaskingSerializer(MaskingType type) {
        this.type = type;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        String strValue = value.toString();
        switch (type) {
            case EMAIL:
                gen.writeString(maskEmail(strValue));
                break;
            case PASSWORD:
                gen.writeString("****"); // fully masked
                break;
            case GENERIC:
            default:
                gen.writeString("****"); // generic mask
                break;
        }
    }

    private String maskEmail(String email) {
        if (!email.contains("@")) {
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
