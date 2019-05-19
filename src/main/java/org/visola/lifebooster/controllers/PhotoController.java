package org.visola.lifebooster.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("${api.base.path}/photos")
@Controller
public class PhotoController {

  @PostMapping
  public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile files) {
    return null;
  }

}
