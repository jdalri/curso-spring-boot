package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.modelo.Topico;

													 // Classe, Type do atributo id
public interface TopicoRepository extends JpaRepository<Topico, Long> {
	// se existisse um atributo cursoNome dentro de Topico, pra não dar ambiguidade, seria fetio findByCurso_Nome 
	// para buscar pelo Nome da entidade Curso
	
	// vai buscar pela Entidade Curso e o atributo Nome dessa entidade
	List<Topico> findByCursoNome(String nomeCurso);
	
	// Caso não queira usar a facilidade do Spring Data, dá pra fazer assim
	@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
	List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);

}
