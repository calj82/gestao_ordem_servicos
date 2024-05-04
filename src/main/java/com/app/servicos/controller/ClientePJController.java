package com.app.servicos.controller;

import com.app.servicos.entity.ClientePJ;
import com.app.servicos.service.ClientePJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clientes/pj")
public class ClientePJController {

    private final ClientePJService clientePJService;

    @Autowired
    public ClientePJController(ClientePJService clientePJService) {
        this.clientePJService = clientePJService;
    }

    @PostMapping
    public ResponseEntity<ClientePJ> criarCliente(@RequestBody ClientePJ cliente) {
        return clientePJService.criarClientePJ(cliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientePJ> buscarCliente(@PathVariable Long id) {
        return clientePJService.buscarClientePJ(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientePJ> atualizarCliente(@PathVariable Long id, @RequestBody ClientePJ clienteAtualizado) {
        return clientePJService.atualizarClientePJ(id, clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        return clientePJService.deletarClientePJ(id);
    }
}
