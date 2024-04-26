package com.app.servicos.service;

import lombok.Data;

@Data
public class EnderecoCep {

    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
}
