package com.mercadolibre.rampup_angelmarin.unit.beans;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mercadolibre.rampup_angelmarin.beans.RandomSampleBean;
import com.mercadolibre.rampup_angelmarin.dtos.SampleDTO;
import org.junit.jupiter.api.Test;

class RandomSampleBeanTest {

  @Test
  void randomPositiveTestOK() {
    RandomSampleBean randomSample = new RandomSampleBean();

    SampleDTO sample = randomSample.random();

    assertTrue(sample.getRandom() >= 0);
  }
}
