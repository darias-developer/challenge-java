package cl.test.challenge.config;

import cl.test.challenge.data.ErrorResponse;
import cl.test.challenge.util.ExternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * CustomExceptionHandler: clase que customiza los errores lanzados pro la api
 *
 * @author  David Arias
 * @version 1.0
 * @since   2021-06-28
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    /**
     * Este metodo obtiene los errores no controlados por ala api
     * @param ex excepcion arrojada por la api
     * @param headers headers de la respuesta
     * @param status estatus de la respuesta
     * @param request contenido de la respuesta
     * @return retorna el error customizado
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        log.info("init handleNoHandlerFoundException");

        log.error(ex.getMessage(), ex);

        ErrorResponse errorResponse = null;

        if (status == HttpStatus.NOT_FOUND && ex.getMessage().contains("/city/temperature")) {
            errorResponse = new ErrorResponse(
                    messageSource.getMessage("url.city.temperature.error", null, LocaleContextHolder.getLocale()));
        } else {
            errorResponse = new ErrorResponse(
                    messageSource.getMessage("generic.error", null, LocaleContextHolder.getLocale()));
        }

        log.info("end handleNoHandlerFoundException");

        return handleExceptionInternal(ex, errorResponse, headers, status, request);
    }

    /**
     * Este metodo obtiene los errores arrojado por la api de validacion de spring
     * @param ex excepcion arrojada por la api
     * @param request contenido de la respuesta
     * @return retorna el error customizado
     */
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

        log.info("init handleConstraintViolationException");

        log.error(ex.getMessage(), ex);

        ErrorResponse errorResponse = null;

        //obtiene el primer error y lo envia como respuesta
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorResponse = new ErrorResponse(
                    messageSource.getMessage(violation.getMessage(), null, LocaleContextHolder.getLocale()));
            break;
        }

        log.info("end handleConstraintViolationException");

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Este metodo obtiene los errores arrojado de tipo ExternalException
     * @param ex excepcion arrojada por la api
     * @param request contenido de la respuesta
     * @return retorna el error customizado
     */
    @ExceptionHandler({ ExternalException.class })
    public ResponseEntity<Object> handleExternalException(ExternalException ex, WebRequest request) {

        log.info("init handleExternalException");

        log.error(ex.getMessage(), ex);

        log.info("end handleExternalException");

        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
