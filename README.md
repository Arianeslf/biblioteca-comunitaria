# Página Virada — API REST para Biblioteca Comunitária

API REST em Java 21 e Spring Boot 4 para cadastro de leitores,
catálogo de livros e controle de empréstimos, com persistência
em PostgreSQL.

## Tecnologias

- Java 21 e Spring Boot 4.0.10
- Spring Web MVC, Spring Data JPA, Bean Validation, Hibernate e Lombok
- PostgreSQL 15; Docker Compose para o banco local
- Maven Wrapper para execução

## Repositório

https://github.com/Arianeslf/biblioteca-comunitaria

Para clonar o projeto:

```bash
git clone https://github.com/Arianeslf/biblioteca-comunitaria.git
cd biblioteca-comunitaria
```

## Pré-requisitos

Java 21 e Docker com Docker Compose, ou uma instância
PostgreSQL acessível.

Execute os comandos na pasta raiz do projeto.

## Configuração e execução

Inicie o banco de dados, caso utilize Docker:

```bash
docker compose up -d
docker compose ps
```

O arquivo `docker-compose.yml` configura automaticamente
o banco `postgres`, com usuário `postgres`, senha `postgres`
e porta `5432`.

As credenciais são destinadas exclusivamente ao ambiente
local de desenvolvimento.

As configurações de conexão encontram-se no arquivo
`src/main/resources/application.properties`.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Caso utilize uma instância externa do PostgreSQL, ajuste
os parâmetros de conexão conforme o ambiente e certifique-se
de que o banco informado existe.

Inicie a aplicação:

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

A API estará disponível em http://localhost:8080.

A configuração `spring.jpa.hibernate.ddl-auto=update`
cria e atualiza as tabelas automaticamente.

Não é necessário criar o banco manualmente ao utilizar
a configuração padrão do Docker Compose.

O banco utiliza um volume persistente. O comando
`docker compose down` encerra os containers sem excluir
os registros. Não utilize a opção `-v` caso deseje
preservar os dados.

## Entidades e relacionamentos

- `leitores`: id, nome, email (único), telefone, cidade,
  data_cadastro e ativo.
- `livros`: id, titulo, autor, categoria, isbn (único) e ativo.
- `emprestimos`: id, leitor_id (FK), livro_id (FK),
  data_emprestimo, data_prevista, data_devolucao e status.

Cada empréstimo pertence a um leitor e a um livro.

Categorias aceitas: LITERATURA, TECNICO, INFANTIL e QUADRINHOS.

Status aceitos: ABERTO, DEVOLVIDO e CANCELADO.

## Regras de negócio

- E-mail do leitor e ISBN do livro devem ser únicos (409).
- Leitores e livros inativos não podem participar de novos
  empréstimos (409).
- Um livro não pode possuir dois empréstimos abertos
  simultaneamente (409).
- Todo empréstimo nasce ABERTO.
- Somente as transições ABERTO → DEVOLVIDO ou
  ABERTO → CANCELADO são permitidas (409).
- O prazo previsto deve estar entre 0 e 30 dias (400).
- A devolução exige uma data igual ou posterior à data
  do empréstimo (400).
- O cancelamento não registra data de devolução.
- Registros inexistentes retornam 404.
- As demais restrições são verificadas com Bean Validation.

## Endpoints (12 previstos no desafio)

| Método | Endpoint | Objetivo |
|---|---|---|
| POST | `/leitores` | Cadastrar leitor (201) |
| GET | `/leitores` | Listar e filtrar leitores (200) |
| GET | `/leitores/{id}` | Consultar leitor (200/404) |
| PUT | `/leitores/{id}` | Atualizar leitor (200) |
| POST | `/livros` | Cadastrar livro (201) |
| GET | `/livros` | Listar e filtrar livros (200) |
| GET | `/livros/{id}` | Consultar livro (200/404) |
| PUT | `/livros/{id}` | Atualizar livro (200) |
| POST | `/emprestimos` | Registrar empréstimo (201) |
| GET | `/emprestimos` | Listar e filtrar empréstimos (200) |
| GET | `/emprestimos/{id}` | Consultar empréstimo (200/404) |
| PATCH | `/emprestimos/{id}/status` | Devolver ou cancelar (200) |

As requisições com corpo e as respostas utilizam JSON.
Os campos seguem a convenção camelCase e as colunas do
banco utilizam snake_case.

As datas dos empréstimos utilizam o formato `yyyy-MM-dd`.
A data de cadastro do leitor utiliza LocalDateTime.

## Exemplos de requisições

POST /leitores:

```json
{
  "nome": "Livia Avelar",
  "email": "livia@example.com",
  "telefone": "11900000009",
  "cidade": "Suzano"
}
```

PUT /leitores/1:

```json
{
  "nome": "Alice Monteiro",
  "email": "alice@example.com",
  "telefone": "11900000001",
  "cidade": "Itaquaquecetuba",
  "ativo": true
}
```

