package com.app.servicos.service;

import com.app.servicos.entity.Endereco;
import com.app.servicos.entity.ClientePF;
import com.app.servicos.repository.ClientePFRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientePFService {

    private final ClientePFRepository clientePFRepository;
    private final CepService cepService;

    @Autowired
    public ClientePFService(ClientePFRepository clientePFRepository, CepService cepService) {
        this.clientePFRepository = clientePFRepository;
        this.cepService = cepService;
    }

//    public ResponseEntity<ClientePF> criarClientePF(ClientePF cliente) {
//        if (cliente.getCliente().getCep() != null && !cliente.getCliente().getCep().isEmpty()) {
//            EnderecoCep enderecoCep = cepService.consultarCep(cliente.getCliente().getCep());
//            if (enderecoCep != null) {
//                cliente.getCliente().setEndereco(enderecoCep.getLogradouro() + ", " + enderecoCep.getBairro() + ", " + enderecoCep.getLocalidade() + " - " + enderecoCep.getUf());
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//        }
//
//        ClientePF novoCliente = clientePFRepository.save(cliente);
//        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
//    }

    public ResponseEntity<ClientePF> criarClientePF(ClientePF cliente) {
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

        ClientePF novoCliente = clientePFRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }


    public ResponseEntity<ClientePF> buscarClientePF(Long clienteId) {
        Optional<ClientePF> clienteOptional = clientePFRepository.findById(clienteId);
        return clienteOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<ClientePF> atualizarClientePF(Long clienteId, ClientePF clienteAtualizado) {
        Optional<ClientePF> clienteExistenteOptional = clientePFRepository.findById(clienteId);
        if (clienteExistenteOptional.isPresent()) {
            ClientePF clienteExistente = clienteExistenteOptional.get();

            // Atualiza os campos do clientePF existente apenas se forem fornecidos na atualização
            clienteExistente.setNome(clienteAtualizado.getNome() != null ? clienteAtualizado.getNome() : clienteExistente.getNome());
            clienteExistente.setCpf(clienteAtualizado.getCpf() != null ? clienteAtualizado.getCpf() : clienteExistente.getCpf());
            clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento() != null ? clienteAtualizado.getDataNascimento() : clienteExistente.getDataNascimento());
            clienteExistente.setIdade(clienteAtualizado.getIdade() != null ? clienteAtualizado.getIdade() : clienteExistente.getIdade());

            // Atualizar os campos da cliente incorporada ao clientePF apenas se forem fornecidos na atualização
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

            ClientePF clienteAtualizadoEntity = clientePFRepository.save(clienteExistente);
            return ResponseEntity.ok(clienteAtualizadoEntity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    public ResponseEntity<ClientePF> atualizarClientePF(Long clienteId, ClientePF clienteAtualizado) {
//        Optional<ClientePF> clienteExistenteOptional = clientePFRepository.findById(clienteId);
//        if (clienteExistenteOptional.isPresent()) {
//            ClientePF clienteExistente = clienteExistenteOptional.get();
//            clienteExistente.setNome(clienteAtualizado.getNome());
//            clienteExistente.setCpf(clienteAtualizado.getCpf());
//            clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
//            clienteExistente.setIdade(clienteAtualizado.getIdade());
//
//            Cliente clienteInfo = clienteExistente.getCliente();
//            clienteInfo.setEndereco(clienteAtualizado.getCliente().getEndereco());
//            clienteInfo.setNumero(clienteAtualizado.getCliente().getNumero());
//            clienteInfo.setComplemento(clienteAtualizado.getCliente().getComplemento());
//            // Adicione outros atributos conforme necessário
//
//            if (clienteAtualizado.getCliente().getCep() != null && !clienteAtualizado.getCliente().getCep().isEmpty()) {
//                EnderecoCep enderecoCep = cepService.consultarCep(clienteAtualizado.getCliente().getCep());
//                if (enderecoCep != null) {
//                    clienteInfo.setEndereco(enderecoCep.getLogradouro());
//                    clienteInfo.setBairro(enderecoCep.getBairro());
//                    clienteInfo.setCidade(enderecoCep.getLocalidade());
//                    clienteInfo.setUf(enderecoCep.getUf());
//                } else {
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//                }
//            }
//
//            ClientePF clienteAtualizadoEntity = clientePFRepository.save(clienteExistente);
//            return ResponseEntity.ok(clienteAtualizadoEntity);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    public ResponseEntity<Void> deletarClientePF(Long clienteId) {
        if (clientePFRepository.existsById(clienteId)) {
            clientePFRepository.deleteById(clienteId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


