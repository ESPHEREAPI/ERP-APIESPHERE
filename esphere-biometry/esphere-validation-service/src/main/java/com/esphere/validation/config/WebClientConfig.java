package com.esphere.validation.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Configuration
public class WebClientConfig {

//    @Bean
//    public WebClient adherentWebClient() {
//        HttpClient httpClient = HttpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//                .doOnConnected(conn -> conn
//                        .addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
//                        .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS)));
//
//        return WebClient.builder()
//                .baseUrl("http://35.204.126.17/web_service/public/biometry")
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .build();
//    }
    
   @Bean
public WebClient adherentWebClient() {
    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected(conn -> conn
                    .addHandlerLast(new ReadTimeoutHandler(60, TimeUnit.SECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(60, TimeUnit.SECONDS)));

    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configurer ->
                    configurer.defaultCodecs()
                            .maxInMemorySize(50 * 1024 * 1024))
            .build();

    return WebClient.builder()
            .baseUrl("http://35.204.126.17/web_service/public/biometry")
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(exchangeStrategies)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE) // ← Fix principal
            .build();
}
}