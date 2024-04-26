package com.app.servicos.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "clientes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_cliente")
    private String nomeCliente;
    @Column(name = "cpf_cliente")
    private String cpfCliente;
    @Column(name = "endereco_cliente")
    private String enderecoCliente;
    @Column(name = "endereco_numero")
    private Integer enderecoNumero;
    @Column(name = "complemento")
    private String complemento;
    @Column(name = "cep_cliente")
    private String cepCliente;

}
