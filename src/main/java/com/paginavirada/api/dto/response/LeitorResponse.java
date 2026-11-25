package com.paginavirada.api.dto.response;

import com.paginavirada.entity.Leitor;

import java.time.LocalDateTime;

public record LeitorResponse(

        Integer id,
        String nome,
        String email,
        String telefone,
        String cidade,
        LocalDateTime dataCadastro,
        Boolean ativo

) {

    public LeitorResponse(Leitor leitor) {
        this(
                leitor.getId(),
                leitor.getNome(),
                leitor.getEmail(),
                leitor.getTelefone(),
                leitor.getCidade(),
                leitor.getDataCadastro(),
                leitor.getAtivo()
        );
    }

}
