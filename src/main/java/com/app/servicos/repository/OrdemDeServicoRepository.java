package com.app.servicos.repository;

import com.app.servicos.entity.OrdemDeServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, Long> {

    //Futuro: quando quiser encontrar todas as ordens de serviço com um determinado status.
//    List<OrdemDeServico> findByStatus(String status);

}
