package com.mercadolibre.rampup_angely_blanco.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FruitRequestDTO {

    @NotNull(message = "Nombre no puede estar vacío")
    @JsonProperty("name")
    private String name;

    @Min(value = 0, message = "El campo quantity no debe ser menor que cero")
    @JsonProperty("quantity")
    private Integer quantity;

    @Min(value = 0, message = "El campo price no debe ser menor que cero")
    @JsonProperty("price")
    private Float price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
