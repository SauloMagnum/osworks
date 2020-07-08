package br.com.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.osworks.api.model.OrdemServicoInput;
import br.com.osworks.api.model.OrdemServicoRepresentationModel;
import br.com.osworks.domain.model.OrdemServico;
import br.com.osworks.domain.repository.OrdemServicoRepository;
import br.com.osworks.domain.service.GestaoOrdemServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

	@Autowired
	private GestaoOrdemServicoService servico;

	@Autowired
	private OrdemServicoRepository ordemServicoRepository;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrdemServicoRepresentationModel criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
		OrdemServico ordemServico = toEntity(ordemServicoInput);
		
		return toModel(servico.criar(ordemServico));
	}

	@GetMapping
	public List<OrdemServicoRepresentationModel> listar() {
		return toCollectionModel(ordemServicoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrdemServicoRepresentationModel> buscar(@PathVariable Long id) {
		Optional<OrdemServico> ordemServico = ordemServicoRepository.findById(id);

		if (ordemServico.isPresent()) {
			OrdemServicoRepresentationModel representation = toModel(ordemServico.get());
			return ResponseEntity.ok(representation);
		}

		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("{id}/finalizacao")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void finalizar(@PathVariable Long id) {
		servico.finalizar(id);
	}
	
	@PutMapping("{id}/cancelamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelar(@PathVariable Long id) {
		servico.cancelar(id);
	}

	private OrdemServicoRepresentationModel toModel(OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoRepresentationModel.class);
	}

	private List<OrdemServicoRepresentationModel> toCollectionModel(List<OrdemServico> ordensServico) {
		return ordensServico.stream().map(ordemServico -> toModel(ordemServico)).collect(Collectors.toList());

	}
	
	private OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
		return modelMapper.map(ordemServicoInput, OrdemServico.class);
	}
}
