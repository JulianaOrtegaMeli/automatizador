package com.mercadolibre.rampup_angelmarin.services;

import com.mercadolibre.fashion.validator.plugin.dto.FileInputDTO;
import com.mercadolibre.fashion.validator.plugin.dto.PrioritiesEnum;
import com.mercadolibre.fashion.validator.plugin.util.GeneratorJsonUtils;
import com.mercadolibre.rampup_angelmarin.clients.SdHubClient;
import com.mercadolibre.rampup_angelmarin.dtos.ValidatorAPI;
import com.mercadolibre.rampup_angelmarin.model.File;
import com.mercadolibre.rampup_angelmarin.util.FileUtility;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ProcessService {

  private FileUtility fileUtility;

  private SdHubClient sdHubClient;

  public ProcessService(FileUtility fileUtility, SdHubClient sdHubClient) {
    this.fileUtility = fileUtility;
    this.sdHubClient = sdHubClient;
  }

  public ResponseEntity<?> generate(
      String scope,
      MultipartFile fileConfigGender,
      MultipartFile fileConfigAge,
      String workDirectory) {

    java.io.File[] files = fileUtility.getDirectoryFiles(workDirectory);

    Arrays.stream(files)
        .sequential()
        .forEach(
            fileToRead -> {
              if (fileToRead.isDirectory()) {
                return;
              }

              File file = getFileToGenerate(fileToRead);

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

                String txtStart =
                    "{\n"
                        + "    \"configuration\": {\n"
                        + "        \"conditions\": [],\n"
                        + "        \"default_response\": {\n"
                        + "            \"value_map\": ";
                String txtFinal = "    \n}\n" + "    }\n" + "}";

                String output =
                    String.format(
                        "%s%s%s",
                        txtStart, new GeneratorJsonUtils(input).generateJsonFile(), txtFinal);

                createFileOut(workDirectory, input, output, file);
                //  return ResponseEntity.ok(output);
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });

    return null;
  }

  private void createFileOut(String workDirectory, FileInputDTO input, String outJson, File file) {

    String pathSalida = workDirectory + "/salida/";
    java.io.File folder = new java.io.File(pathSalida);

    if (!folder.exists()) {
      folder.mkdir();
    }

    for (String domain : file.getDomains()) {
      String nameFileOut = String.format("%s|%s|%s", file.getSite(), domain, "JSONGENERADO.json");
      try (PrintWriter out =
          new PrintWriter(
              new OutputStreamWriter(
                  Files.newOutputStream(Paths.get(String.format("%s%s", pathSalida, nameFileOut))),
                  StandardCharsets.UTF_8))) {
        if (outJson != null) {
          out.write(outJson);
        }
      } catch (Exception e) {
        log.error("ERROR GENERANDO SALIDA>>>>VER LOG" + e.getMessage());
      }
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

  private File getFileToGenerate(java.io.File contents) {

    if (!contents.getName().substring(contents.getName().lastIndexOf(".") + 1).equals("xlsx")) {
      throw new IllegalArgumentException(
          String.format("El archivo \"%s\" debe ser extension .xlsx", contents.getName()));
    }

    String[] fileCompose = contents.getName().replace(".xlsx", "").toString().split("\\|");

    if (fileCompose.length != 3) {
      throw new IllegalArgumentException(
          String.format(
              "El nombre \"%s\" del file no es valido ejemplo MLA_PANT1-SNEKEARS_P0-P1.xlsx asegurate que este bien nombrado el file",
              contents.getName()));
    }

    File file =
        File.builder()
            .nameFile(contents.getName())
            .site(fileCompose[0])
            .domains(Arrays.asList(fileCompose[1].split("-")))
            .priorities(Arrays.asList(fileCompose[2].split("-")))
            .contents(contents)
            .build();

    validateFile(file);

    return file;
  }

  private void validateFile(File file) {

    List<String> listWhite =
        Arrays.asList(
            "SNEAKERS",
            "BOOTS_AND_BOOTIES",
            "LOAFERS_AND_OXFORDS",
            "FOOTBALL_SHOES",
            "SANDALS_AND_CLOGS",
            "PANTS",
            "T_SHIRTS",
            "JACKETS_AND_COATS",
            "DRESSES",
            "SWEATSHIRTS_AND_HOODIES",
            "SHORTS",
            "BRAS",
            "BLOUSES",
            "SHIRTS",
            "LEGGINGS",
            "UNDERPANTS",
            "PANTIES",
            "SOCKS",
            "PAJAMAS",
            "TESTDOMAIN"
);

    if (file.getDomains().stream()
        .anyMatch(domain -> listWhite.stream().noneMatch(list -> list.equals(domain)))) {
      throw new IllegalArgumentException(
          String.format(
              "Existe algun dominio configurado en el nombre %s que no es valido para el automatizador, dominios permitidos-> %s",
              file.getNameFile(), listWhite));
    }
  }

  public ResponseEntity<?> uploadFiles(String scope, String workDirectory, String xClientId) {

    java.io.File[] files = fileUtility.getDirectoryFiles(workDirectory);

    Arrays.stream(files)
        .sequential()
        .forEach(
            fileToRead -> {
              if (fileToRead.isDirectory()) {
                return;
              }

              if (fileToRead.getName().endsWith("_PROCESADO")) {
                return;
              }

              if (fileToRead.getName().endsWith("_ERROR")) {
                return;
              }

              File file = getFileToUploadServer(fileToRead);
              try {
                ValidatorAPI apiResponseConfig = sdHubClient.getConfig(scope);
                AtomicBoolean updateConfig = new AtomicBoolean(false);

                file.getDomains().stream()
                    .forEach(
                        domainToLoad -> {
                          String newDomain =
                              String.format(
                                  "%s-%s", file.getSite().toUpperCase(Locale.ROOT), domainToLoad);

                          if (apiResponseConfig
                              .getConfiguration()
                              .getDefaultResponse()
                              .getEnabledDomains()
                              .stream()
                              .noneMatch(domain -> domain.equals(newDomain))) {
                            apiResponseConfig
                                .getConfiguration()
                                .getDefaultResponse()
                                .getEnabledDomains()
                                .add(newDomain);
                            updateConfig.set(true);
                          }
                        });

                if (updateConfig.get()) {
                  sdHubClient.postConfig(scope, apiResponseConfig, xClientId);
                }

                file.getDomains().stream()
                    .forEach(
                        domainToLoad -> {
                          String newDomain =
                              String.format(
                                  "%s-%s", file.getSite().toUpperCase(Locale.ROOT), domainToLoad);

                          String textSystem = "";
                          try (FileReader reader = new FileReader(file.getContents())) {
                            int c;
                            while ((c = reader.read()) != -1) {
                              textSystem += (char) c;
                            }
                          } catch (IOException e) {
                            // TODO ATRAPAR EL ERROR DE PROCESAMIENTO DE FILE
                            throw new RuntimeException(e);
                          }

                          sdHubClient.postDomainConfig(scope, newDomain, textSystem, xClientId);
                          DateTimeFormatter formatter =
                              DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                          java.io.File newFile =
                              new java.io.File(
                                  file.getContents().getParent(),
                                  String.format(
                                      "%s_%s_%s_%s",
                                      file.getContents().getName(),
                                      scope,
                                      LocalDateTime.now().format(formatter).replace(" ", "T"),
                                      "PROCESADO"));
                          file.getContents().renameTo(newFile);
                        });

              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });

    return null;
  }

  private File getFileToUploadServer(java.io.File contents) {

    if (!contents.getName().substring(contents.getName().lastIndexOf(".") + 1).equals("json")) {
      throw new IllegalArgumentException(
          String.format("El archivo \"%s\" debe ser extension .json", contents.getName()));
    }

    String[] fileCompose = contents.getName().replace("|JSONGENERADO.json", "").split("\\|");
    if (fileCompose.length != 2) {
      throw new IllegalArgumentException(
          String.format(
              "El nombre \"%s\" del file no es valido: ejemplo MLA|SNEAKERS|JSONGENERADO.json asegurate que este bien nombrado el file",
              contents.getName()));
    }

    File file =
        File.builder()
            .nameFile(contents.getName())
            .site(fileCompose[0])
            .domains(Arrays.asList(fileCompose[1].split("-")))
            .contents(contents)
            .build();

    validateFile(file);

    return file;
  }
}
