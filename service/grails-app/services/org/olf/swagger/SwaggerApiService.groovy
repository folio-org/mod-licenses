package org.olf.swagger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


public class SwaggerApiService {

    def grailsApplication;

    /** The mapping between HTTP status codes and the default text that is returned */
    static private final Map httpCodeMessage = [
        100 : "Continue",
        101 : "Switching Protocols",
        102 : "Processing",
        200 : "OK",
        201 : "Created",
        202 : "Accepted",
        204 : "No Content",
        400 : "Bad Request",
        404 : "Not Found",
        500 : "Internal Server Error"
    ];

    /** The X_Okapi-Tenant header that is be added as a parameter to all API calls */
    private static final Map parameterOkapiTenant = [
        name : "X-Okapi-Tenant",
        in : "header",
        description : "The tenant the request is for",
        required : true,
        type : "string",
        default : "test1"
    ];

    // /** The X-OKAPI-TOKEN header that is be added as a parameter to all API calls */
    // private static final Map parameterOkapiToken = [
    //     name : "X-OKAPI-TOKEN",
    //     in : "header",
    //     description : "The authentication token to use with this request",
    //     required : false,
    //     type : "string"
    // ];

    // /** The X-Okapi-User-Id header that is be added as a parameter to all API calls */
    // private static final Map parameterOkapiUserId = [
    //     name : "X-Okapi-User-Id",
    //     in : "header",
    //     description : "The user id to use with this request",
    //     required : false,
    //     type : "string"
    // ];

    /**
     * Generates the API Swagger document, we should probably allow the Security definitions be passed through
     * @return A map containing the API document
     */
    public Map generateSwaggerApiDoc() {
        // Define the non auto generated stuff ...
        Map api = [
            swagger : "2.0",
            security : [
                [
                    "apiKey" : []
                ]
            ],
            "securityDefinitions" : [
                "apiKey" : [
                    "type" : "apiKey",
                    "name" : "apiKey",
                    "in" : "header"
                ]
            ]
        ];

        // Now lets move onto generating the paths
        Map paths = new TreeMap();
        api.paths = paths;

        // The annotations we will be looking for
        Class annotationSwaggerApiClass = Api.class;
        Class annotationSwaggerImplicitParamClass = ApiImplicitParam.class;
        Class annotationSwaggerImplicitParamsClass = ApiImplicitParams.class;
        Class annotationSwaggerOperationClass = ApiOperation.class;
        Class annotationSwaggerResponseClass = ApiResponse.class;
        Class annotationSwaggerResponsesClass = ApiResponses.class;

        // Loop through all the controller classes
        grailsApplication.controllerClasses.each { controllerArtefact ->
            Class controllerClass = controllerArtefact.getClazz();

            // Does the class have the Api annotation, if not we will ignore it
            Api annotationSwaggerApi = controllerClass.getAnnotation(annotationSwaggerApiClass);
            if (annotationSwaggerApi) {
                // Grab hold of what we are interested in from the Api annotation
                String apiBasePath = annotationSwaggerApi.value();
                String[] apiTags = annotationSwaggerApi.tags();

                // Now we have the api details lets us deal with the methods
                for (Method method : controllerClass.getMethods()) {
                    // Only interested in public methods
                    if (Modifier.isPublic(method.getModifiers())) {
                        // Only interested in methods that have a swagger Operation annotation
                        ApiOperation annotationSwaggerOperation = method.getAnnotation(annotationSwaggerOperationClass);
                        if (annotationSwaggerOperation) {
                            // This is an Operation we are interested in
                            String path = buildPath(apiBasePath, annotationSwaggerOperation.nickname());

                            // Does this path already exist
                            Map pathDetails = paths[path];
                            if (pathDetails == null) {
                                // No it dosn't so create a new Map, we use a TreeMap for ordering purposes
                                pathDetails = new TreeMap();
                                paths[path] = pathDetails;
                            }

                            // Now deal with the method
                            String httpMethod =  annotationSwaggerOperation.httpMethod();

                            // Ensure we have a http method
                            if (!httpMethod) {
                                httpMethod = "GET";
                            }
                            Map httpMethodDetails = [ : ];
                            pathDetails[httpMethod.toLowerCase()] = httpMethodDetails;

                            // Now we can fill out the operation details
                            httpMethodDetails.tags = apiTags;
                            httpMethodDetails.summary = annotationSwaggerOperation.value();
                            httpMethodDetails.operationId = annotationSwaggerOperation.nickname();
                            if (annotationSwaggerOperation.produces()) {
                                httpMethodDetails.produces = [ annotationSwaggerOperation.produces() ];
                            }

                            // Now we deal with the parameters
                            List parameters = [ ];
                            httpMethodDetails.parameters = parameters;

                            // Deal with the parameters defined against the method
                            addParameters(parameters, (ApiImplicitParams)method.getAnnotation(annotationSwaggerImplicitParamsClass));
                            addParameter(parameters, (ApiImplicitParam)method.getAnnotation(annotationSwaggerImplicitParamClass));

                            // Add the default parameters
                            parameters.add(parameterOkapiTenant);
                            // parameters.add(parameterOkapiUserId);
                            // parameters.add(parameterOkapiToken);

                            // And now for the responses
                            Map responses = [ : ];
                            httpMethodDetails.responses = responses;
                            addResponses(responses, (ApiResponses)method.getAnnotation(annotationSwaggerResponsesClass));
                            addResponse(responses, (ApiResponse)method.getAnnotation(annotationSwaggerResponseClass));
                        }
                    }
                }
            }
        }

        // Finally return the Api document to the caller
        return(api);
    }

