package com.adyanta.iot.reactivespringmask.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MaskingSerializer extends JsonSerializer<Object> {

    private final String fieldName;

    public MaskingSerializer(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if ("email".equals(fieldName)) {
            gen.writeString(maskEmail(value.toString()));
        } else {
            gen.writeString("****");
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
