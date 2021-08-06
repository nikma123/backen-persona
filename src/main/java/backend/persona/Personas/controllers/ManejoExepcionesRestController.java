package backend.persona.Personas.controllers;

import backend.persona.Personas.dto.RespuestaError;
import backend.persona.Personas.exceptions.ObjetoExistenteException;
import backend.persona.Personas.exceptions.ObjetoNoExisteException;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ManejoExepcionesRestController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ObjetoExistenteException.class)
    public ResponseEntity<RespuestaError> objetoExistente(ObjetoExistenteException ex) {
        var respuesta = new RespuestaError(ex.getMessage());
        return ResponseEntity.unprocessableEntity().body(respuesta);
    }

    @ExceptionHandler(ObjetoNoExisteException.class)
    public ResponseEntity<RespuestaError> objetoNoExistente(ObjetoNoExisteException ex) {
        var respuesta = new RespuestaError(ex.getMessage());
        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<RespuestaError> errorInternoDelServidor(Exception ex) {
        ex.printStackTrace();
        var respuesta = new RespuestaError("Ocurrio un error inesperado");
        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<RespuestaError> datosErroneos(Exception ex) {
        ex.printStackTrace();
        var respuesta = new RespuestaError(ex.getMessage());
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException exception, @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            WebRequest request) {
        List<String> validationErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return getExceptionResponseEntity(HttpStatus.BAD_REQUEST, validationErrors);
    }

    private ResponseEntity<Object> getExceptionResponseEntity(final HttpStatus status,
                                                              List<String> errors) {
        List<RespuestaError> errores =
                !CollectionUtils.isEmpty(errors) ? errors.stream().map(RespuestaError::new)
                        .collect(Collectors.toList())
                        : Collections.singletonList(new RespuestaError(status.getReasonPhrase()));
        return new ResponseEntity<>(errores, status);
    }
}
