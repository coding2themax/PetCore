package com.coding2themax.petcore.pet.service.profile.api.handler;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;

import com.coding2themax.petcore.pet.service.profile.api.domain.model.Age;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.AgeUnit;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.IntakeType;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.PetStatus;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Sex;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Size;
import com.coding2themax.petcore.pet.service.profile.api.domain.model.Species;
import com.coding2themax.petcore.pet.service.profile.api.dto.request.PetIntakeRequest;
import com.coding2themax.petcore.pet.service.profile.api.service.PetIntakeService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class PetIntakeHandlerTest {

  @Mock
  private PetIntakeService petIntakeService;

  @InjectMocks
  private PetIntakeHandler handler;

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

  private MockServerRequest request = MockServerRequest.builder().body(Mono.just(petIntakeRequest));

  @Test
  void testHandleIntake() {
    StepVerifier.create(handler.handleIntake(request))
        .expectSubscription()
        .expectNextMatches(response -> {
          assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
          return true;
        })
        .expectComplete()
        .verify();
  }
}
