package com.pig4cloud.pigx.common.gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.resource.NoResourceFoundException;

/**
 * @author lengleng
 * @date 2023-07-30
 * <p>
 * 网关异常通用处理器，只作用在webflux 环境下 , 优先级低于 ReactiveNoResourceFoundHandler 执行
 */
@Slf4j
@Order(-1)
@Configuration
@RequiredArgsConstructor
public class GlobalExceptionConfiguration implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // header set
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatusCode());
        }

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                log.error("Error request :{} Error Spring Cloud Gateway : {}", exchange.getRequest().getPath(),
                        ex.getMessage());
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(R.failed(ex.getMessage())));
            } catch (JsonProcessingException e) {
                log.warn("Error writing response", ex);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }

}


/**
 * 保持和低版本请求路径不存在的行为一致
 * <p>
 * <a href="https://github.com/spring-projects/spring-boot/issues/38733">[Spring Boot
 * 3.2.0] 404 Not Found behavior #38733</a>
 */
@Slf4j
@RestControllerAdvice
class ReactiveNoResourceFoundHandler {


    @ExceptionHandler({NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R noResourceFoundException(NoResourceFoundException exception) {
        log.debug("请求路径 404 {}", exception.getMessage());
        return R.failed(exception.getMessage());
    }

}
