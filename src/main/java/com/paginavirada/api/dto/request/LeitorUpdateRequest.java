
package com.paginavirada.api.dto.request;

import jakarta.validation.constraints.*;

public record LeitorUpdateRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 3, max = 120,
                message = "O nome deve ter entre 3 e 120 caracteres")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        @Size(max = 255,
                message = "O e-mail deve ter até 255 caracteres")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "[0-9]{10,11}",
                message = "O telefone deve conter 10 ou 11 dígitos")
        String telefone,

        @NotBlank(message = "A cidade é obrigatória")
        @Size(min = 2, max = 80,
                message = "A cidade deve ter entre 2 e 80 caracteres")
        String cidade,

        @NotNull(message = "O status ativo é obrigatório")
        Boolean ativo

) {
}
