package br.com.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import br.com.osworks.domain.exception.EntidadeNaoEncontradaException;
import br.com.osworks.domain.exception.NegocioException;
import br.com.osworks.domain.model.Cliente;
import br.com.osworks.domain.model.Comentario;
import br.com.osworks.domain.model.OrdemServico;
import br.com.osworks.domain.model.StatusOrdemServico;
import br.com.osworks.domain.repository.ClienteRepository;
import br.com.osworks.domain.repository.ComentarioRepository;
import br.com.osworks.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {

	@Autowired
	private OrdemServicoRepository repository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private ComentarioRepository comentarioRepository;

	public OrdemServico criar(OrdemServico ordemServico) {

		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("Cliente nao encontrado"));

		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());

		return repository.save(ordemServico);
	}

	public Comentario adicionarComentario(Long id, String descricao) {
		OrdemServico ordemServico = buscar(id);

		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);

		return comentarioRepository.save(comentario);

	}

	public void finalizar(Long id) {
		OrdemServico ordemServico = buscar(id);

		ordemServico.finalizar();
		repository.save(ordemServico);
	}

	public void cancelar(Long id) {
		OrdemServico ordemServico = buscar(id);

		ordemServico.cancelar();
		repository.save(ordemServico);
	}

	private OrdemServico buscar(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem Servico nao encontrada"));
	}

}
