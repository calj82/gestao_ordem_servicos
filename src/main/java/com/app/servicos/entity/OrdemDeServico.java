package com.app.servicos.entity;

import com.app.servicos.enums.StatusServico;
import com.app.servicos.enums.TipoCliente;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ordem_servico")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdemDeServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao_servico")
    private String descricaoServico;

    @Column(name = "data_servico")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataServico;

    @Column(name =  "custo_servico")
    private BigDecimal custoServico;

    @Column(name =  "custo_adicional")
    private BigDecimal custoAdicional;

    @Column(name =  "custo_total")
    private BigDecimal custoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente")
    private TipoCliente tipoCliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_servico")
    private StatusServico statusServico = StatusServico.ABERTO;

    @ManyToOne
    @JoinColumn(name = "cliente_pj_id")
    private ClientePJ clientePJ;

    @ManyToOne
    @JoinColumn(name = "cliente_pf_id")
    private ClientePF clientePF;

}
