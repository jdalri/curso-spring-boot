package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // vai ser chamado toda vez que o BeanValidation disparar uma excessão
public class ErroValidacaoHandler {
	@Autowired
	private MessageSource messageSource; // ajuda com a internacionalizacao
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) // seta o tipo de retorno (por padrao devolveria um 200, por considerar que a exception foi tratada aqui)
	@ExceptionHandler(MethodArgumentNotValidException.class) // vai pegar todos os erros desse tipo. Esse é o erro lançado na validação do formulário
	public List<ErroFormularioDto> handle(MethodArgumentNotValidException exception) {
		List<ErroFormularioDto> dto = new ArrayList<>();
		// pega todos os erros
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		
		fieldErrors.forEach(erro -> {
			String mensagem = messageSource.getMessage(erro, LocaleContextHolder.getLocale());
			ErroFormularioDto erroFormularioDto = new ErroFormularioDto(erro.getField(), mensagem);
			dto.add(erroFormularioDto);
		});
		
		return dto;
	}
}
