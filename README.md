# Votação API

API REST para gerenciamento de sessões de votação em assembleias cooperativistas. Permite cadastrar pautas, abrir sessões com tempo configurável, registrar votos de associados e apurar resultados.

O foco do projeto está na comunicação entre o backend e um aplicativo mobile, utilizando o padrão **Server-Driven UI (SDUI)** — o servidor retorna JSONs que descrevem as telas, e o cliente as renderiza dinamicamente.

---

## Stack

- **Java 17** + **Spring Boot 3.2.4**
- **Spring Data JPA** + **PostgreSQL 15**
- **Spring Cloud OpenFeign** — integração com API externa de validação de CPF
- **WireMock** — mock da API de CPF para testes locais
- **springdoc-openapi** — Swagger UI
- **Gradle** — build tool
- **Docker / Docker Compose** — containerização

---

## Arquitetura

O projeto segue **Arquitetura Hexagonal (Ports & Adapters)**:

```
src/main/java/br/com/sicredi/votacao/
├── domain/
│   ├── model/          # Entidades e value objects do domínio
│   └── exception/      # Exceções de negócio
├── application/
│   ├── usecase/        # Casos de uso (regras de negócio)
│   └── ports/          # Interfaces (portas de entrada e saída)
├── infra/
│   ├── adapters/
│   │   ├── in/web/     # Controllers REST (adaptadores de entrada)
│   │   └── out/        # Repositórios JPA, clients Feign (adaptadores de saída)
│   └── config/         # Configurações Spring (Feign, OpenAPI, handlers)
└── util/               # Utilitários (ex: gerador de CPF para testes)
```

---

## Pré-requisitos

- Docker e Docker Compose
- Java 17+ (apenas para rodar fora do Docker)
- k6 ou Docker (para os testes de performance)

---

## Como executar

### Opção 1 — Docker Compose (mais rápido)

Sobe tudo com um único comando: API, PostgreSQL e WireMock.

```bash
docker compose up --build
```

| Serviço | Endereço |
|---|---|
| API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| PostgreSQL | `localhost:5432` |
| WireMock (mock CPF) | `http://localhost:8081` |

---

### Opção 2 — IntelliJ IDEA

1. **Importe o projeto** como projeto Gradle: `File > Open` e selecione a pasta raiz (onde está o `build.gradle`)

2. **Suba a infraestrutura** (PostgreSQL e WireMock) via Docker:
   ```bash
   docker compose up postgres wiremock -d
   ```

3. **Configure a Run Configuration:**
   - Vá em `Run > Edit Configurations`
   - Adicione uma `Spring Boot` configuration apontando para `VotacaoApiApplication`
   - Em `Active profiles`, coloque: `local`
   - Ou adicione em `Environment variables`: `SPRING_PROFILES_ACTIVE=local`

4. **Rode a aplicação** com `Shift+F10` ou pelo botão de play

A API estará disponível em `http://localhost:8080`.

---

### Opção 3 — Terminal com Gradle

```bash
# Sobe só a infraestrutura
docker compose up postgres wiremock -d

# Roda a aplicação com perfil local
./gradlew bootRun --args='--spring.profiles.active=local'
```

---

### Opção 4 — Build do JAR

```bash
# Gera o JAR ignorando os testes
./gradlew bootJar -x test

# Executa
java -jar build/libs/votacao-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

---

## Testes unitários

Os testes usam banco H2 em memória — não precisa de nada rodando.

```bash
# Roda todos os testes
./gradlew test

# Roda e gera relatório de cobertura
./gradlew test jacocoTestReport
```

O relatório HTML fica em `build/reports/jacoco/test/html/index.html`.

---

## Teste de performance (k6)

O script `performance-test/load-test.js` simula múltiplos usuários votando simultaneamente (10 VUs por 10 segundos por padrão).

### Antes de rodar

O script aponta para um `sessaoId` fixo. Você precisa:

1. Criar uma sessão via API (veja a seção de endpoints abaixo)
2. Copiar o `id` retornado
3. Substituir o valor na linha 10 do arquivo `performance-test/load-test.js`:

```js
const sessaoId = "cole-aqui-o-id-da-sessao"
```

Crie uma sessão com duração suficiente para o teste terminar — por exemplo, 5 minutos:

```bash
curl -X POST http://localhost:8080/v1/sessoes \
  -H "Content-Type: application/json" \
  -d '{"descricao": "Sessão para teste de carga", "duracaoMinutos": 5}'
```

### Rodando com Docker (sem instalar o k6)

```bash
# Linux
docker run --rm -i --network=host grafana/k6 run - < performance-test/load-test.js

# Mac / Windows (Docker Desktop)
docker run --rm -i grafana/k6 run - < performance-test/load-test.js
```

---

## Endpoints da API

A API está versionada sob o prefixo `/v1`. A documentação interativa completa está disponível em `http://localhost:8080/swagger-ui.html`.

