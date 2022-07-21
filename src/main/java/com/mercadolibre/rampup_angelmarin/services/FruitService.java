package com.mercadolibre.rampup_angelmarin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mercadolibre.credits.consumer.Collector;
import com.mercadolibre.dsclient.exception.DsClientException;
import com.mercadolibre.rampup_angelmarin.dtos.FruitRequestDTO;
import com.mercadolibre.rampup_angelmarin.dtos.FruitResponseDTO;
import com.mercadolibre.rampup_angelmarin.dtos.ResponseDTO;
import com.mercadolibre.rampup_angelmarin.metrics.MetricData;
import com.mercadolibre.rampup_angelmarin.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

import static com.mercadolibre.rampup_angelmarin.metrics.DatadogMetricName.CREATE_FRUIT;
import static com.mercadolibre.rampup_angelmarin.metrics.MetricData.EVENT_CREATE_FRUIT;

@Service
public class FruitService {


    public static final Logger LOGGER = LoggerFactory.getLogger(FruitService.class);
    @Autowired
    private KvsService kvsService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private DSService dsService;

    //private Collector metricCollector;

    public FruitResponseDTO create(String owner, FruitRequestDTO request) throws Exception {


        String res = Util.validateFruit(request.getName(), owner);
        String uuid = Util.generateUUID();
        FruitResponseDTO response = new FruitResponseDTO();
        response.setUuid(uuid);
        response.setName(request.getName());
        response.setQuantity(request.getQuantity());
        response.setPrice(request.getPrice());
        response.setDateCreation(new Date());
        response.setLastUpdate(new Date());
        response.setStatus("comestible");

        List<String> tags = Arrays.asList("save-audit-async");
        Map<String, Object> data = objectMapper.convertValue(response, Map.class);
        auditService.createAudit(response.getUuid(), data, tags);


        try {
            kvsService.registrarDatoKvs(response);
        } catch (
                Exception e
        ) {

            Map<String, Object> dataa = new HashMap<String, Object>();
            dataa.put("error", e.toString());
            auditService.createAudit(response.getUuid(), dataa, tags);

            throw e;
        }
        //Kvs save


        //metricCollector.incrementCounter(CREATE_FRUIT.getName(), MetricData.buildCreateFruitTags("MLC", EVENT_CREATE_FRUIT));

        return response;
    }


    public ResponseDTO searchFruit(String name, String status, Integer limit, Integer offset){
        ResponseDTO response = null;
        try {
            response = dsService.search(name, status, limit, offset);
        } catch (DsClientException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return response;
    }
}
