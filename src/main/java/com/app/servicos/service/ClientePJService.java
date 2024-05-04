package com.app.servicos.service;

import com.app.servicos.entity.ClientePJ;
import com.app.servicos.entity.Endereco;
import com.app.servicos.repository.ClientePJRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientePJService {

    private final ClientePJRepository clientePJRepository;
    private final CepService cepService;

    @Autowired
    public ClientePJService(ClientePJRepository clientePJRepository, CepService cepService) {
        this.clientePJRepository = clientePJRepository;
        this.cepService = cepService;
    }

    public ResponseEntity<ClientePJ> criarClientePJ(ClientePJ cliente) {
        if (cliente.getEndereco() != null && cliente.getEndereco().getCep() != null && !cliente.getEndereco().getCep().isEmpty()) {
            EnderecoCep enderecoCep = cepService.consultarCep(cliente.getEndereco().getCep());
            if (enderecoCep != null) {
                cliente.getEndereco().setEndereco(enderecoCep.getLogradouro());
                cliente.getEndereco().setBairro(enderecoCep.getBairro());
                cliente.getEndereco().setCidade(enderecoCep.getLocalidade());
                cliente.getEndereco().setUf(enderecoCep.getUf());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        ClientePJ novoCliente = clientePJRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    public ResponseEntity<ClientePJ> buscarClientePJ(Long clienteId) {
        Optional<ClientePJ> clienteOptional = clientePJRepository.findById(clienteId);
        return clienteOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<ClientePJ> atualizarClientePJ(Long clienteId, ClientePJ clienteAtualizado) {
        Optional<ClientePJ> clienteExistenteOptional = clientePJRepository.findById(clienteId);
        if (clienteExistenteOptional.isPresent()) {
            ClientePJ clienteExistente = clienteExistenteOptional.get();

            // Atualiza os campos do clientePJ existente apenas se forem fornecidos na atualização
            clienteExistente.setRazaoSocial(clienteAtualizado.getRazaoSocial() != null ? clienteAtualizado.getRazaoSocial() : clienteExistente.getRazaoSocial());
            clienteExistente.setCnpj(clienteAtualizado.getCnpj() != null ? clienteAtualizado.getCnpj() : clienteExistente.getCnpj());
            clienteExistente.setNomeFantasia(clienteAtualizado.getNomeFantasia() != null ? clienteAtualizado.getNomeFantasia() : clienteExistente.getNomeFantasia());

            // Atualizar os campos da classe Endereço incorporada ao clientePJ apenas se forem fornecidos na atualização
            Endereco enderecoInfo = clienteExistente.getEndereco();
            enderecoInfo.setEndereco(clienteAtualizado.getEndereco().getEndereco() != null ? clienteAtualizado.getEndereco().getEndereco() : enderecoInfo.getEndereco());
            enderecoInfo.setNumero(clienteAtualizado.getEndereco().getNumero() != null ? clienteAtualizado.getEndereco().getNumero() : enderecoInfo.getNumero());
            enderecoInfo.setComplemento(clienteAtualizado.getEndereco().getComplemento() != null ? clienteAtualizado.getEndereco().getComplemento() : enderecoInfo.getComplemento());

            String cep = clienteAtualizado.getEndereco().getCep();
            if (cep != null && !cep.isEmpty()) {
                EnderecoCep enderecoCep = cepService.consultarCep(cep);
                if (enderecoCep != null) {
                    enderecoInfo.setEndereco(enderecoCep.getLogradouro());
                    enderecoInfo.setBairro(enderecoCep.getBairro());
                    enderecoInfo.setCidade(enderecoCep.getLocalidade());
                    enderecoInfo.setUf(enderecoCep.getUf());
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

            ClientePJ clienteAtualizadoEntity = clientePJRepository.save(clienteExistente);
            return ResponseEntity.ok(clienteAtualizadoEntity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deletarClientePJ(Long clienteId) {
        if (clientePJRepository.existsById(clienteId)) {
            clientePJRepository.deleteById(clienteId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
