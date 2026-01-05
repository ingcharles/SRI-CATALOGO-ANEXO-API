package ec.gob.sri.api.catalogo.shared.exception;

import ec.gob.sri.api.catalogo.infraestructure.rest.constant.ErrorMessages;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ManejadorExcepcion implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorDto(ErrorMessages.ERROR_CODE_500, exception.getMessage()))
                .build();
    }

    public static class ErrorDto {
        public final String codigo;
        public final String mensaje;

        public ErrorDto(String codigo, String mensaje) {
            this.codigo = codigo;
            this.mensaje = mensaje;
        }
    }
}
