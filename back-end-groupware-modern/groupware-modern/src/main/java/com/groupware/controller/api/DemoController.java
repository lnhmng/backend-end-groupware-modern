package com.groupware.controller.api;

import com.groupware.common.CommonResponse;
import com.groupware.exception.CommonException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/demo-controller")
@Hidden
public class DemoController {

  @GetMapping
  public CommonResponse<?> sayHello() {
    try {
      return CommonResponse.success("Hello from secured endpoint");
    }
    catch (Exception e){
      throw new  CommonException(e.getMessage());
    }
  }
}
