package com.app.servicos.dto.response;

import com.app.servicos.enums.StatusServico;
import com.app.servicos.enums.TipoCliente;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class OrdemResponseDTO extends ClienteResponseDTO {

    private long id;

    private String descricaoServico;

    private TipoCliente tipoCliente;

    private StatusServico statusServico;

    private BigDecimal custoTotal;

    private ClientePJResponseDTO clientePJ;

    private ClientePFResponseDTO clientePF;

}


