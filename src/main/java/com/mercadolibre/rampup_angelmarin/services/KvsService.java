package com.mercadolibre.rampup_angelmarin.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.kvsclient.ContainerKvsLowLevelClient;
import com.mercadolibre.kvsclient.exceptions.KvsException;
import com.mercadolibre.kvsclient.item.Item;
import com.mercadolibre.kvsclient.kvsapi.KvsapiConfiguration;
import com.mercadolibre.kvsclient.kvsapi.KvsapiLowLevelClient;
import com.mercadolibre.rampup_angelmarin.dtos.FruitResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KvsService {

    @Autowired
    private ObjectMapper objectMapper;

    private ContainerKvsLowLevelClient kvs;
    String containerName = System.getenv("KEY_VALUE_STORE_FRUITS_KVS_CONTAINER_NAME");

    public void registrarDatoKvs(FruitResponseDTO response) {

        KvsapiConfiguration config = KvsapiConfiguration.builder()
                .withSocketTimeout(150)
                .withMaxWait(100)
                .withConnectionTimeout(100)
                .withMaxConnections(30)
                .withMaxConnectionsPerRoute(30)
                 .withMaxRetries(1)
                .withRetryDelay(30)
                .build();

        kvs = new ContainerKvsLowLevelClient(new KvsapiLowLevelClient(config), containerName);
        try {
            String value = objectMapper.writeValueAsString(response);

            Item item = new Item();
            item.setKey(response.getUuid());
            item.setValue(value);
            item.setTtl(1011111);

            kvs.save(item);

        } catch (KvsException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
