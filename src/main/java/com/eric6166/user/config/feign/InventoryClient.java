package com.eric6166.user.config.feign;

import com.eric6166.common.config.feign.FeignClientConfig;
import com.eric6166.common.exception.AppException;
import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.client.config.inventory-client.name}",
        url = "${feign.client.config.inventory-client.url}",
        configuration = FeignClientConfig.class)
public interface InventoryClient {

    @GetMapping("/product/test")
    String productTest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws AppException;


}
