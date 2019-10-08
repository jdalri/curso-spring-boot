package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
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
	@Cacheable(value = "listaDeTopicos")
//	public List<TopicoDto> lista(
//	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @RequestParam int pagina, @RequestParam int qtd,	@RequestParam String ordenacao) {
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, @PageableDefault(sort = "id", direction = Direction.DESC) Pageable paginacao) {
//		Topico topico = new Topico("Duvida", "Duvida com Spring", new Curso("Spring", "Programação"));
//		return TopicoDto.converter(Arrays.asList(topico, topico, topico));

		// para paginação
//		Pageable paginacao = PageRequest.of(pagina, qtd, Direction.DESC, ordenacao);
		
		if (nomeCurso == null) {
//			List<Topico> topicos = topicoRepository.findAll();
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			
			return TopicoDto.converter(topicos);
		}
		else {
//			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			
			return TopicoDto.converter(topicos);
		}
	}
	
//	@RequestMapping(value = "/topicos", method = RequestMethod.POST)
	@PostMapping
	@Transactional									// @Valid vai realizar o BeanValidation
	@CacheEvict(value = "listaDeTopicos", allEntries = true) // serve para limpar o cache toda vez que salvar um novo topico
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);
		
		topicoRepository.save(topico);
		
		// monta a uri (UriBuilder é do Spring e vai me dar o domínio do servidor já), o buildAndExpand serve pra botar o valor do parâmetro id
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		
		// created por padrão já retorna um código HTTP 201 e devolve uma uri de pra onde ir após criar e um objeto criado
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@GetMapping("/{id}")
	//public TopicoDto detalhar(@PathVariable("id") Long codigo) { // @PathVariable diz ao Spring que o parametro vem na URL /topicos/123 e nao na query /topicos?id=123
//	public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
//		Topico topico = topicoRepository.getOne(id);
		Optional<Topico> topico = topicoRepository.findById(id);
		
		if (topico.isPresent())
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		
		return ResponseEntity.notFound().build();
	}

	// PUT seria pra sobrescrever o model inteiro / PATCH seria pra alterar uma pequena parte do model
	@PutMapping("/{id}")
	// nao precisa chamar um método update() da vida, porque quando acabar a execução do método, a JPA vai fazer um commit.	Então vai notar que ese model mudou e vai fazer a alteração no banco
	// deve ser usado em operações salvar, editar e excluir
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
//		Topico topico = form.atualizar(id, topicoRepository);
//		return ResponseEntity.ok(new TopicoDto(topico));
		
		Optional<Topico> optional = topicoRepository.findById(id);
		
		if (optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
//		topicoRepository.deleteById(id);
//		return ResponseEntity.ok().build();
		
		Optional<Topico> optional = topicoRepository.findById(id);
		
		if (optional.isPresent()) {
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
