package com.app.servicos.service;

import com.app.servicos.entity.Clientes;
import com.app.servicos.entity.OrdemDeServico;
import com.app.servicos.repository.ClienteRepository;
import com.app.servicos.repository.OrdemDeServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdemDeServicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public OrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository, ClienteRepository clienteRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.clienteRepository = clienteRepository;
    }

    public OrdemDeServico criarOrdemDeServicoComCliente(Long clienteId, OrdemDeServico ordemDeServico) {
        Clientes cliente = clienteRepository.findById(clienteId).orElse(null);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + clienteId);
        }
        ordemDeServico.setCliente(cliente);
        ordemDeServico.setCustoServico(ordemDeServico.getCustoServico());
        ordemDeServico.setCustoAdicional(ordemDeServico.getCustoAdicional());

        return ordemDeServicoRepository.save(ordemDeServico);
    }

    public List<OrdemDeServico> listarOrdensDeServico() {
        return ordemDeServicoRepository.findAll();
    }

    public Optional<OrdemDeServico> buscarOrdemDeServicoPorId(Long ordemDeServicoId) {
        return ordemDeServicoRepository.findById(ordemDeServicoId);
    }

    public OrdemDeServico criarOrdemDeServico(OrdemDeServico ordemDeServico) {
        return ordemDeServicoRepository.save(ordemDeServico);
    }

    public Optional<OrdemDeServico> atualizarOrdemDeServico(Long ordemDeServicoId, OrdemDeServico ordemDeServicoAtualizada) {
        Optional<OrdemDeServico> ordemDeServicoExistente = ordemDeServicoRepository.findById(ordemDeServicoId);
        if (ordemDeServicoExistente.isPresent()) {
            OrdemDeServico ordemDeServico = ordemDeServicoExistente.get();
            ordemDeServico.setDescricaoServico(ordemDeServicoAtualizada.getDescricaoServico());
            return Optional.of(ordemDeServicoRepository.save(ordemDeServico));
        } else {
            return Optional.empty();
        }
    }
    public void excluirOrdemDeServico(Long ordemDeServicoId) {
        ordemDeServicoRepository.deleteById(ordemDeServicoId);
    }


}
