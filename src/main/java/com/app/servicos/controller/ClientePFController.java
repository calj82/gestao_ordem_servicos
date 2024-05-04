package com.app.servicos.controller;

import com.app.servicos.entity.ClientePF;
import com.app.servicos.service.ClientePFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes/pf")
public class ClientePFController {

    private final ClientePFService clientePFService;

    @Autowired
    public ClientePFController(ClientePFService clientePFService) {
        this.clientePFService = clientePFService;
    }

    @PostMapping
    public ResponseEntity<ClientePF> criarCliente(@RequestBody ClientePF cliente) {
        return clientePFService.criarClientePF(cliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientePF> buscarCliente(@PathVariable Long id) {
        return clientePFService.buscarClientePF(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientePF> atualizarCliente(@PathVariable Long id, @RequestBody ClientePF clienteAtualizado) {
        return clientePFService.atualizarClientePF(id, clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        return clientePFService.deletarClientePF(id);
    }
}

