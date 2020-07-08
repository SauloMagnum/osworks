package br.com.osworks.domain.service;

import org.springframework.stereotype.Service;

import br.com.osworks.domain.exception.NegocioException;
import br.com.osworks.domain.model.Cliente;
import br.com.osworks.domain.repository.ClienteRepository;

@Service
public class CadastroClienteService {

	private ClienteRepository clienteRepository;

	public Cliente salvar(Cliente cliente) {
		Cliente clienteExistente = clienteRepository.findByEmail(cliente.getEmail());
		
		if(clienteExistente != null && !clienteExistente.equals(cliente))
			throw new NegocioException("Ja existe cliente cadastrado");
			
		return clienteRepository.save(cliente);
	}

	public void excluir(Long id) {
		clienteRepository.deleteById(id);
	}

}
