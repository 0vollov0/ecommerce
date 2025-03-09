package portfolio.ecommerce.worker.response;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "BadRequest", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "BadRequest"))),
        @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Conflict"))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Internal Server Error")))
})
public @interface ApiErrorResponses { }
