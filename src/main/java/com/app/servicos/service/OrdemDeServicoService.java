package com.app.servicos.service;

import com.app.servicos.dto.response.OrdemResponseDTO;
import com.app.servicos.entity.ClientePF;
import com.app.servicos.entity.ClientePJ;
import com.app.servicos.entity.OrdemDeServico;
import com.app.servicos.enums.StatusServico;
import com.app.servicos.enums.TipoCliente;
import com.app.servicos.mapper.OrdemServicoMapper;
import com.app.servicos.repository.ClientePFRepository;
import com.app.servicos.repository.ClientePJRepository;
import com.app.servicos.repository.OrdemDeServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdemDeServicoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;
    private final ClientePFRepository clientePFRepository;
    private final ClientePJRepository clientePJRepository;
    private final CalculadoraCustoService calculadoraCustoService;

    @Autowired
    public OrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository, ClientePFRepository clientePFRepository, ClientePJRepository clientePJRepository, CalculadoraCustoService calculadoraCustoService) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.clientePFRepository = clientePFRepository;
        this.clientePJRepository = clientePJRepository;
        this.calculadoraCustoService = calculadoraCustoService;
    }


    public OrdemDeServico criarOrdemDeServico(Long clienteId, TipoCliente tipoCliente, OrdemDeServico ordemDeServico) {

        switch (tipoCliente) {
            case PF:
                clientePFRepository.findById(clienteId)
                        .ifPresentOrElse(
                                clientePF -> configurarOrdemDeServicoParaPF(ordemDeServico, clientePF),
                                () -> { throw new IllegalArgumentException("Cliente PF não encontrado com ID: " + clienteId);
                                });
                break;

            case PJ:
                clientePJRepository.findById(clienteId)
                        .ifPresentOrElse(
                                clientePJ -> configurarOrdemDeServicoParaPJ (ordemDeServico, clientePJ),
                        () -> { throw new IllegalArgumentException("Cliente PJ não encontrado com ID: " + clienteId);
                                });
                break;

            default:
                throw new IllegalArgumentException("Tipo de cliente inválido: " + tipoCliente);
        }

        return ordemDeServicoRepository.save(ordemDeServico);
    }

    public Optional<List<OrdemResponseDTO>> listarOrdensDeServico() {
        List<OrdemDeServico> ordens = ordemDeServicoRepository.findAll();
        if (ordens.isEmpty()) {
            return Optional.empty();
        }

        List<OrdemResponseDTO> dtos = ordens.stream()
                .map(OrdemServicoMapper::mapToDto)
                .collect(Collectors.toList());

        return Optional.of(dtos);
    }

    public Optional<OrdemDeServico> buscarOrdemDeServicoPorId(Long id) {
        return ordemDeServicoRepository.findById(id);
    }

    public Optional<OrdemDeServico> atualizarOrdemDeServico(Long ordemDeServicoId, OrdemDeServico ordemDeServicoAtualizada) {

        Optional<OrdemDeServico> ordemDeServicoExistente = ordemDeServicoRepository.findById(ordemDeServicoId);
        if (ordemDeServicoExistente.isEmpty()) {
            throw new RuntimeException("Ordem de serviço não encontrada para o ID: " + ordemDeServicoId);
        }

        OrdemDeServico ordemDeServico = ordemDeServicoExistente.get();

        verificarSeOrdemDeServicoEstaFechada(ordemDeServico);

        ordemDeServico.setDescricaoServico(ordemDeServicoAtualizada.getDescricaoServico() != null ? ordemDeServicoAtualizada.getDescricaoServico() :
                ordemDeServico.getDescricaoServico());
        ordemDeServico.setStatusServico(ordemDeServicoAtualizada.getStatusServico() != null ? ordemDeServicoAtualizada.getStatusServico() :
                ordemDeServico.getStatusServico());

        if(ordemDeServico.getStatusServico() == StatusServico.FECHADO){
            BigDecimal custoTotal = calculadoraCustoService.calcularCustoTotal(ordemDeServicoId);
            ordemDeServicoAtualizada.setCustoTotal(custoTotal);

        }
        return Optional.of(ordemDeServicoRepository.save(ordemDeServico));
    }

    public void excluirOrdemDeServico(Long ordemDeServicoId) {
        ordemDeServicoRepository.deleteById(ordemDeServicoId);
    }



    private void verificarSeOrdemDeServicoEstaFechada(OrdemDeServico ordemDeServico) {
        if (ordemDeServico.getStatusServico() == StatusServico.FECHADO) {
            throw new RuntimeException("A ordem de serviço já está fechada.");
        }
    }

    private void configurarOrdemDeServicoParaPF(OrdemDeServico ordemDeServico, ClientePF clientePF) {
        ordemDeServico.setClientePF(clientePF);
    }

    private void configurarOrdemDeServicoParaPJ(OrdemDeServico ordemDeServico, ClientePJ clientePJ) {
        ordemDeServico.setClientePJ(clientePJ);
    }

}
