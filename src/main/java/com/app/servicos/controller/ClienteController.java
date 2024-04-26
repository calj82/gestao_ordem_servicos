package com.app.servicos.controller;

import com.app.servicos.entity.Clientes;
import com.app.servicos.repository.ClienteRepository;
import com.app.servicos.service.CepService;
import com.app.servicos.service.ClienteService;
import com.app.servicos.service.EnderecoCep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping(value = "/atualizar-endereco/{clienteId}", params = "cep")
    public ResponseEntity<Clientes> atualizarEnderecoCliente(@PathVariable Long clienteId, @RequestParam String cep) {
        return clienteService.atualizarEnderecoCliente(clienteId, cep);
    }

    @GetMapping
    public List<Clientes> listarClientes() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Clientes> buscarClientePorId(@PathVariable Long clienteId) {
        Optional<Clientes> cliente = clienteService.buscarClientePorId(clienteId);
        return cliente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/criar-cliente")
    public ResponseEntity<Clientes> criarCliente(@RequestBody Clientes cliente) {
        Clientes novoCliente = clienteService.criarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @PostMapping("/criar-cliente-consultacep")
    public ResponseEntity<Clientes> criarClienteConsultaCep(@RequestBody Clientes cliente) {
        return clienteService.criarClienteConsultaCep(cliente);
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<Clientes> atualizarCliente(@PathVariable Long clienteId, @RequestBody Clientes clienteAtualizado) {
        return clienteService.atualizarCliente(clienteId, clienteAtualizado);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long clienteId) {
        return clienteService.excluirCliente(clienteId);
    }
}