    /**
     * Builds the path that is to be used for the call
     * @param basePath The base path as defined by the API annotation
     * @param secondaryPath The path defined by the Operation annotation
     * @return The path to be used for the call
     */
    public String buildPath(String basePath, String secondaryPath) {
        StringBuffer path = new StringBuffer(basePath == null ? "" : basePath);
        if (secondaryPath != null) {
            // When we combine the paths we need to ensure we do not have 2 forward slashes and just the one
            if (basePath.endsWith('/')) {
                if (secondaryPath.startsWith('/')) {
                    path.append(secondaryPath.substring(1));
                } else {
                    path.append(secondaryPath);
                }
            } else if (secondaryPath != '/') {
                if (!secondaryPath.startsWith('/')) {
                    path.append('/');
                }
                path.append(secondaryPath);
            }
        }

        // Return the concatenated path to the caller
        return(path.toString());
    }

    /**
     * Process the ImplicitParams annotation
     * @param parameters The list of parameters we need to add the ImplicitParams annotation to
     * @param apiImplicitParams The ImplicitParams annotation that needs to be interpreted
     */
    private void addParameters(List parameters, ApiImplicitParams apiImplicitParams) {
        // Have we been been supplied some parameters
        if (apiImplicitParams && apiImplicitParams.value()) {
            // We have so process each of them
            apiImplicitParams.value().each { ApiImplicitParam apiImplicitParam ->
                addParameter(parameters, apiImplicitParam);
            }
        }
    }

