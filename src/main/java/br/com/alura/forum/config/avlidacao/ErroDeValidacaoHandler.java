package br.com.alura.forum.config.avlidacao;

import br.com.alura.forum.controller.dto.ErroDeFormularioDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception){
        var fieldErrors = exception.getBindingResult().getFieldErrors();
        List<ErroDeFormularioDto> erros = new ArrayList<>();

        fieldErrors.forEach(e -> {
            var mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            var erro = new ErroDeFormularioDto(e.getField(), mensagem);
            erros.add(erro);
        });
        return erros;
    }
}
