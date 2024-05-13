package com.app.servicos.mapper;

import com.app.servicos.dto.response.ClientePFResponseDTO;
import com.app.servicos.dto.response.ClientePJResponseDTO;
import com.app.servicos.dto.response.OrdemResponseDTO;
import com.app.servicos.entity.ClientePF;
import com.app.servicos.entity.ClientePJ;
import com.app.servicos.entity.OrdemDeServico;

public class OrdemServicoMapper {

    public static OrdemResponseDTO mapToDto(OrdemDeServico ordem) {
        OrdemResponseDTO dto = new OrdemResponseDTO();
        dto.setId(ordem.getId());
        dto.setDescricaoServico(ordem.getDescricaoServico());
        dto.setTipoCliente(ordem.getTipoCliente());
        dto.setStatusServico(ordem.getStatusServico());
        dto.setCustoTotal(ordem.getCustoTotal());

        switch (ordem.getTipoCliente()) {
            case PF:
                if (ordem.getClientePF() != null) {
                    mapClientePF(ordem.getClientePF(), dto);
                }
                break;
            case PJ:
                if (ordem.getClientePJ() != null) {
                    mapClientePJ(ordem.getClientePJ(), dto);
                }
                break;
        }

        return dto;
    }

    private static void mapClientePF(ClientePF clientePF, OrdemResponseDTO dto) {
        dto.setEmail(clientePF.getEmail());
        dto.setTelefone(clientePF.getTelefone());

        ClientePFResponseDTO clientePFResponseDTO = new ClientePFResponseDTO();
        clientePFResponseDTO.setId(clientePF.getId());
        clientePFResponseDTO.setNome(clientePF.getNome());
        clientePFResponseDTO.setCpf(clientePF.getCpf());

        dto.setClientePF(clientePFResponseDTO);
    }

    private static void mapClientePJ(ClientePJ clientePJ, OrdemResponseDTO dto) {
        dto.setEmail(clientePJ.getEmail());
        dto.setTelefone(clientePJ.getTelefone());

        ClientePJResponseDTO clientePJResponseDTO = new ClientePJResponseDTO();
        clientePJResponseDTO.setId(clientePJ.getId());
        clientePJResponseDTO.setCnpj(clientePJ.getCnpj());
        clientePJResponseDTO.setRazaoSocial(clientePJ.getRazaoSocial());

        dto.setClientePJ(clientePJResponseDTO);
    }
}