    /**
     * Adds a parameter to the list of parameters that are known from the ImplicitParam annotation
     * @param parameters The list of parameters for the current api call being processed
     * @param apiImplicitParam The annotation that needs to be interpreted
     */
    private void addParameter(List parameters, ApiImplicitParam apiImplicitParam) {
        // Have we been been supplied a parameter
        if (apiImplicitParam) {
            // We have so build the parameter
            Map parameter = [ : ];
            parameters.add(parameter);
            parameter.in = apiImplicitParam.paramType();

            // Is this parameter contained within the body, if so we need to define the schema
            if (apiImplicitParam.paramType() == "body") {
                parameter.name = "body";
                parameter.schema = [
                    type : apiImplicitParam.dataType()
                ];
                if (apiImplicitParam.defaultValue()) {
                    parameter.schema.default = apiImplicitParam.defaultValue();
                }
            } else {
                // Just standard parameter
                parameter.name = apiImplicitParam.name();

                // Can it have multiple values
                if (apiImplicitParam.allowMultiple()) {
                    // It does, so we need to define it as an array
                    parameter.type = "array";
                    parameter.items = [
                        type : apiImplicitParam.dataType()
                    ];
                    parameter.collectionFormat = "multi";
                } else {
                    parameter.type = apiImplicitParam.dataType();
                }

                // Do we have a default value
                if (apiImplicitParam.defaultValue()) {
                    // We do, so we need to convert the default value into the appropriate type
                    if (apiImplicitParam.dataType() == "boolean") {
                        try {
                            parameter.default = apiImplicitParam.defaultValue().toBoolean();
                        } catch(Exception e) {
                            log.error("Excption thrown trying to convert string " + apiImplicitParam.defaultValue() + " to a boolean", e);
                        }
                    } else if (apiImplicitParam.dataType() == "int") {
                        try {
                            parameter.default = apiImplicitParam.defaultValue().toInteger();
                        } catch(Exception e) {
                            log.error("Excption thrown trying to convert string " + apiImplicitParam.defaultValue() + " to a integer", e);
                        }
                    } else {
                        setMapStringValue(parameter, "default", apiImplicitParam.defaultValue());
                    }
                }

                // Do we have a restricted set of values
                if (apiImplicitParam.allowableValues()) {
                    // Need to split the values into an array
                    parameter.enum = apiImplicitParam.allowableValues().split(',');
                }
            }

            // Values that should apply to all types of parameters
            parameter.description = apiImplicitParam.value();
            setMapBooleanValue(parameter, "required", apiImplicitParam.required());
            setMapBooleanValue(parameter, "allowEmptyValue", apiImplicitParam.allowEmptyValue());
            setMapStringValue(parameter, "example", apiImplicitParam.example());
        }
    }

    /**
     * Add the possible Responses to the map of responses
     * @param responses The map of responses for the call
     * @param apiResponses The Responses annotation that contains the list of possible responses
     */
    private void addResponses(Map responses, ApiResponses apiResponses) {
        // Have we been been supplied some parameters
        if (apiResponses && apiResponses.value()) {
            // We have so process each of them
            apiResponses.value().each { ApiResponse apiResponse ->
                addResponse(responses, apiResponse);
            }
        }
    }

    /**
     * Adds a response to the map of possible responses for a call
     * @param responses The map of responses for the call
     * @param apiResponse The Response annotation that needs to be added
     */
    private void addResponse(Map responses, ApiResponse apiResponse) {
        // Have we been been supplied a response
        if (apiResponse) {
            // We have so build the response
            Map response = [ : ];
            responses[apiResponse.code()]= response;
            response.description = getResponseDescription(apiResponse.code(), apiResponse.message());
        }
    }

    /**
     * Adds a value to a map if the value is not null and is not empty
     * @param map The map that the value is to be added to
     * @param key The key that the value should be added with
     * @param value The value that is to be added
     */
    private void setMapStringValue(Map map, String key, String value) {
        // Set the key if there is a value
        if (value) {
            map[key] = value;
        }
    }

    /**
     * Adds a boolean value to a map, if its value is true
     * @param map The map that the value is to be added to
     * @param key The key in which to add the value to the map with
     * @param value The value to be added to the map if it is true
     */
    private void setMapBooleanValue(Map map, String key, boolean value) {
        // Set the key if the value is true
        if (value) {
            map[key] = value;
        }
    }

    /**
     * Obtain the response description for a response code
     * @param code The code that a description is needed for
     * @param message The message supplied for this code from the annotation
     * @return
     */
    private String getResponseDescription(int code, String message) {
        String description = message;

        // If we were supplied with a message from the annotation there is nothing for us to do
        if (!description) {
            description = httpCodeMessage.get(code);
            if (!description) {
                // We do not have a default for this code, so rhwy should supply one in the annotation or it needs adding to the list of known ones
                description = "Unknown";
            }
        }
        return(description);
    }
}

