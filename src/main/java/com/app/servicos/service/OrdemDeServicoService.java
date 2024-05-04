package com.app.servicos.service;

import com.app.servicos.entity.ClientePF;
import com.app.servicos.entity.ClientePJ;
import com.app.servicos.entity.Endereco;
import com.app.servicos.entity.OrdemDeServico;
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

    @Autowired
    public OrdemDeServicoService(OrdemDeServicoRepository ordemDeServicoRepository, ClientePFRepository clientePFRepository, ClientePJRepository clientePJRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.clientePFRepository = clientePFRepository;
        this.clientePJRepository = clientePJRepository;
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

    public List<OrdemDeServico> listarOrdensDeServico() {

        List<OrdemDeServico> ordens = ordemDeServicoRepository.findAll();

        if (ordens.isEmpty()) {
            throw new RuntimeException("Nenhuma ordem de serviço encontrada.");
        }
        return ordens;
    }

    public Optional<OrdemDeServico> buscarOrdemDeServicoPorId(Long id) {

        Optional<OrdemDeServico> ordem = ordemDeServicoRepository.findById(id);

        if (ordem.isEmpty()) {
            throw new RuntimeException("Ordem de serviço não encontrada para o ID: " + id);
        }
        return ordem;
    }

    public Optional<OrdemDeServico> atualizarOrdemDeServico(Long ordemDeServicoId, OrdemDeServico ordemDeServicoAtualizada) {

        Optional<OrdemDeServico> ordemDeServicoExistente = ordemDeServicoRepository.findById(ordemDeServicoId);
        if (ordemDeServicoExistente.isEmpty()) {
            throw new RuntimeException("Ordem de serviço não encontrada para o ID: " + ordemDeServicoId);
        }
        OrdemDeServico ordemDeServico = ordemDeServicoExistente.get();
        ordemDeServico.setDescricaoServico(ordemDeServicoAtualizada.getDescricaoServico());
        return Optional.of(ordemDeServicoRepository.save(ordemDeServico));
    }

    public void excluirOrdemDeServico(Long ordemDeServicoId) {
        ordemDeServicoRepository.deleteById(ordemDeServicoId);
    }

}
