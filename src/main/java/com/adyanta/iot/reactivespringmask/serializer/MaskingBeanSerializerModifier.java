package com.adyanta.iot.reactivespringmask.serializer;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;

public class MaskingBeanSerializerModifier extends BeanSerializerModifier {

    private final List<String> fieldsToMask;

    public MaskingBeanSerializerModifier(List<String> fieldsToMask) {
        this.fieldsToMask = fieldsToMask;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {

        for (BeanPropertyWriter writer : beanProperties) {
            if (fieldsToMask.contains(writer.getName())) {
                writer.assignSerializer(new MaskingSerializer(writer.getName()));
            }
        }

        return beanProperties;
    }
}
