package com.coding2themax.petcore.pet.service.profile.api.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import com.coding2themax.petcore.pet.service.profile.api.handler.validator.PetIntakeValidator;
import com.coding2themax.petcore.pet.service.profile.api.router.PetIntakeRouter;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class PetIntakeHandlerTest {

  @Mock
  private PetIntakeService petIntakeService;

  private PetIntakeValidator petIntakeValidator = new PetIntakeValidator();

  private WebTestClient webTestClient;

  private UUID expectedId;
  private Instant expectedCreatedAt;

  @BeforeEach
  void setUp() {
    PetIntakeHandler handler = new PetIntakeHandler(petIntakeService, petIntakeValidator);
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
      "EXT-123",
      null);

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
        .uri("/api/v1/pets")
        .header("X-Idempotency-Key", "test-idempotency-key")
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

  @Test
  void testHandleIntake_serviceError_returns5xx() {
    // Given: service throws an error
    BDDMockito.when(petIntakeService.createPetProfile(any(PetIntakeRequest.class)))
        .thenReturn(Mono.error(new RuntimeException("Database unavailable")));

    // When: POST request to intake endpoint
    // Then: returns 5xx Server Error
    webTestClient.post()
        .uri("/api/v1/pets")
        .header("X-Idempotency-Key", "test-idempotency-key")
        .bodyValue(petIntakeRequest)
        .exchange()
        .expectStatus().is5xxServerError();

    // Verify service was called
    verify(petIntakeService).createPetProfile(any(PetIntakeRequest.class));
  }

  @Test
  @DisplayName("POST /api/v1/pets without X-Idempotency-Key returns 400")
  void testHandleIntake_missingIdempotencyKey_returns400() {
    webTestClient.post()
        .uri("/api/v1/pets")
        .bodyValue(petIntakeRequest)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testHandleGetPetById_found() {
    // Given: service returns a PetResponse for the given id
    PetResponse petResponse = new PetResponse(
        expectedId,
        "Buddy",
        Species.DOG.name(),
        "Labrador",
        PetStatus.AVAILABLE.name(),
        expectedCreatedAt);

    BDDMockito.when(petIntakeService.getPetById(expectedId.toString()))
        .thenReturn(Mono.just(petResponse));

    // When: GET request to /api/v1/pets/{id}
    // Then: returns 200 OK with PetResponse body
    webTestClient.get()
        .uri("/api/v1/pets/" + expectedId)
        .exchange()
        .expectStatus().isOk()
        .expectBody(PetResponse.class)
        .value(resp -> {
          assertThat(resp.petId()).isEqualTo(expectedId);
          assertThat(resp.name()).isEqualTo("Buddy");
          assertThat(resp.species()).isEqualTo("DOG");
          assertThat(resp.breed()).isEqualTo("Labrador");
          assertThat(resp.status()).isEqualTo("AVAILABLE");
          assertThat(resp.createdAt()).isEqualTo(expectedCreatedAt);
        });

    verify(petIntakeService).getPetById(expectedId.toString());
  }

  @Test
  @DisplayName("POST /api/v1/pets with no body returns 400")
  void testHandleIntake_missingBody_returns400() {
    webTestClient.post()
        .uri("/api/v1/pets")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  @DisplayName("POST /api/v1/pets with missing name returns 400")
  void testHandleIntake_missingName_returns400() {
    PetIntakeRequest missingName = new PetIntakeRequest(
        null,
        Species.DOG,
        "Labrador",
        Sex.MALE,
        new Age(3, AgeUnit.YEARS),
        Size.LARGE,
        LocalDate.now(),
        IntakeType.OWNER_SURRENDER,
        PetStatus.AVAILABLE,
        "EXT-123",
        null);

    webTestClient.post()
        .uri("/api/v1/pets")
        .header("X-Idempotency-Key", "test-idempotency-key")
        .bodyValue(missingName)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void testHandleGetPetById_serviceError_returns5xx() {
    // Given: service throws an error
    BDDMockito.when(petIntakeService.getPetById(expectedId.toString()))
        .thenReturn(Mono.error(new RuntimeException("Database unavailable")));

    // When: GET request to /api/v1/pets/{id}
    // Then: returns 5xx Server Error
    webTestClient.get()
        .uri("/api/v1/pets/" + expectedId)
        .exchange()
        .expectStatus().is5xxServerError();

    verify(petIntakeService).getPetById(expectedId.toString());
  }

  @Test
  void testHandleGetPets_returnsPetList() {
    // Given: service returns two pets
    PetResponse pet1 = new PetResponse(expectedId, "Buddy", Species.DOG.name(), "Golden Retriever",
        PetStatus.AVAILABLE.name(), expectedCreatedAt);
    UUID id2 = UUID.randomUUID();
    PetResponse pet2 = new PetResponse(id2, "Whiskers", Species.CAT.name(), "Tabby",
        PetStatus.ADOPTED.name(), expectedCreatedAt);

    BDDMockito.when(petIntakeService.getAllPets(null, null))
        .thenReturn(Flux.just(pet1, pet2));

    // When: GET /api/v1/pets with no filters
    // Then: returns 200 OK with full list
    webTestClient.get()
        .uri("/api/v1/pets")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(PetResponse.class)
        .hasSize(2)
        .value(list -> {
          assertThat(list.get(0).petId()).isEqualTo(expectedId);
          assertThat(list.get(0).name()).isEqualTo("Buddy");
          assertThat(list.get(1).petId()).isEqualTo(id2);
          assertThat(list.get(1).name()).isEqualTo("Whiskers");
        });

    verify(petIntakeService).getAllPets(null, null);
  }

  @Test
  void testHandleGetPets_withSpeciesAndStatusFilter_passesParamsToService() {
    // Given: service returns one matching pet
    PetResponse pet = new PetResponse(expectedId, "Whiskers", Species.CAT.name(), "Tabby",
        PetStatus.FOSTER.name(), expectedCreatedAt);

    BDDMockito.when(petIntakeService.getAllPets("CAT", "FOSTER"))
        .thenReturn(Flux.just(pet));

    // When: GET /api/v1/pets?species=CAT&status=FOSTER
    // Then: returns 200 OK with only the matching pet
    webTestClient.get()
        .uri("/api/v1/pets?species=CAT&status=FOSTER")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(PetResponse.class)
        .hasSize(1)
        .value(list -> {
          assertThat(list.get(0).species()).isEqualTo("CAT");
          assertThat(list.get(0).status()).isEqualTo("FOSTER");
        });

    verify(petIntakeService).getAllPets("CAT", "FOSTER");
  }

  @Test
  void testHandleGetPets_empty_returnsEmptyList() {
    // Given: service returns no pets
    BDDMockito.when(petIntakeService.getAllPets(null, null))
        .thenReturn(Flux.empty());

    // When: GET /api/v1/pets with no filters
    // Then: returns 200 OK with empty list
    webTestClient.get()
        .uri("/api/v1/pets")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(PetResponse.class)
        .hasSize(0);

    verify(petIntakeService).getAllPets(null, null);
  }

  @Test
  void testHandleGetPets_serviceError_returns5xx() {
    // Given: service throws an error
    BDDMockito.when(petIntakeService.getAllPets(null, null))
        .thenReturn(Flux.error(new RuntimeException("Database unavailable")));

    // When: GET /api/v1/pets
    // Then: returns 5xx Server Error
    webTestClient.get()
        .uri("/api/v1/pets")
        .exchange()
        .expectStatus().is5xxServerError();

    verify(petIntakeService).getAllPets(null, null);
  }
}
