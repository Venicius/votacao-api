package br.com.sicredi.votacao.infra.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "br.com.sicredi.votacao.infra.adapters.out.client")
public class FeignConfig {}
