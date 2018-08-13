package org.visola.lifebooster.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

  @GetMapping("${api.base.path}/ping")
  public ResponseEntity<String> processLogin() {
    return ResponseEntity.ok("Pong");
  }

}
