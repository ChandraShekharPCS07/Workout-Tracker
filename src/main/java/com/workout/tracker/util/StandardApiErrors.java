package com.workout.tracker.util;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Operation
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = "{\"code\":400,\"success\":false,\"message\":\"Validation failed\",\"errors\":[\"email is required\"],\"path\":\"/api/v1/users\"}"))),
        @ApiResponse(responseCode = "404", description = "Not Found",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = "{\"code\":404,\"success\":false,\"message\":\"User not found\",\"errors\":[],\"path\":\"/api/v1/users/1\"}"))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = "{\"code\":500,\"success\":false,\"message\":\"Unexpected error\",\"errors\":[],\"path\":\"/api/v1/users\"}")))
})
public @interface StandardApiErrors {
}

