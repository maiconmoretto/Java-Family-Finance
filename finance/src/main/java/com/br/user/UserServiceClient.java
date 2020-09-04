package com.br.user;

import com.br.model.UserDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service-client", url = "${spring.user-service.url}")
public interface UserServiceClient {

    @GetMapping("/api/v1/user/{id}")
    UserDTO findById(@PathVariable int id);

}
