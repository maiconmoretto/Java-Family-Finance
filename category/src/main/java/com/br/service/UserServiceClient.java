package com.br.service;

import com.br.entity.UserDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service-client", url = "${spring.user-service.url}")
public interface UserServiceClient {

    @GetMapping("/user/{id}")
    UserDTO viewUser(@PathVariable int id);

}