package com.mercadolibre.rampup_angelmarin.controller;

import com.mercadolibre.rampup_angelmarin.services.ProcessService;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/processGDT")
public class ProcessGDT {

  private ProcessService process;

  public ProcessGDT(ProcessService process) {
    this.process = process;
  }

  @PostMapping(value = "/generateConfig", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadFile(
      @RequestParam("scope") String scope,
      @RequestParam("fileConfigGender") MultipartFile fileConfigGender,
      @RequestParam("fileConfigAge") MultipartFile fileConfigAge,
      @RequestParam("workDirectory") String workDirectory)
      throws IOException {
    return process.generate(scope, fileConfigGender, fileConfigAge, workDirectory);
  }

  @PostMapping(value = "/uploadConfigToServer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> uploadFile(
      @RequestParam("scope") String scope,
      @RequestParam("workDirectory") String workDirectory,
      @RequestParam("xClientId") String xClientId)
      throws IOException {
    return process.uploadFiles(scope, workDirectory, xClientId);
  }
}
