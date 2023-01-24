package com.mercadolibre.rampup_angelmarin.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mercadolibre.rampup_angelmarin.model.validator.Configuration;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ValidatorAPI implements Serializable, Cloneable {
    private static final long serialVersionUID = -5469387229372567034L;
    private Configuration configuration;

    public ValidatorAPI copy(ValidatorAPI objecto) {
       return ValidatorAPI.builder().configuration(objecto.getConfiguration()).build();
    }

}
