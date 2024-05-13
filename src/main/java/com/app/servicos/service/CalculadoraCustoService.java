package com.app.servicos.service;

import com.app.servicos.entity.OrdemDeServico;
import com.app.servicos.repository.OrdemDeServicoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CalculadoraCustoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;

    public CalculadoraCustoService(OrdemDeServicoRepository ordemDeServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
    }

    public BigDecimal calcularCustoTotal(Long ordemDeServicoId) {
        Optional<OrdemDeServico> ordemDeServicoOptional = ordemDeServicoRepository.findById(ordemDeServicoId);
        if (ordemDeServicoOptional.isEmpty()) {
            throw new IllegalArgumentException("Ordem de serviço não encontrada com ID: " + ordemDeServicoId);
        }

        OrdemDeServico ordemDeServico = ordemDeServicoOptional.get();
        var custoServico = ordemDeServico.getCustoServico();
        var custoAdicional = ordemDeServico.getCustoAdicional();

        var custoTotal = custoServico.add(custoAdicional);

        ordemDeServico.setCustoTotal(custoTotal);
        ordemDeServicoRepository.save(ordemDeServico);

        return custoTotal;
    }
}

