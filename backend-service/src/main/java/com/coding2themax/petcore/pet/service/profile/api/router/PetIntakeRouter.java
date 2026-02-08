package com.coding2themax.petcore.pet.service.profile.api.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.coding2themax.petcore.pet.service.profile.api.handler.PetIntakeHandler;

@Configuration
@EnableR2dbcRepositories
public class PetIntakeRouter {

  public static final String PET_INTAKE_BASE_URL = "/api/v1/pets/intake";

  @Bean
  public RouterFunction<ServerResponse> route(PetIntakeHandler handler) {
    return RouterFunctions.route().path(PET_INTAKE_BASE_URL,
        b -> b.GET(RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::handleGetPets)
            .POST(handler::handleIntake))
        .build();
  }

}