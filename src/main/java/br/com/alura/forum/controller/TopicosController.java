package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

//@Controller
@RestController // faz com todos os métodos dessa classe funcoiname como se tivessem o @ResponseBody
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
//	@RequestMapping(value = "/topicos", method = RequestMethod.GET)
//	@ResponseBody
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {
//		Topico topico = new Topico("Duvida", "Duvida com Spring", new Curso("Spring", "Programação"));
//		return TopicoDto.converter(Arrays.asList(topico, topico, topico));

		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			
			return TopicoDto.converter(topicos);
		}
		else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			
			return TopicoDto.converter(topicos);
		}
	}
	
//	@RequestMapping(value = "/topicos", method = RequestMethod.POST)
	@PostMapping
												// @Valid vai realizar o BeanValidation
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		
		topicoRepository.save(topico);
		
		// monta a uri (UriBuilder é do Spring e vai me dar o domínio do servidor já), o buildAndExpand serve pra botar o valor do parâmetro id
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		
		// created por padrão já retorna um código HTTP 201 e devolve uma uri de pra onde ir após criar e um objeto criado
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
}
