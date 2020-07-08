package br.com.osworks.api.controller;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.osworks.api.model.ComentarioInput;
import br.com.osworks.api.model.ComentarioRepresentation;
import br.com.osworks.domain.exception.EntidadeNaoEncontradaException;
import br.com.osworks.domain.model.Comentario;
import br.com.osworks.domain.model.OrdemServico;
import br.com.osworks.domain.repository.OrdemServicoRepository;
import br.com.osworks.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico/{id}/comentarios")
public class ComentarioController {
	
	@Autowired
	private GestaoOrdemServicoService servico;
	
	@Autowired 
	private OrdemServicoRepository repository;
	
	@Autowired
	ModelMapper model;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioRepresentation adicionar(@PathVariable Long id, @Valid @RequestBody ComentarioInput comentarioInput) {
		Comentario comentario = servico.adicionarComentario(id, comentarioInput.getDescricao());
		
		return toModel(comentario);
	}
	
	private ComentarioRepresentation toModel(Comentario comentario) {
		return model.map(comentario, ComentarioRepresentation.class);
		
	}
	
	@GetMapping
	public List<ComentarioRepresentation> comentarios(@PathVariable Long id){
		OrdemServico servico = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Servico nao encontrado"));
		
		return toComentarioModel(servico.getComentarios());
	}
	
	private List<ComentarioRepresentation> toComentarioModel(List<Comentario> comentarios){
		return comentarios.stream().map(comentario -> toModel(comentario)).collect(Collectors.toList());
		
	}

}
