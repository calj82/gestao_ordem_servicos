package com.app.servicos.controller;

import com.app.servicos.entity.OrdemDeServico;
import com.app.servicos.enums.TipoCliente;
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

    @PostMapping("/criar/{clienteId}")
    public ResponseEntity<OrdemDeServico> criarOrdemDeServicoComCliente(@PathVariable Long clienteId, @RequestParam TipoCliente tipoCliente,
                                                                        @RequestBody OrdemDeServico ordemDeServico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemDeServicoService.criarOrdemDeServico(clienteId, tipoCliente, ordemDeServico));
    }
    @GetMapping
    public ResponseEntity<List<OrdemDeServico>> listarOrdensDeServico() {
        return ordemDeServicoService.listarOrdensDeServico()
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServico> buscarOrdemDeServicoPorId(@PathVariable Long id) {
        Optional<OrdemDeServico> ordemDeServico = ordemDeServicoService.buscarOrdemDeServicoPorId(id);
        return ordemDeServico.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemDeServico> atualizarOrdemDeServico(@PathVariable Long id, @RequestBody OrdemDeServico ordemDeServicoAtualizada) {
        Optional<OrdemDeServico> ordemDeServico = ordemDeServicoService.atualizarOrdemDeServico(id, ordemDeServicoAtualizada);
        return ordemDeServico.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirOrdemDeServico(@PathVariable Long id) {
        ordemDeServicoService.excluirOrdemDeServico(id);
        return ResponseEntity.noContent().build();
    }

}


