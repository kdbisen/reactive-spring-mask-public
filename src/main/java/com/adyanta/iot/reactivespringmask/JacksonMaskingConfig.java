package com.adyanta.iot.reactivespringmask;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonMaskingConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer maskingCustomizer() {
        return builder -> builder.modules(new SimpleModule() {
            @Override
            public void setupModule(Module.SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new MaskingBeanSerializerModifier());
            }
        });
    }
}
