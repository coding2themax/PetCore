package com.coding2themax.petcore.pet.service.profile.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.coding2themax.petcore.pet.service.profile.api.handler.PetIntakeHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
@EnableR2dbcRepositories
public class PetIntakeRouter {

    public static final String PET_INTAKE_BASE_URL = "/api/v1/pets";

    @Bean
    @RouterOperations({
            @RouterOperation(path = PET_INTAKE_BASE_URL, method = RequestMethod.GET, beanClass = PetIntakeHandler.class, beanMethod = "handleGetPets", operation = @Operation(operationId = "getPets", summary = "Get all pets", description = "Retrieves all pets in the system", responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved pets", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            })),
            @RouterOperation(path = PET_INTAKE_BASE_URL, method = RequestMethod.POST, beanClass = PetIntakeHandler.class, beanMethod = "handleIntake", operation = @Operation(operationId = "intakePet", summary = "Intake a new pet", description = "Registers a new pet in the system", responses = {
                    @ApiResponse(responseCode = "201", description = "Pet successfully registered", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = "Invalid pet data")
            }))
    })
    public RouterFunction<ServerResponse> route(PetIntakeHandler handler) {
        return RouterFunctions.route()
                .path(PET_INTAKE_BASE_URL, b -> b
                        .GET("/{id}", accept(MediaType.APPLICATION_JSON), handler::handleGetPetById)
                        .GET(accept(MediaType.APPLICATION_JSON), handler::handleGetPets)
                        .POST(handler::handleIntake))

                .build();
    }

}