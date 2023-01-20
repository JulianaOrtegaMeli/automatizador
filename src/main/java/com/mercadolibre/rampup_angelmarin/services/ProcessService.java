package com.mercadolibre.rampup_angelmarin.services;

// import com.mercadolibre.validator.model.File;
import com.mercadolibre.fashion.validator.plugin.dto.FileInputDTO;
import com.mercadolibre.fashion.validator.plugin.dto.PrioritiesEnum;
import com.mercadolibre.fashion.validator.plugin.util.GeneratorJsonUtils;
import com.mercadolibre.rampup_angelmarin.model.File;
import com.mercadolibre.rampup_angelmarin.util.FileUtility;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ProcessService {

  private FileUtility fileUtility;

  public ProcessService(FileUtility fileUtility) {
    this.fileUtility = fileUtility;
  }

  public ResponseEntity<?> upload(
      String scope,
      MultipartFile fileConfigGender,
      MultipartFile fileConfigAge,
      String workDirectory) {

    java.io.File[] files = fileUtility.getDirectoryFiles(workDirectory);

    if (files.length == 0) {
      throw new IllegalArgumentException(
          String.format("No se encontraron files en el directorio de  trabajo %s ", workDirectory));
    }

    Arrays.stream(files)
        .sequential()
        .forEach(
            fileToRead -> {
              File file = getFile(fileToRead);

              List<PrioritiesEnum> prioritiesList = formatPriorities(file);
              try {
                FileInputDTO input =
                    new FileInputDTO(
                        fileToRead,
                        null,
                        prioritiesList,
                        file.getSite(),
                        multipartToFile(fileConfigGender),
                        multipartToFile(fileConfigAge));

                String output = new GeneratorJsonUtils(input).generateJsonFile();

                createFileOut(
                    workDirectory,
                    input);
                //  return ResponseEntity.ok(output);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });

    return null;
  }

  private void createFileOut(String pathOut, FileInputDTO input) {

    try (PrintWriter out =
        new PrintWriter(
            new OutputStreamWriter(
                Files.newOutputStream(
                    Paths.get(
                        pathOut
                            + "/"
                            + String.format(
                                "%s%s",
                                input.getFile().getName().replace(".xlsx", ""),
                                "_JSONGENERADO.json"))),
                StandardCharsets.UTF_8))) {
      if (pathOut != null) {
        out.write(pathOut);
      }
    } catch (Exception e) {
      log.error("ERROR GENERANDO SALIDA>>>>VER LOG" + e.getMessage());
    }
  }

  public java.io.File multipartToFile(MultipartFile multipart)
      throws IllegalStateException, IOException, IOException {
    java.io.File convFile = new java.io.File(multipart.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(multipart.getBytes());
    fos.close();
    return convFile;
  }

  public List<PrioritiesEnum> formatPriorities(File file) {
    List<PrioritiesEnum> prioritiesList = new ArrayList<>();
    if (!file.getPriorities().isEmpty()) {
      prioritiesList =
          file.getPriorities().stream().map(PrioritiesEnum::valueOf).collect(Collectors.toList());
    }
    return prioritiesList;
  }

  private File getFile(java.io.File contents) {

    if (!contents.getName().substring(contents.getName().lastIndexOf(".") + 1).equals("xlsx")) {
      throw new IllegalArgumentException("El archivo debe ser extension xlsx");
    }

    String[] fileCompose = contents.getName().replace(".xlsx", "").split("_");
    if (fileCompose.length != 3) {
      throw new IllegalArgumentException(
          "El nombre del file no es valido ejemplo MLA_TOP-SNEAKERS_P0-P1.xlsx");
    }

    return File.builder()
        .nameFile(contents.getName())
        .site(fileCompose[0])
        .domains(Arrays.asList(fileCompose[1].split("-")))
        .priorities(Arrays.asList(fileCompose[2].split("-")))
        .contents(contents)
        .build();
  }
}

// BOTTOMS - T2 - PANTS - MLA - REGLAS PARA MAPA DE TALLES.xlsx
