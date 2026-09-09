/**
 * Clase FormularioResource.java 02 dic. 2025
 * Copyright 2025 Servicio de Rentas Internas.
 * Todos los derechos reservados.
 */
package ec.gob.sri.api.catalogo.infraestructure.rest;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ec.gob.sri.api.catalogo.application.dto.ConsultarFormulariosRequest;
import ec.gob.sri.api.catalogo.application.dto.ConsultarFormulariosResponse;
import ec.gob.sri.api.catalogo.application.dto.CrearFormularioRequest;
import ec.gob.sri.api.catalogo.application.dto.ErrorResponse;
import ec.gob.sri.api.catalogo.application.dto.FormularioResponse;
import ec.gob.sri.api.catalogo.application.dto.ModificarFormularioRequest;
import ec.gob.sri.api.catalogo.application.service.FormularioService;
import ec.gob.sri.api.catalogo.infraestructure.rest.constant.ErrorMessages;
import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Controlador REST para Formulario
 *
 * @author SRI
 */
@Path("/formulario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Formulario", description = "Gestión de formularios basados en plantillas")
public class FormularioResource {

        private final FormularioService service;

        public FormularioResource(FormularioService service) {
                this.service = service;
        }

        @POST
        @Operation(summary = "Crear un nuevo formulario", description = "Crea un nuevo formulario basado en una plantilla")
        @APIResponse(responseCode = "201", description = "Formulario creado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FormularioResponse.class), examples = {
                        @ExampleObject(name = "Ejemplo 201", value = """
                                        {
                                          "codigoFormulario": 1,
                                          "codigoPlantillaFormulario": 1,
                                          "codigoUsuario": "USR001",
                                          "identificacionUsuario": "1234567890",
                                          "estado": "Activo",
                                          "elementos": [
                                            {
                                              "id": "page-001",
                                              "tipo": "page",
                                              "nombre": "Página 1",
                                              "contenido": {}
                                            }
                                          ],
                                          "fechaCreacion": "2025-12-02T10:30:00",
                                          "fechaActualizacion": "2025-12-02T10:30:00"
                                        }
                                        """)
        }))
        @APIResponse(responseCode = "400", description = "Petición inválida (datos con formato o valores incorrectos)", content = @Content(mediaType = MediaType.APPLICATION_JSON, examples = {
                        @ExampleObject(name = "Ejemplo 400", value = """
                                        { "codigo": "ERR-400", "mensaje": "El código de plantilla formulario es obligatorio" }
                                        """)
        }))
        @APIResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON, examples = {
                        @ExampleObject(name = "Ejemplo 500", value = ErrorMessages.EXAMPLE_ERROR_500)
        }))
        public Uni<Response> crear(@Valid CrearFormularioRequest request) {
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
        @Path("/{codigoFormulario}")
        @Operation(summary = "Actualizar un formulario existente", description = "Actualiza los datos de un formulario existente")
        @APIResponse(responseCode = "200", description = "Formulario actualizado exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FormularioResponse.class)))
        @APIResponse(responseCode = "400", description = "Petición inválida", content = @Content(mediaType = MediaType.APPLICATION_JSON))
        @APIResponse(responseCode = "404", description = "Formulario no encontrado")
        @APIResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = MediaType.APPLICATION_JSON))
        public Uni<Response> actualizar(
                        @Parameter(description = "Código del formulario", required = true) @PathParam("codigoFormulario") Long codigoFormulario,
                        @Valid ModificarFormularioRequest request) {
                return service.actualizar(request, codigoFormulario)
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
        @Path("/{codigoFormulario}")
        @Operation(summary = "Consultar un formulario por ID", description = "Obtiene los datos de un formulario específico por su identificador")
        @APIResponse(responseCode = "200", description = "Formulario obtenido correctamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = FormularioResponse.class)))
        @APIResponse(responseCode = "404", description = "Formulario no encontrado")
        @APIResponse(responseCode = "500", description = "Error interno del servidor")
        public Uni<Response> buscarPorId(
                        @Parameter(description = "Código del formulario", required = true) @PathParam("codigoFormulario") Long codigoFormulario) {
                return service.buscarPorId(codigoFormulario)
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
        @APIResponse(responseCode = "200", description = "Listado obtenido correctamente", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ConsultarFormulariosResponse.class), examples = {
                        @ExampleObject(name = "Ejemplo 200", value = """
                                        {
                                          "formularios": [
                                            {
                                              "codigoFormulario": 1,
                                              "codigoPlantillaFormulario": 1,
                                              "codigoUsuario": "USR001",
                                              "identificacionUsuario": "1234567890",
                                              "estado": "Activo",
                                              "elementos": [{"id": "field-001", "valor": "dato ingresado"}],
                                              "elementosPlantilla": [{"id": "page-001", "tipo": "page", "nombre": "Datos Generales"}],
                                              "nombrePlantilla": "Formulario de Anexo",
                                              "descripcionPlantilla": "Formulario para registro de anexos",
                                              "fechaCreacion": "2025-12-02T10:30:00",
                                              "fechaActualizacion": "2025-12-02T10:30:00"
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
                        @Parameter(description = "Número de página (inicia en 1)") @QueryParam("pagina") Integer pagina,
                        @Parameter(description = "Tamaño de página (registros por página)") @QueryParam("limite") Integer limite,
                        @Parameter(description = "Código de plantilla formulario") @QueryParam("codigoPlantillaFormulario") Long codigoPlantillaFormulario,
                        @Parameter(description = "Código de usuario") @QueryParam("codigoUsuario") String codigoUsuario,
                        @Parameter(description = "Identificación de usuario") @QueryParam("identificacionUsuario") String identificacionUsuario,
                        @Parameter(description = "Texto de búsqueda (busca en codigoUsuario, identificacionUsuario)") @QueryParam("buscar") String buscar,
                        @Parameter(description = "Campo por el cual ordenar (codigoFormulario, fechaCreacion, fechaActualizacion)") @QueryParam("ordenarPor") String ordenarPor,
                        @Parameter(description = "Orden de clasificación (asc/desc)") @QueryParam("orden") String orden) {

                System.out.println("[FormularioResource.listar] 📥 REQUEST recibido:");
                System.out.println("  - pagina: " + pagina);
                System.out.println("  - limite: " + limite);
                System.out.println("  - codigoPlantillaFormulario: " + codigoPlantillaFormulario);
                System.out.println("  - codigoUsuario: " + codigoUsuario);
                System.out.println("  - identificacionUsuario: " + identificacionUsuario);
                System.out.println("  - buscar: " + buscar);
                System.out.println("  - ordenarPor: " + ordenarPor);
                System.out.println("  - orden: " + orden);
                
                ConsultarFormulariosRequest request = new ConsultarFormulariosRequest();
                request.pagina = pagina;
                request.limite = limite;
                request.codigoPlantillaFormulario = codigoPlantillaFormulario;
                request.codigoUsuario = codigoUsuario;
                request.identificacionUsuario = identificacionUsuario;
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
        @Path("/{codigoFormulario}")
        @Operation(summary = "Eliminar un formulario", description = "Elimina lógicamente un formulario (marca como eliminado)")
        @APIResponse(responseCode = "204", description = "Formulario eliminado exitosamente")
        @APIResponse(responseCode = "404", description = "Formulario no encontrado")
        @APIResponse(responseCode = "500", description = "Error interno del servidor")
        public Uni<Response> eliminar(
                        @Parameter(description = "Código del formulario", required = true) @PathParam("codigoFormulario") Long codigoFormulario) {
                return service.eliminar(codigoFormulario)
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
}
