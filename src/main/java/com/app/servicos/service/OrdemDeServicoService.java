package com.app.servicos.service;

import com.app.servicos.entity.Endereco;
import com.app.servicos.entity.OrdemDeServico;
import com.app.servicos.repository.ClientePFRepository;
import com.app.servicos.repository.OrdemDeServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdemDeServicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ClientePFRepository clientePFRepository;

    @Autowired
    public OrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository, ClientePFRepository clientePFRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.clientePFRepository = clientePFRepository;
    }

    public OrdemDeServico criarOrdemDeServicoComCliente(Long clienteId, OrdemDeServico ordemDeServico) {
        Endereco endereco = clientePFRepository.findById(clienteId).orElse(null).getEndereco();
        if (endereco == null) {
            throw new IllegalArgumentException("Cliente não encontrado com ID: " + clienteId);
        }
        ordemDeServico.setId(clienteId);
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
