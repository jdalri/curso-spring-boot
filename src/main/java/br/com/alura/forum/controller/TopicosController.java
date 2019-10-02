package br.com.alura.forum.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;

//@Controller
@RestController // faz com todos os métodos dessa classe funcoiname como se tivessem o @ResponseBody
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@RequestMapping("/topicos")
//	@ResponseBody
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
}
