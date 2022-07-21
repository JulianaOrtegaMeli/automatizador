package com.mercadolibre.rampup_angelmarin.dtos;

import com.mercadolibre.rampup_angelmarin.entity.Fruit;

import java.util.List;

public class ResponseDTO {

    private List<FruitResponseDTO> fruitsResponse;

    private PaggingDTO pagging;

    public List<FruitResponseDTO> getFruitsResponse() {
        return fruitsResponse;
    }

    public void setFruitsResponse(List<FruitResponseDTO> fruitsResponse) {
        this.fruitsResponse = fruitsResponse;
    }

    public PaggingDTO getPagging() {
        return pagging;
    }

    public void setPagging(PaggingDTO pagging) {
        this.pagging = pagging;
    }
}