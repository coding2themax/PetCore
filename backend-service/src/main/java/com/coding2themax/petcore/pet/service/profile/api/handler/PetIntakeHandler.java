package com.coding2themax.petcore.pet.service.profile.api.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class PetIntakeHandler {

  public Mono<ServerResponse> handleIntake(ServerRequest request) {
    // Implementation for handling pet intake
    return ServerResponse.ok().build();
  }
}
