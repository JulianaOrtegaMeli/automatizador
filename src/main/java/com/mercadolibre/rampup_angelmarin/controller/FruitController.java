package com.mercadolibre.rampup_angelmarin.controller;

import com.mercadolibre.rampup_angelmarin.dtos.FruitRequestDTO;
import com.mercadolibre.rampup_angelmarin.dtos.FruitResponseDTO;
import com.mercadolibre.rampup_angelmarin.dtos.ResponseDTO;
import com.mercadolibre.rampup_angelmarin.services.FruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class FruitController {

    @Autowired
    FruitService service;

    @PostMapping(value = "/create")
    public ResponseEntity<FruitResponseDTO> createFruit(@RequestHeader("owner") String owner, @Valid @RequestBody FruitRequestDTO request) throws Exception {
        {
            FruitResponseDTO fruit = service.create(owner, request);
            return new ResponseEntity(fruit, HttpStatus.CREATED);
        }
    }


    @GetMapping(value = "/search")
    public ResponseEntity<FruitResponseDTO> searchFruit(@RequestParam("name") String name, @RequestParam("status") String status,
                                                        @RequestParam("limit") Integer limit, @RequestParam("offset") Integer offset) throws Exception {
        {
            ResponseDTO fruta = service.searchFruit(name, status, limit, offset);

            return new ResponseEntity(fruta, HttpStatus.OK);
        }
    }
}
