package hpbtw.github.com.ms_produto.exceptions.handler;

import hpbtw.github.com.ms_produto.exceptions.ResourceNotFoundException;
import hpbtw.github.com.ms_produto.exceptions.dto.CustomErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorDTO> handleResourceNotFound(ResourceNotFoundException e,
                                                                 HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; //404
        CustomErrorDTO err = new CustomErrorDTO(Instant.now(), status.value(),
                e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status).body(err);
    }


    // 400 - JSON malformado / corpo inválido (não dá para desserializar)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomErrorDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException e,
                                                                       HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST; //400
        CustomErrorDTO err = new CustomErrorDTO(Instant.now(), status.value(),
                "Requisição inválida (JSON malformado ou corpo não interpretável).",
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    // 400 - tipo inválido em PathVariable/RequestParam (ex.: /produtos/abc quando espera Long)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomErrorDTO> handleTypeMismatch(MethodArgumentNotValidException e,
                                                             HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST; //400
        CustomErrorDTO err = new CustomErrorDTO(Instant.now(), status.value(),
                "Requisição inválida (parâmetro com tipo/formato incorreto).",
                request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }


    // 500 - fallback para qualquer erro não tratado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorDTO> handleGenericException(Exception e,
                                                                 HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        CustomErrorDTO err = new CustomErrorDTO(
                Instant.now(), status.value(),
                "Erro interno inesperado.",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }
}
