package com.app.servicos.controller;

import com.app.servicos.entity.OrdemDeServico;
import com.app.servicos.repository.OrdemDeServicoRepository;
import com.app.servicos.service.OrdemDeServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ordensdeservico")
public class OrdemDeServicoController {

    private final OrdemDeServicoService ordemDeServicoService;

    @Autowired
    public OrdemDeServicoController(OrdemDeServicoService ordemDeServicoService) {
        this.ordemDeServicoService = ordemDeServicoService;
    }

    @PostMapping("/criar-ordem-cliente/{clienteId}")
    public ResponseEntity<OrdemDeServico> criarOrdemDeServicoComCliente(@PathVariable Long clienteId, @RequestBody OrdemDeServico ordemDeServico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemDeServicoService.criarOrdemDeServicoComCliente(clienteId, ordemDeServico));
    }

    @GetMapping
    public List<OrdemDeServico> listarOrdensDeServico() {
        return ordemDeServicoService.listarOrdensDeServico();
    }

    @GetMapping("/{ordemDeServicoId}")
    public ResponseEntity<OrdemDeServico> buscarOrdemDeServicoPorId(@PathVariable Long ordemDeServicoId) {
        Optional<OrdemDeServico> ordemDeServico = ordemDeServicoService.buscarOrdemDeServicoPorId(ordemDeServicoId);
        return ordemDeServico.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/criar-ordem")
    public ResponseEntity<OrdemDeServico> criarOrdemDeServico(@RequestBody OrdemDeServico ordemDeServico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemDeServicoService.criarOrdemDeServico(ordemDeServico));
    }

    @PutMapping("/{ordemDeServicoId}")
    public ResponseEntity<OrdemDeServico> atualizarOrdemDeServico(@PathVariable Long ordemDeServicoId, @RequestBody OrdemDeServico ordemDeServicoAtualizada) {
        Optional<OrdemDeServico> ordemDeServico = ordemDeServicoService.
                atualizarOrdemDeServico(ordemDeServicoId, ordemDeServicoAtualizada);
        return ordemDeServico.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{ordemDeServicoId}")
    public ResponseEntity<Void> excluirOrdemDeServico(@PathVariable Long ordemDeServicoId) {
        ordemDeServicoService.excluirOrdemDeServico(ordemDeServicoId);
        return ResponseEntity.noContent().build();
    }

}


