package com.app.servicos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class Cliente {

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Embedded
    private Endereco endereco;
}
