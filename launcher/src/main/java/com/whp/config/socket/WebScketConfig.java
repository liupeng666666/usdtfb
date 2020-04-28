package com.whp.config.socket;

import com.whp.usdtfb.socket.utils.WebSocketUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author : 张吉伟
 * @data : 2018/7/25 15:21
 * @descrpition :
 */
@Configuration
@EnableWebSocket
public class WebScketConfig implements WebSocketConfigurer {

    @Bean
    public WebSocketInterceptor userInterceptor() {
        return new WebSocketInterceptor();
    }

    @Bean
    public WebSocketUtils MyInterceptor() {
        return new WebSocketUtils();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(MyInterceptor(), "/FbSocket/{ID}").setAllowedOrigins("*").addInterceptors(userInterceptor());
    }
}
