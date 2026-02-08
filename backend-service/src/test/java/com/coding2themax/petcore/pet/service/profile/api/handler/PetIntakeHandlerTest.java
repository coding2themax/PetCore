package com.coding2themax.petcore.pet.service.profile.api.handler;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Age;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.AgeUnit;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.IntakeType;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Pet;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.PetStatus;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Sex;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Size;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Species;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.dto.response.PetResponse;
import com.coding2themax.petcore.pet.service.profile.api.router.PetIntakeRouter;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class PetIntakeHandlerTest {

  @Mock
  private PetIntakeService petIntakeService;

  private WebTestClient webTestClient;

  private UUID expectedId;
  private Instant expectedCreatedAt;

  @BeforeEach
  void setUp() {
    PetIntakeHandler handler = new PetIntakeHandler(petIntakeService);
    PetIntakeRouter router = new PetIntakeRouter();
    webTestClient = WebTestClient.bindToRouterFunction(router.route(handler)).build();

    expectedId = UUID.randomUUID();
    expectedCreatedAt = Instant.now();
  }

  private PetIntakeRequest petIntakeRequest = new PetIntakeRequest(
      "Buddy",
      Species.DOG,
      "Labrador",
      Sex.MALE,
      new Age(3, AgeUnit.YEARS),
      Size.LARGE,
      LocalDate.now(),
      IntakeType.OWNER_SURRENDER,
      PetStatus.AVAILABLE,
      "EXT-123");

  @Test
  void testHandleIntake() {
    // Given: service returns a created pet
    Pet mockPet = new Pet();
    mockPet.setId(expectedId);
    mockPet.setName("Buddy");
    mockPet.setSpecies(Species.DOG);
    mockPet.setBreed("Labrador");
    mockPet.setStatus(PetStatus.AVAILABLE);
    mockPet.setCreatedAt(expectedCreatedAt);

    PetResponse petResponse = new PetResponse(
        mockPet.getId(),
        mockPet.getName(),
        mockPet.getSpecies().toString(),
        mockPet.getBreed(),
        mockPet.getStatus().toString(),
        mockPet.getCreatedAt());

    BDDMockito.when(petIntakeService.createPetProfile(any(PetIntakeRequest.class)))
        .thenReturn(Mono.just(petResponse));

    // When: POST request to intake endpoint
    // Then: returns 200 OK with PetResponse body
    webTestClient.post()
        .uri("/api/v1/pets/intake")
        .bodyValue(petIntakeRequest)
        .exchange()
        .expectStatus().isOk()
        .expectBody(PetResponse.class)
        .value(petResponseDTO -> {
          assertThat(petResponseDTO.petId()).isEqualTo(expectedId);
          assertThat(petResponseDTO.name()).isEqualTo("Buddy");
          assertThat(petResponseDTO.species()).isEqualTo("DOG");
          assertThat(petResponseDTO.breed()).isEqualTo("Labrador");
          assertThat(petResponseDTO.status()).isEqualTo("AVAILABLE");
          assertThat(petResponseDTO.createdAt()).isEqualTo(expectedCreatedAt);
        });

    // Verify service was called
    verify(petIntakeService).createPetProfile(any(PetIntakeRequest.class));
  }
}
