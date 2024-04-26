package com.app.servicos.service;

import com.app.servicos.entity.OrdemDeServico;
import com.app.servicos.repository.OrdemDeServicoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalculadoraCustoService {

    private final OrdemDeServicoRepository ordemDeServicoRepository;

    public CalculadoraCustoService(OrdemDeServicoRepository ordemDeServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
    }

    public double calcularCustoTotal(Long ordemDeServicoId) {
        Optional<OrdemDeServico> ordemDeServicoOptional = ordemDeServicoRepository.findById(ordemDeServicoId);
        if (ordemDeServicoOptional.isEmpty()) {
            throw new IllegalArgumentException("Ordem de serviço não encontrada com ID: " + ordemDeServicoId);
        }

        OrdemDeServico ordemDeServico = ordemDeServicoOptional.get();
        double custoServico = ordemDeServico.getCustoServico();
        double custoAdicional = ordemDeServico.getCustoAdicional();

        double custoTotal = custoServico + custoAdicional;

        ordemDeServico.setCustoTotal(custoTotal);
        ordemDeServicoRepository.save(ordemDeServico);

        return custoTotal;
    }
}

