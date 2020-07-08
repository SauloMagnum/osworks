package br.com.osworks.api.model;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OrdemServicoInput {
	@NotBlank
	private String descricao;
	
	@NotNull
	private BigDecimal preco;
	
	@Valid
	@NotNull
	private ClienteIdInput clienteId;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public ClienteIdInput getClienteId() {
		return clienteId;
	}

	public void setClienteId(ClienteIdInput clienteId) {
		this.clienteId = clienteId;
	}

}
