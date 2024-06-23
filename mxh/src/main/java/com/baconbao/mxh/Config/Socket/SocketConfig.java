package com.baconbao.mxh.Config.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {
    @Lazy
    @Autowired
    private UserDetailsService userDetail;
    // configureMessageBroker: tao kho chua du lieu
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue"); // duong dan nhan kho chua
        config.setApplicationDestinationPrefixes("/app"); // Duong dan goi len Gui len kho chua
    }

    // registerStompEndpoints: dang ky su dung socket
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // StompEndpointRegistry: doi tuong dang ky
        registry.addEndpoint("/ws").withSockJS(); // import duong dan tren http. giong controll
    }

    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = SimpMessageHeaderAccessor.getAccessor(message,
                        StompHeaderAccessor.class);
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String username = accessor.getFirstNativeHeader("username");
                    if (username != null) {
                        // Thực hiện xác thực bằng username (có thể kiểm tra trong cơ sở dữ liệu)
                        UserDetails userDetails = userDetail.loadUserByUsername(username);
                        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                        accessor.setUser(authentication);
                    }
                }
                return message;
            }
        });
    }
}
