-- BIBLIOTECA COMUNITÁRIA PÁGINA VIRADA
-- Carga de dados fictícios para demonstração
-- Banco de dados: PostgreSQL

BEGIN;

-- 1. LEITORES

INSERT INTO leitores (
    nome,
    email,
    telefone,
    cidade,
    data_cadastro,
    ativo
)
VALUES
    ('Alice Monteiro', 'alice@example.com', '11900000001', 'Itaquaquecetuba', '2026-07-01 09:00:00', true),

    ('Bruno Valente', 'bruno@example.com', '11900000002', 'São Paulo', '2026-07-02 10:00:00', true),

    ('Clara Ventura', 'clara@example.com', '11900000003', 'Suzano', '2026-07-03 11:00:00', true),

    ('Daniel Azevedo', 'daniel@example.com', '11900000004', 'Mogi das Cruzes', '2026-07-04 14:00:00', true),

    ('Elisa Campos', 'elisa@example.com', '11900000005', 'Itaquaquecetuba', '2026-07-05 15:00:00', true),

    ('Felipe Amaral', 'felipe@example.com', '11900000006', 'São Paulo', '2026-07-06 09:30:00', true),

    ('Gabriela Fontes', 'gabriela@example.com', '11900000007', 'Suzano', '2026-07-07 10:30:00', true),

    ('Henrique Duarte', 'henrique@example.com', '11900000008', 'Mogi das Cruzes', '2026-07-08 13:00:00', false);




-- 2. LIVROS

INSERT INTO livros (
    titulo,
    autor,
    categoria,
    isbn,
    ativo
)
VALUES
    ('Entre Cartas e Estrelas', 'Laura Vilar', 'LITERATURA', '9788500000101', true),

    ('O Segredo das Estações', 'Rafael Norato', 'LITERATURA', '9788500000102', true),

    ('Programando Novos Caminhos', 'Beatriz Lume', 'TECNICO', '9788500000103', true),

    ('O Jardim das Nuvens', 'Sofia Venturi', 'INFANTIL', '9788500000104', true),

    ('As Aventuras de Pixel', 'Caio Belmonte', 'QUADRINHOS', '9788500000105', true),

    ('Universo dos Algoritmos', 'Pedro Alvor', 'TECNICO', '9788500000106', false);



-- 3. EMPRESTIMOS


INSERT INTO emprestimos (
    leitor_id,
    livro_id,
    data_emprestimo,
    data_prevista,
    data_devolucao,
    status
)
VALUES

    -- AGOSTO
    (1, 1, '2026-08-01', '2026-08-15', '2026-08-12', 'DEVOLVIDO'),
    (2, 2, '2026-08-03', '2026-08-17', '2026-08-19', 'DEVOLVIDO'),
    (3, 3, '2026-08-05', '2026-08-20', '2026-08-18', 'DEVOLVIDO'),
    (4, 4, '2026-08-07', '2026-08-21', NULL, 'CANCELADO'),
    (5, 5, '2026-08-09', '2026-08-23', '2026-08-25', 'DEVOLVIDO'),
    (6, 1, '2026-08-16', '2026-08-30', '2026-08-28', 'DEVOLVIDO'),
    (7, 2, '2026-08-21', '2026-09-04', '2026-09-03', 'DEVOLVIDO'),
    (1, 3, '2026-08-24', '2026-09-07', NULL, 'CANCELADO'),

    -- SETEMBRO
    (2, 4, '2026-09-01', '2026-09-15', '2026-09-14', 'DEVOLVIDO'),
    (3, 5, '2026-09-03', '2026-09-17', '2026-09-20', 'DEVOLVIDO'),
    (4, 1, '2026-09-05', '2026-09-19', '2026-09-18', 'DEVOLVIDO'),
    (5, 2, '2026-09-07', '2026-09-21', NULL, 'CANCELADO'),
    (6, 3, '2026-09-09', '2026-09-23', '2026-09-25', 'DEVOLVIDO'),
    (7, 4, '2026-09-17', '2026-10-01', '2026-09-29', 'DEVOLVIDO'),
    (1, 5, '2026-09-22', '2026-10-06', '2026-10-05', 'DEVOLVIDO'),
    (2, 1, '2026-09-25', '2026-10-09', '2026-10-11', 'DEVOLVIDO'),

    -- OUTUBRO
    (3, 2, '2026-10-02', '2026-10-16', '2026-10-14', 'DEVOLVIDO'),
    (4, 3, '2026-10-04', '2026-10-18', NULL, 'CANCELADO'),
    (5, 4, '2026-10-06', '2026-10-20', '2026-10-22', 'DEVOLVIDO'),
    (6, 5, '2026-10-08', '2026-10-22', '2026-10-20', 'DEVOLVIDO'),
    (7, 1, '2026-10-13', '2026-10-27', '2026-10-26', 'DEVOLVIDO'),
    (1, 2, '2026-10-18', '2026-11-01', '2026-10-30', 'DEVOLVIDO'),
    (2, 3, '2026-10-22', '2026-11-05', '2026-11-07', 'DEVOLVIDO'),
    (3, 4, '2026-10-25', '2026-11-08', NULL, 'CANCELADO'),

    -- NOVEMBRO
    (4, 5, '2026-11-01', '2026-11-15', '2026-11-13', 'DEVOLVIDO'),
    (5, 1, '2026-11-03', '2026-11-17', NULL, 'ABERTO'),
    (6, 2, '2026-11-05', '2026-11-19', '2026-11-18', 'DEVOLVIDO'),
    (7, 3, '2026-11-10', '2026-11-24', NULL, 'ABERTO'),
    (1, 4, '2026-11-15', '2026-11-29', NULL, 'ABERTO'),
    (2, 5, '2026-11-20', '2026-12-04', NULL, 'ABERTO');

COMMIT;