package com.app.servicos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente_pf")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientePF extends Cliente{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "data_nascimento")
    private String dataNascimento;

    @Column(name = "idade")
    private String idade;

    @OneToMany(mappedBy = "clientePF", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<OrdemDeServico> ordensDeServico = new ArrayList<>();
}
