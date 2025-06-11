package com.adyanta.iot.reactivespringmask.handler;

import com.adyanta.iot.reactivespringmask.serializer.MaskingBeanSerializerModifier;
import com.adyanta.iot.reactivespringmask.annotation.Mask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.HandlerResultHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // ensure it runs before default handlers
public class MaskingHandlerResultHandler implements HandlerResultHandler {

    private final ObjectMapper objectMapper;
    private final ServerCodecConfigurer codecConfigurer;

    public MaskingHandlerResultHandler(ObjectMapper objectMapper, ServerCodecConfigurer codecConfigurer) {
        this.objectMapper = objectMapper;
        this.codecConfigurer = codecConfigurer;
    }

    @Override
    public boolean supports(HandlerResult result) {
        // Support any Mono or plain Object:
        return result.getReturnValue() instanceof Mono<?> || result.getReturnValue() != null;
    }

    @Override
    public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {

        Object handler = exchange.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

        List<String> maskFields = null;

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            Mask mask = handlerMethod.getMethodAnnotation(Mask.class);
            if (mask != null) {
                maskFields = Arrays.asList(mask.value());
            }
        }

        // Proceed to render the body with masking if needed:
        Object returnValue = result.getReturnValue();
        Mono<?> bodyMono;

        if (returnValue instanceof Mono<?>) {
            bodyMono = (Mono<?>) returnValue;
        } else {
            bodyMono = Mono.just(returnValue);
        }

        List<String> finalMaskFields = maskFields;

        return bodyMono.flatMap(body -> {

            // If masking is needed, use special ObjectMapper:
            ObjectMapper mapperToUse = objectMapper;

            if (finalMaskFields != null && !finalMaskFields.isEmpty()) {
                mapperToUse = createMaskingObjectMapper(finalMaskFields);
            }

            DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
            ServerHttpResponse response = exchange.getResponse();
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            try {
                byte[] jsonBytes = mapperToUse.writeValueAsBytes(body);
                DataBuffer buffer = bufferFactory.wrap(jsonBytes);
                return response.writeWith(Mono.just(buffer));
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
        });
    }

    private ObjectMapper createMaskingObjectMapper(List<String> fieldsToMask) {
        ObjectMapper mapper = objectMapper.copy();

        SimpleModule module = new SimpleModule();
        module.setSerializerModifier(new MaskingBeanSerializerModifier(fieldsToMask));
        mapper.registerModule(module);

        return mapper;
    }
}
