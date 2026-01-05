/**
 * Clase PlantillaFormularioResource.java 10 nov. 2025
 * Copyright 2025 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.infraestructure.rest;

import ec.gob.sri.api.catalogo.application.dto.*;
import ec.gob.sri.api.catalogo.application.service.PlantillaFormularioService;
import ec.gob.sri.api.catalogo.infraestructure.rest.constant.ErrorMessages;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Controlador REST para Plantilla Formulario
 *
 * @author SRI
 */
@Path("/plantilla-formulario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Plantilla Formulario", description = "Gestión de plantillas de formularios")
public class PlantillaFormularioResource {

    private final PlantillaFormularioService service;

    public PlantillaFormularioResource(PlantillaFormularioService service) {
        this.service = service;
    }

    @POST
    @Operation(summary = "Guardar un nuevo formulario", description = "Crea una nueva plantilla de formulario")
    @APIResponse(responseCode = "201", description = "Formulario creado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GuardarPlantillaFormularioResponse.class), examples = {
            @ExampleObject(name = "Ejemplo 201", value = """
                    {
                      "codigoPlantillaFormulario": 1,
                      "codigo": "FORM-ANEXO-001",
                      "nombre": "Formulario de Anexo",
                      "descripcion": "Formulario para registro de anexos",
                      "version": "1.0.0",
                      "estado": "Activo",
                      "elementos": [
                        {
                          "id": "page-d9a5480b-cbef-430f-a8a8-cdbfd29a9a25",
                          "tipo": "page",
                          "nombre": "Página Principal",
                          "dimension": 12,
                          "atributos": {
                            "contenidos": [],
                            "secciones": [
                              {
                                "id": "section-8b22b028-f722-4d4f-bf57-c419bed00122",
                                "tipo": "section",
                                "nombre": "Sección 1",
                                "dimension": 12,
                                "atributos": {
                                  "contenidos": [],
                                  "secciones": []
                                }
                              }
                            ]
                          }
                        }
                      ],
                      "fechaCreacion": "2025-11-10T10:30:00",
                      "fechaActualizacion": "2025-11-10T10:30:00"
                    }
                    """)
    }))
    @APIResponse(responseCode = "400", description = "Petición inválida (datos con formato o valores incorrectos)", content = @Content(mediaType = MediaType.APPLICATION_JSON, examples = {
            @ExampleObject(name = "Ejemplo 400", value = """
                    { "codigo": "ERR-400", "mensaje": "Ya existe un formulario con el código: FORM-ANEXO-001" }
                    """)
    }))
    @APIResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON, examples = {
            @ExampleObject(name = "Ejemplo 500", value = ErrorMessages.EXAMPLE_ERROR_500)
    }))

    public Uni<Response> guardar(@Valid GuardarPlantillaFormularioRequest request) {
        return service.guardar(request)
                .onItem()
                .transform(result -> Response.status(Response.Status.CREATED).entity(result).build())
                .onFailure(IllegalArgumentException.class)
                .recoverWithItem(err -> Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse("ERR-400", err.getMessage()))
                        .build())
                .onFailure()
                .recoverWithItem(err -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_500,
                                ErrorMessages.ERROR_INESPERADO))
                        .build());
    }

    @PUT
    @Path("/{codigoPlantillaFormulario}")
    @Operation(summary = "Actualizar un formulario existente", description = "Actualiza los datos de una plantilla de formulario existente")

    @APIResponse(responseCode = "200", description = "Formulario actualizado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GuardarPlantillaFormularioResponse.class)))
    @APIResponse(responseCode = "400", description = "Petición inválida", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @APIResponse(responseCode = "404", description = "Formulario no encontrado")
    @APIResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON))

    public Uni<Response> actualizar(
            @Parameter(description = "Código de plantilla formulario", required = true) @PathParam("codigoPlantillaFormulario") Long codigoPlantillaFormulario,
            @Valid ActualizarPlantillaFormularioRequest request) {
        return service.actualizar(request, codigoPlantillaFormulario)
                .onItem().transform(result -> Response.ok(result).build())
                .onFailure(IllegalArgumentException.class)
                .recoverWithItem(err -> {
                    if (err.getMessage().contains("No se encontró")) {
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_404,
                                        err.getMessage()))
                                .build();
                    }
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new ErrorResponse("ERR-400", err.getMessage()))
                            .build();
                })
                .onFailure()
                .recoverWithItem(err -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_500,
                                ErrorMessages.ERROR_INESPERADO))
                        .build());
    }

    @GET
    @Path("/{codigoPlantillaFormulario}")
    @Operation(summary = "Consultar un formulario por ID", description = "Obtiene los datos de un formulario específico por su identificador")
    @APIResponse(responseCode = "200", description = "Formulario obtenido correctamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GuardarPlantillaFormularioResponse.class)))
    @APIResponse(responseCode = "404", description = "Formulario no encontrado")
    @APIResponse(responseCode = "500", description = "Error interno del servidor")

    public Uni<Response> buscarPorId(
            @Parameter(description = "Código de plantilla formulario", required = true) @PathParam("codigoPlantillaFormulario") Long codigoPlantillaFormulario) {
        return service.buscarPorId(codigoPlantillaFormulario)
                .onItem().transform(result -> Response.ok(result).build())
                .onFailure(IllegalArgumentException.class)
                .recoverWithItem(err -> Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_404,
                                err.getMessage()))
                        .build())
                .onFailure()
                .recoverWithItem(err -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_500,
                                ErrorMessages.ERROR_INESPERADO))
                        .build());
    }

    @GET
    @Operation(summary = "Listar formularios", description = "Obtiene un listado paginado de formularios con filtros opcionales")

    @APIResponse(responseCode = "200", description = "Listado obtenido correctamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ListarPlantillaFormulariosResponse.class), examples = {
            @ExampleObject(name = "Ejemplo 200", value = """
                    {
                      "formularios": [
                        {
                          "codigoPlantillaFormulario": 1,
                          "codigo": "FORM-ANEXO-001",
                          "nombre": "Formulario de Anexo",
                          "descripcion": "Formulario para registro de anexos",
                          "version": "1.0.0",
                          "estado": "Activo",
                          "paginas": [
                            {
                              "id": "page-d9a5480b",
                              "tipo": "page",
                              "nombre": "",
                              "dimension": 12,
                              "atributos": {
                                "contenidos": [],
                                "secciones": []
                              }
                            }
                          ],
                          "fechaCreacion": "2025-11-10T10:30:00",
                          "fechaActualizacion": "2025-11-10T10:30:00"
                        }
                      ],
                      "total": 100,
                      "totalPaginas": 10,
                      "pagina": 1,
                      "tamanio": 10,
                      "esPrimera": true,
                      "esUltima": false
                    }
                    """)
    }))
    @APIResponse(responseCode = "400", description = "Parámetros de consulta inválidos")
    @APIResponse(responseCode = "500", description = "Error interno del servidor")
    public Uni<Response> listar(
            @Parameter(description = "Número de página (inicia en 1)", example = "1") @QueryParam("pagina") Integer pagina,
            @Parameter(description = "Tamaño de página (registros por página)", example = "10") @QueryParam("limite") Integer limite,
            @Parameter(description = "Texto de búsqueda (busca en código, nombre y descripción)") @QueryParam("buscar") String buscar,
            @Parameter(description = "Campo por el cual ordenar (codigo, nombre, fechaCreacion, fechaActualizacion)", example = "fechaCreacion") @QueryParam("ordenarPor") String ordenarPor,
            @Parameter(description = "Orden de clasificación (asc/desc)", example = "desc") @QueryParam("orden") String orden) {

        ListarPlantillaFormulariosRequest request = new ListarPlantillaFormulariosRequest();
        request.pagina = pagina;
        request.limite = limite;
        request.buscar = buscar;
        request.ordenarPor = ordenarPor;
        request.orden = orden;

        return service.listar(request)
                .onItem().transform(result -> Response.ok(result).build())
                .onFailure()
                .recoverWithItem(err -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_500,
                                ErrorMessages.ERROR_INESPERADO))
                        .build());
    }

    @DELETE
    @Path("/{codigoPlantillaFormulario}")
    @Operation(summary = "Eliminar un formulario", description = "Elimina lógicamente un formulario (marca como eliminado)")
    @APIResponse(responseCode = "204", description = "Formulario eliminado exitosamente")
    @APIResponse(responseCode = "404", description = "Formulario no encontrado")
    @APIResponse(responseCode = "500", description = "Error interno del servidor")

    public Uni<Response> eliminar(
            @Parameter(description = "Código de plantilla formulario", required = true) @PathParam("codigoPlantillaFormulario") Long codigoPlantillaFormulario) {
        return service.eliminar(codigoPlantillaFormulario)
                .onItem().transform(result -> Response.noContent().build())
                .onFailure(IllegalArgumentException.class)
                .recoverWithItem(err -> Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_404,
                                err.getMessage()))
                        .build())
                .onFailure()
                .recoverWithItem(err -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_500,
                                ErrorMessages.ERROR_INESPERADO))
                        .build());
    }

    @PATCH
    @Path("/{codigoPlantillaFormulario}/estado")
    @Operation(summary = "Cambiar estado de una plantilla", description = "Cambia el estado de una plantilla de formulario (A=Activo, I=Inactivo, EC=EN CONSTRUCCIÓN, ER=EN REVISIÓN). IMPORTANTE: Solo se puede cambiar a 'EN REVISIÓN' (ER) desde el estado 'EN CONSTRUCCIÓN' (EC)")
    @APIResponse(responseCode = "200", description = "Estado actualizado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GuardarPlantillaFormularioResponse.class), examples = {
            @ExampleObject(name = "Ejemplo 200", value = """
                    {
                      "codigoPlantillaFormulario": 1,
                      "codigo": "FORM-ANEXO-001",
                      "nombre": "Formulario de Anexo",
                      "descripcion": "Formulario para registro de anexos",
                      "version": "1.0.0",
                      "estado": "ER",
                      "elementos": [...],
                      "fechaCreacion": "2025-11-10T10:30:00",
                      "fechaActualizacion": "2025-12-04T11:30:00"
                    }
                    """)
    }))
    @APIResponse(responseCode = "400", description = "Estado inválido o transición de estado no permitida", content = @Content(mediaType = MediaType.APPLICATION_JSON, examples = {
            @ExampleObject(name = "Estado inválido", value = """
                    { "codigo": "ERR-400", "mensaje": "Estado inválido: XYZ. Los valores permitidos son: A, I, EC, ER, PU" }
                    """),
            @ExampleObject(name = "Transición no permitida", value = """
                    { "codigo": "ERR-400", "mensaje": "Solo se puede cambiar a 'EN REVISIÓN' desde el estado 'EN CONSTRUCCIÓN'. Estado actual: Activo" }
                    """)
    }))
    @APIResponse(responseCode = "404", description = "Plantilla no encontrada")
    @APIResponse(responseCode = "500", description = "Error interno del servidor")
    public Uni<Response> cambiarEstado(
            @Parameter(description = "Código de plantilla formulario", required = true) @PathParam("codigoPlantillaFormulario") Long codigoPlantillaFormulario,
            @Valid CambiarEstadoPlantillaRequest request) {
        return service.cambiarEstado(codigoPlantillaFormulario, request)
                .onItem().transform(result -> Response.ok(result).build())
                .onFailure(IllegalArgumentException.class)
                .recoverWithItem(err -> {
                    if (err.getMessage().contains("No se encontró")) {
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_404,
                                        err.getMessage()))
                                .build();
                    }
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_404,
                                    err.getMessage()))
                            .build();
                })
                .onFailure()
                .recoverWithItem(err -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse(ErrorMessages.ERROR_CODE_500,
                                ErrorMessages.ERROR_INESPERADO))
                        .build());
    }

}
