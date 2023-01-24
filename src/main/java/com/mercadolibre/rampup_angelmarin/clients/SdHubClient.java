package com.mercadolibre.rampup_angelmarin.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.rampup_angelmarin.dtos.ValidatorAPI;
import java.util.Arrays;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SdHubClient {

  public RestTemplate restTemplate;

  public ObjectMapper objectMapper;
  public static String sdHub =
      "https://internal-api.mercadolibre.com/sd-hub/fashion-validator-plugin/fashion-size-consistency/configurations/validator_config?env=%s";

  public static String sdHubDomain =
      "https://internal-api.mercadolibre.com/sd-hub/domains/%s/configurations/size_by_site_map?env=%s";

  // web-test

  public ValidatorAPI getConfig(String scope) {
    restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<ValidatorAPI> requestEntity = new HttpEntity<>(headers);
    ResponseEntity<ValidatorAPI> r =
        restTemplate.exchange(
            String.format(sdHub, scope), HttpMethod.GET, requestEntity, ValidatorAPI.class);
    return r.getBody();
  }

  public ValidatorAPI postConfig(String scope, ValidatorAPI update, String xClientId) {
    restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    // TODO llevar a parametro del servicio
    headers.add("X-Client-Id", xClientId);
    // headers.add("X-Auth-Token",
    // "ad6824d50e1a23d5f12debc5829759a9bed608726f52784e6bdd8847beca14c9");

    HttpEntity<ValidatorAPI> requestEntity = new HttpEntity<>(update, headers);
    ResponseEntity<ValidatorAPI> r =
        restTemplate.exchange(
            String.format(sdHub, scope), HttpMethod.POST, requestEntity, ValidatorAPI.class);
    return r.getBody();
  }

  public Object postDomainConfig(String scope, String domain, String update, String xClientId) {
    restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);

    // TODO llevar a parametro del servicio
    headers.add("X-Client-Id", xClientId);
    // 2579503448603610
    HttpEntity<String> requestEntity = new HttpEntity<>(update, headers);
    ResponseEntity<String> r =
        restTemplate.exchange(
            String.format(sdHubDomain, domain, scope),
            HttpMethod.POST,
            requestEntity,
            String.class);
    return r.getBody();
  }
}
