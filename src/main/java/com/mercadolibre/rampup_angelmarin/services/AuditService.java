package com.mercadolibre.rampup_angelmarin.services;

import com.mercadolibre.auditclient.auditapi.AuditApiConfiguration;
import com.mercadolibre.auditclient.auditapi.AuditClient;
import com.mercadolibre.auditclient.auditapi.AuditRecord;
import com.mercadolibre.auditclient.exceptions.AuditException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service("defaultAuditsService")
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);
    public static final Integer TIMEOUT = 50;
    public static final Integer MAX_RETRIES = 2;

    public void createAudit(String uuid, Map<String, Object> data, List<String> tags){

      /*
        AuditRecord auditRecord;

        AuditApiConfiguration config = AuditApiConfiguration.builder()
                .withTimeout(TIMEOUT)
                .withRetries(MAX_RETRIES)
                .build();

        String config2 = AuditApiConfiguration.builder()
                .withTimeout(TIMEOUT)
                .withRetries(MAX_RETRIES)
                .build().getEndpoint("fruitsaudits", "rampup-angely-blanco");

        System.out.println("config2" + config2);

        AuditClient client = new AuditClient(config, "fruitsaudits");

        auditRecord = new AuditRecord("post", "prueba", "fruit");
        auditRecord.setResourceID(uuid);
        auditRecord.setCurrentData(data);
        auditRecord.setTags(tags);

        try {
            CompletableFuture<com.mercadolibre.restclient.Response> future = client.saveAuditAsync(auditRecord);
            future.thenAcceptAsync(response -> LOGGER.info("Save Audit Create Fruit", response)).exceptionally(ex -> {
                LOGGER.error("Error Save Audit Create Fruit", ex);
                return null;
            });
        } catch (AuditException e) {
            throw new RuntimeException(e);
        }


       */
    }
}
