package br.com.osworks.api.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.osworks.domain.model.Cliente;
import br.com.osworks.domain.repository.ClienteRepository;
import br.com.osworks.domain.service.CadastroClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@PersistenceContext
	private EntityManager manager;
	
	@Autowired
	private CadastroClienteService service;

	@Autowired
	private ClienteRepository clienteRepository;

	@GetMapping
	public List<Cliente> teste() {
		// return manager.createQuery("from cliente", Cliente.class).getResultList();
		return clienteRepository.findAll();

	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);

		if (cliente.isPresent())
			return ResponseEntity.ok(cliente.get());

		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente adicionar(@Valid @RequestBody Cliente cliente) {
		return service.salvar(cliente); //clienteRepository.save(cliente);

	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizar(@Valid @PathVariable Long id, @RequestBody Cliente cliente) {
		if (!clienteRepository.existsById(id))
			return ResponseEntity.notFound().build();
		cliente.setId(id);

		return ResponseEntity.ok(service.salvar(cliente));//(clienteRepository.save(cliente));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> remover(@PathVariable Long id) {
		if (!clienteRepository.existsById(id))
			return ResponseEntity.notFound().build();

		//clienteRepository.deleteById(id);
		service.excluir(id);

		return ResponseEntity.noContent().build();
	}

}