### Sessões

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/v1/sessoes/nova` | Retorna tela SDUI para criar nova sessão |
| `POST` | `/v1/sessoes` | Cria pauta e abre sessão de votação |
| `GET` | `/v1/sessoes/{id}/resultado` | Retorna resultado da votação |

### Votos

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/v1/sessoes/{id}/votar` | Retorna tela SDUI de votação |
| `POST` | `/v1/sessoes/{sessaoId}/votos` | Registra voto do associado |

### Exemplos

**Criar sessão:**

```http
POST /v1/sessoes
Content-Type: application/json

{
  "descricao": "Aprovação do orçamento anual",
  "duracaoMinutos": 5
}
```

O campo `duracaoMinutos` é opcional — default é 1 minuto.

**Registrar voto:**

```http
POST /v1/sessoes/{sessaoId}/votos
Content-Type: application/json

{
  "valor": "SIM"
}
```

Valores aceitos: `SIM` ou `NAO`. O CPF do associado é gerado pelo servidor — cada chamada representa um associado único.

**Consultar resultado:**

```http
GET /v1/sessoes/{id}/resultado
```

```json
{
  "pautaId": "abc-123",
  "totalSim": 10,
  "totalNao": 3
}
```

---

## Variáveis de ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `local` | Perfil ativo (`local` ou `prod`) |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/votacao_db` | URL do banco |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | `password` | Senha do banco |
| `API_CPF_REAL_URL` | `https://user-info.herokuapp.com/users` | URL da API de validação de CPF |
| `API_CPF_MOCK_URL` | `http://localhost:8081/users` | URL do WireMock (mock local) |

A variável `API_CPF_REAL_URL` (e `API_CPF_MOCK_URL`) é configurável justamente para facilitar testes com emuladores Android ou dispositivos físicos — troque `localhost` pelo IP da máquina ou pelo endereço acessível na rede.

---

## Tarefas bônus implementadas

### Bônus 1 — Integração com API de CPF

Antes de registrar um voto, o sistema consulta a API externa para verificar se o associado está habilitado:

```
GET https://user-info.herokuapp.com/users/{cpf}
```

- `ABLE_TO_VOTE` → voto permitido
- `UNABLE_TO_VOTE` → voto bloqueado
- `404` → CPF inválido

Se a API externa estiver fora do ar (timeout, erro de rede), o sistema adota um **fallback permissivo** — o voto é permitido para não comprometer a disponibilidade da sessão. Para testes locais, o WireMock simula esse comportamento via mapeamentos em `wiremock/mappings/`.

### Bônus 2 — Performance

Script de carga em `performance-test/load-test.js` usando k6. Veja a seção de testes de performance acima.

### Bônus 3 — Versionamento da API

Versionamento por prefixo de URL (`/v1/`). Escolhi essa estratégia por ser explícita em logs e gateways, compatível com clientes mobile (um app desatualizado continua funcionando enquanto migra para `/v2/`) e simples de implementar sem necessidade de headers customizados.

---

## Formato SDUI

As telas retornadas pela API seguem o padrão Server-Driven UI consumido pelo app mobile.

**Tela `FORMULARIO`** — exibe campos de entrada com botões de ação:

```json
{
  "tipo": "FORMULARIO",
  "titulo": "Nova Sessão de Votação",
  "itens": [
    { "tipo": "INPUT_TEXTO", "id": "descricao", "titulo": "Descrição da pauta", "valor": "" },
    { "tipo": "INPUT_NUMERO", "id": "duracaoMinutos", "titulo": "Duração (minutos)", "valor": 1 }
  ],
  "botaoOk": {
    "texto": "Abrir Sessão",
    "url": "http://localhost:8080/v1/sessoes",
    "body": {}
  },
  "botaoCancelar": {
    "texto": "Cancelar",
    "url": "http://localhost:8080/v1"
  }
}
```

**Tela `SELECAO`** — exibe lista de opções clicáveis:

```json
{
  "tipo": "SELECAO",
  "titulo": "Seu voto",
  "itens": [
    { "texto": "Sim", "url": "http://localhost:8080/v1/sessoes/{sessaoId}/votos", "body": { "valor": "SIM" } },
    { "texto": "Não", "url": "http://localhost:8080/v1/sessoes/{sessaoId}/votos", "body": { "valor": "NAO" } }
  ]
}
```

---

## Estrutura do projeto

```
votacao-api/
├── src/
│   ├── main/
│   │   ├── java/br/com/sicredi/votacao/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-local.yml   # Perfil de desenvolvimento
│   │       └── application-prod.yml    # Perfil Docker/produção
│   └── test/
├── performance-test/
│   └── load-test.js        # Script k6 de teste de carga
├── wiremock/
│   └── mappings/           # Mapeamentos mock da API de CPF
├── Dockerfile
├── docker-compose.yml
├── build.gradle
└── README.md
```
