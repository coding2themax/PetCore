package com.coding2themax.petcore.pet.service.profile.api.router;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2themax.petcore.pet.service.profile.api.handler.PetIntakeHandler;

@WebFluxTest(PetIntakeRouter.class)
@AutoConfigureWebTestClient
public class PetIntakeRouterTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private PetIntakeHandler handler;

  @Test
  void testRoute500() {

    webTestClient.post()
        .uri("/api/v1/pets/intake")
        .exchange()
        .expectStatus().is5xxServerError();

  }
}
