package com.coding2themax.petcore.pet.service.profile.api.router;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.coding2themax.petcore.pet.service.profile.api.dto.response.PetResponse;
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
        .uri("/api/v1/pets")
        .exchange()
        .expectStatus().is5xxServerError();

  }

  @Test
  void testGetPetById_found() {
    UUID id = UUID.randomUUID();
    Instant createdAt = Instant.now();
    PetResponse petResponse = new PetResponse(id, "Buddy", "DOG", "Golden Retriever", "AVAILABLE", createdAt);

    BDDMockito.given(handler.handleGetPetById(ArgumentMatchers.any()))
        .willReturn(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(petResponse));

    webTestClient.get()
        .uri("/api/v1/pets/" + id)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(PetResponse.class)
        .value(resp -> Assertions.assertAll(
            () -> Assertions.assertEquals(id, resp.petId()),
            () -> Assertions.assertEquals("Buddy", resp.name()),
            () -> Assertions.assertEquals("DOG", resp.species()),
            () -> Assertions.assertEquals("Golden Retriever", resp.breed()),
            () -> Assertions.assertEquals("AVAILABLE", resp.status()),
            () -> Assertions.assertEquals(createdAt, resp.createdAt())));
  }

  @Test
  void testGetPetById_notFound() {
    UUID id = UUID.randomUUID();

    BDDMockito.given(handler.handleGetPetById(ArgumentMatchers.any()))
        .willReturn(ServerResponse.notFound().build());

    webTestClient.get()
        .uri("/api/v1/pets/" + id)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isNotFound();
  }

  @Test
  @DisplayName("POST /api/v1/pets with missing body should return 400 Bad Request")
  void testHandleIntake_missingBody_returns4xx() {
    BDDMockito.given(handler.handleIntake(ArgumentMatchers.any()))
        .willReturn(ServerResponse.badRequest().build());

    webTestClient.post()
        .uri("/api/v1/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @DisplayName("POST /api/v1/pets with missing name should return 400 Bad Request")
  void testHandleIntake_missingName_returns400() {
    BDDMockito.given(handler.handleIntake(ArgumentMatchers.any()))
        .willReturn(ServerResponse.badRequest().build());

    webTestClient.post()
        .uri("/api/v1/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{\"species\":\"DOG\",\"breed\":\"Golden Retriever\",\"status\":\"AVAILABLE\"}")
        .exchange()
        .expectStatus().isBadRequest();
  }
}
