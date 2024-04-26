package com.app.servicos.service;

import com.app.servicos.entity.Clientes;
import com.app.servicos.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CepService cepService;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, CepService cepService) {
        this.clienteRepository = clienteRepository;
        this.cepService = cepService;
    }

    public Clientes criarCliente(Clientes cliente) {
        return clienteRepository.save(cliente);
    }

    public ResponseEntity<Clientes> atualizarEnderecoCliente(Long clienteId, String cep) {
        Clientes cliente = clienteRepository.findById(clienteId).orElse(null);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        EnderecoCep enderecoCep = cepService.consultarCep(cep);

        if (enderecoCep != null) {
            cliente.setEnderecoCliente(enderecoCep.getLogradouro() + ", " + enderecoCep.getBairro() + ", " + enderecoCep.getLocalidade() + " - " + enderecoCep.getUf());
            cliente.setCepCliente(cep);
            clienteRepository.save(cliente);
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Clientes> criarClienteConsultaCep(Clientes cliente) {
        String cepCliente = cliente.getCepCliente();
        if (cepCliente != null && !cepCliente.isEmpty()) {
            EnderecoCep enderecoCep = cepService.consultarCep(cepCliente);

            if (enderecoCep != null) {
                cliente.setEnderecoCliente(enderecoCep.getLogradouro() + ", " + enderecoCep.getBairro() + ", " + enderecoCep.getLocalidade() + " - " + enderecoCep.getUf());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        Clientes novoCliente = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    public List<Clientes> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Clientes> buscarClientePorId(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }

    public ResponseEntity<Clientes> atualizarCliente(Long clienteId, Clientes clienteAtualizado) {
        Optional<Clientes> clienteExistente = clienteRepository.findById(clienteId);

        if (clienteExistente.isPresent()) {
            Clientes cliente = clienteExistente.get();
            cliente.setNomeCliente(clienteAtualizado.getNomeCliente());
            cliente.setCpfCliente(clienteAtualizado.getCpfCliente());
            cliente.setEnderecoCliente(clienteAtualizado.getEnderecoCliente());
            cliente.setEnderecoNumero(cliente.getEnderecoNumero());

            Clientes clienteAtualizadoEntity = clienteRepository.save(cliente);
            return ResponseEntity.ok(clienteAtualizadoEntity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> excluirCliente(Long clienteId) {
        if (clienteRepository.existsById(clienteId)) {
            clienteRepository.deleteById(clienteId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

