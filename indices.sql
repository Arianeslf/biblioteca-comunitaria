CREATE UNIQUE INDEX IF NOT EXISTS uk_emprestimo_livro_aberto
    ON emprestimos (livro_id)
    WHERE status = 'ABERTO';