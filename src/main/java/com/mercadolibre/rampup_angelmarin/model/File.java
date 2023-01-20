package com.mercadolibre.rampup_angelmarin.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class File {
  private String nameFile;
  private String site;
  private List<String> domains;
  private List<String> priorities;
  private java.io.File contents;
}
