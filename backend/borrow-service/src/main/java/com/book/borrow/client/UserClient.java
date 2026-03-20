package com.book.borrow.client;

import com.book.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * User Service Feign Client
 */
@FeignClient(name = "user-service", path = "/api/user")
public interface UserClient {

    @GetMapping("/{id}")
    Result<UserInfo> getUserById(@PathVariable("id") Long id);
}
