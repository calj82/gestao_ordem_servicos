package com.app.servicos.service;

import com.app.servicos.entity.ClientePF;
import com.app.servicos.entity.ClientePJ;
import com.app.servicos.entity.OrdemDeServico;
import com.app.servicos.enums.StatusServico;
import com.app.servicos.enums.TipoCliente;
import com.app.servicos.repository.ClientePFRepository;
import com.app.servicos.repository.ClientePJRepository;
import com.app.servicos.repository.OrdemDeServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                ClientePF clientePF = clientePFRepository.findById(clienteId).orElse(null);
                if (clientePF == null) {
                    throw new IllegalArgumentException("Cliente PF não encontrado com ID: " + clienteId);
                }
                ordemDeServico.setTipoCliente(TipoCliente.PF);
                ordemDeServico.setClientePF(clientePF);

                break;

            case PJ:
                ClientePJ clientePJ = clientePJRepository.findById(clienteId).orElse(null);
                if (clientePJ == null) {
                    throw new IllegalArgumentException("Cliente PJ não encontrado com ID: " + clienteId);
                }
                ordemDeServico.setTipoCliente(TipoCliente.PJ);
                ordemDeServico.setClientePJ(clientePJ);

                break;

            default:
                throw new IllegalArgumentException("Tipo de cliente inválido: " + tipoCliente);
        }

        return ordemDeServicoRepository.save(ordemDeServico);
    }

    public Optional<List<OrdemDeServico>> listarOrdensDeServico() {
        List<OrdemDeServico> ordens = ordemDeServicoRepository.findAll();
        return ordens.isEmpty() ? Optional.empty() : Optional.of(ordens);
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
            double custoTotal = calculadoraCustoService.calcularCustoTotal(ordemDeServicoId);
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

}