POST /livros:

```json
{
  "titulo": "Caminhos da Imaginação",
  "autor": "Luisa Ventar",
  "categoria": "LITERATURA",
  "isbn": "9788500000107"
}
```

PUT /livros/1:

```json
{
  "titulo": "Entre Cartas e Estrelas",
  "autor": "Laura Vilar",
  "categoria": "LITERATURA",
  "isbn": "9788500000101",
  "ativo": true
}
```

POST /emprestimos:

```json
{
  "leitorId": 1,
  "livroId": 2,
  "dataEmprestimo": "2026-11-25",
  "dataPrevista": "2026-12-09"
}
```

PATCH /emprestimos/{id}/status:

```json
{
  "status": "DEVOLVIDO",
  "dataDevolucao": "2026-11-26"
}
```

Para cancelar:

```json
{
  "status": "CANCELADO"
}
```

Os exemplos pressupõem a existência dos registros
e o cumprimento das regras de negócio.

## Consultas, filtros e paginação

```http
GET /leitores?nome=alice&cidade=Itaquaquecetuba&page=0&size=20
GET /livros?titulo=cartas&categoria=LITERATURA&page=0&size=20
GET /emprestimos?status=ABERTO&leitorId=1&livroId=2
GET /emprestimos?dataInicio=2026-08-01&dataFim=2026-11-20
```

O parâmetro `page` possui valor padrão 0. O parâmetro
`size` possui valor padrão 20 e máximo 100.

As listagens utilizam a paginação padrão do Spring Data
(content, totalElements, totalPages, number e size).

Os filtros de datas são inclusivos e independentes.

## Respostas de erro

O tratamento das principais exceções é centralizado
pelo GlobalExceptionHandler.

- 400: dados inválidos.
- 404: registro inexistente.
- 409: conflito de regra de negócio.

As respostas apresentam o código HTTP e uma mensagem
identificando a regra violada. Erros de validação também
podem apresentar os campos inválidos.

## Índice único parcial

O arquivo `indices.sql` reforça a integridade dos
empréstimos diretamente no PostgreSQL.

Após iniciar a aplicação e gerar as tabelas, execute
em outro terminal:

```bash
docker compose cp ./indices.sql postgres:/tmp/indices.sql
docker compose exec postgres psql -U postgres -d postgres -v ON_ERROR_STOP=1 -f /tmp/indices.sql
```

## Dados de exemplo (dados.sql)

O arquivo contém 8 leitores, 6 livros e 30 empréstimos,
abrangendo quatro cidades, quatro categorias, três status
e um período de 111 dias. Todos os dados são fictícios.

O script foi elaborado para um banco recém-criado,
com tabelas vazias e identificadores iniciando em 1.

Após iniciar a aplicação, execute:

```bash
docker compose cp ./dados.sql postgres:/tmp/dados.sql
docker compose exec postgres psql -U postgres -d postgres -v ON_ERROR_STOP=1 -f /tmp/dados.sql
```

Caso utilize PostgreSQL sem Docker e possua o cliente psql:

```bash
psql -h localhost -p 5432 -U postgres -d postgres -v ON_ERROR_STOP=1 -f indices.sql
psql -h localhost -p 5432 -U postgres -d postgres -v ON_ERROR_STOP=1 -f dados.sql
```

O script utiliza BEGIN e COMMIT e não exclui registros
preexistentes.




## Exportação CSV

A aplicação disponibiliza um endpoint adicional para
exportação dos dados consolidados de leitores, livros
e empréstimos.

```http
GET /exportacoes/emprestimos
```

Para realizar o download, acesse:

http://localhost:8080/exportacoes/emprestimos

O arquivo `emprestimos.csv` é gerado em UTF-8 a partir
dos registros armazenados no PostgreSQL, contemplando
os dados iniciais e as inclusões e alterações realizadas
pela API.

Cada linha representa um empréstimo, com os respectivos
dados do leitor e do livro.

A exportação não possui paginação e disponibiliza os
dados em uma estrutura adequada para análise no
Google Looker Studio.

## Testes

As funcionalidades foram verificadas manualmente com
Postman, PowerShell e PostgreSQL.

| Cenário | Resultado esperado |
|---|---|
| Cadastro válido | 201 |
| E-mail ou ISBN duplicado | 409 |
| Registro inexistente | 404 |
| Campos obrigatórios inválidos | 400 |
| Leitor ou livro inativo | 409 |
| Livro com empréstimo aberto | 409 |
| Prazo superior a 30 dias | 400 |
| Devolução válida | 200 |
| Cancelamento válido | 200 |
| Alteração de empréstimo finalizado | 409 |
| Cadastro concorrente | 201 e 409 |
| Alteração concorrente de status | 200 e 409 |

Também foram realizadas verificações de paginação,
persistência, integridade do banco e concorrência.