package org.olf.swagger;

import groovy.json.JsonOutput;
import groovy.json.JsonSlurper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Swagger UI Controller
 */
@Api(value = "/licenses", tags = ["Swagger UI"])
public class SwaggerUIController {
    SwaggerApiService swaggerApiService;

    /**
     * Generates the api documentation, this is from a service we have written as the grails plugin does not work with JDK 17,
     * so we are only looking at the annotations hence its name
     * @return The api document in json form
     */
    @ApiOperation(
        value = "API documentation for the application",
        nickname = "swagger/api",
        httpMethod = "GET"
    )
    @ApiResponses([
        @ApiResponse(code = 200, message = "Success")
    ])
    public def api() {
        // Generate the API documentation
        Map swaggerApiDoc = swaggerApiService.generateSwaggerApiDoc();

        // This header is required if we are coming through okapi
        header("Access-Control-Allow-Origin", request.getHeader('Origin'));

        // We should now just have the calls we are interested in
        render(status: 200, contentType: "application/json", text: JsonOutput.toJson(swaggerApiDoc));
    }
}

