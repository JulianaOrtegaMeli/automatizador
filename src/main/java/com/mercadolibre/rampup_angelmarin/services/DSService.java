package com.mercadolibre.rampup_angelmarin.services;

import com.mercadolibre.ds.common.types.FieldType;
import com.mercadolibre.ds.common.types.SortOrder;
import com.mercadolibre.dsclient.DsClient;
import com.mercadolibre.dsclient.config.DsClientConfiguration;
import com.mercadolibre.dsclient.exception.DsClientException;
import com.mercadolibre.dsclient.impl.EntityDsClient;
import com.mercadolibre.dsclient.response.search.SearchResponse;
import com.mercadolibre.dsclient.search.QueryBuilders;
import com.mercadolibre.dsclient.search.SortBuilders;
import com.mercadolibre.dsclient.search.builders.query.QueryBuilder;
import com.mercadolibre.rampup_angelmarin.dtos.FruitResponseDTO;
import com.mercadolibre.rampup_angelmarin.dtos.PaggingDTO;
import com.mercadolibre.rampup_angelmarin.dtos.ResponseDTO;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class DSService {

    String nameDS = System.getenv("DOCUMENT_SEARCH_FRUITS_KVS_DS_NAMESPACE");

    public ResponseDTO search(String name, String status, Integer limit, Integer offset) throws DsClientException {
        DsClientConfiguration conf = DsClientConfiguration.builder()
                .withServiceName(nameDS)
                .build();

        DsClient dsClient = new EntityDsClient(conf);


        QueryBuilder queryBuilder =QueryBuilders.or(
                QueryBuilders.eq("name", "name"),
                QueryBuilders.eq("status", "status")
        );

        SearchResponse<FruitResponseDTO> response =  dsClient.searchBuilder()
                .addSort(SortBuilders.field("dateCreation", FieldType.DATE).order(SortOrder.ASC))
                .withQuery(queryBuilder)
                .withSize(limit)
                .execute(FruitResponseDTO.class);

        Long total = response.getTotal();

        List<FruitResponseDTO> fruitsDocuments = response.getDocuments();

        ResponseDTO responseDto = new ResponseDTO();
        PaggingDTO paggingDto = new PaggingDTO();
        paggingDto.setLimit(limit);
        paggingDto.setTotal(total!=null?total.intValue():0);
        paggingDto.setOffset(offset);

        responseDto.setPagging(paggingDto);
        responseDto.setFruitsResponse(fruitsDocuments);

        return responseDto;

    }
}
