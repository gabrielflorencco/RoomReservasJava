# 🏢 RoomReservas — Java Spring

Sistema de reserva de salas construído com **Clean Architecture**, **Java 21**, **Spring Boot 3**, **Lombok**, **JPA** e **PostgreSQL**.

> Versão Java do projeto originalmente desenvolvido em C# .NET Core.

---

## 🏗️ Arquitetura

O projeto é dividido em **4 módulos Maven**:

```
roomreservas/
├── domain/           # Entidades, repositórios (interfaces), exceções de domínio
├── application/      # Use Cases, DTOs, regras de negócio
├── infrastructure/   # JPA, PostgreSQL, Flyway, adapters
└── presentation/     # Controllers REST, Swagger, Exception Handler
```

### Fluxo de dependências
```
presentation → application → domain
infrastructure → domain
presentation → infrastructure (para wiring Spring)
```

---

## 🗂️ Entidades

| Entidade   | Atributos principais                                                  |
|------------|-----------------------------------------------------------------------|
| `Usuario`  | id (UUID), nome, email (único)                                        |
| `Sala`     | id (UUID), nome, capacidade, ativa, valorDiaria                       |
| `Reserva`  | id (UUID), usuario, sala, inicio, fim, valorTotal, status, dataCriacao |

**Status da Reserva:** `PENDENTE` → `CONFIRMADA` / `CANCELADA` / `CONCLUIDA`

---

## ✅ Regras de Negócio

- **Conflito de horários**: impossível reservar sala já ocupada no período
- **Sala inativa**: não aceita novas reservas; ao desativar, cancela automaticamente reservas futuras ativas
- **Limite de reservas**: usuário pode ter no máximo **3 reservas ativas** (PENDENTE ou CONFIRMADA) simultâneas
- **Cancelamento com prazo**: somente permitido com **pelo menos 24h de antecedência**
- **Cálculo do valor total**: proporcional ao `valorDiaria` da sala, calculado por horas (`horas / 24 * valorDiaria`)
- **Duração mínima**: reserva deve ter ao menos 30 minutos

---

## 🚀 Como executar

### Apenas o banco (recomendado para desenvolvimento)

```bash
docker compose up postgres -d
```

### Aplicação + Banco completo

```bash
docker compose up -d
```

### Rodando localmente (sem Docker para a app)

```bash
# 1. Suba o PostgreSQL
docker compose up postgres -d

# 2. Build
mvn clean install -DskipTests

# 3. Execute
cd presentation
mvn spring-boot:run
```

---

## 📚 Endpoints

| Método | Rota                                  | Descrição                              |
|--------|---------------------------------------|----------------------------------------|
| POST   | `/api/v1/usuarios`                    | Criar usuário                          |
| GET    | `/api/v1/usuarios`                    | Listar todos os usuários               |
| GET    | `/api/v1/usuarios/{id}`               | Buscar usuário por ID                  |
| PUT    | `/api/v1/usuarios/{id}`               | Atualizar usuário                      |
| DELETE | `/api/v1/usuarios/{id}`               | Deletar usuário                        |
| POST   | `/api/v1/salas`                       | Criar sala                             |
| GET    | `/api/v1/salas`                       | Listar todas as salas                  |
| GET    | `/api/v1/salas/ativas`                | Listar salas ativas                    |
| GET    | `/api/v1/salas/{id}`                  | Buscar sala por ID                     |
| PUT    | `/api/v1/salas/{id}`                  | Atualizar sala                         |
| PATCH  | `/api/v1/salas/{id}/status`           | Ativar/desativar sala                  |
| POST   | `/api/v1/reservas`                    | Criar reserva                          |
| GET    | `/api/v1/reservas`                    | Listar todas as reservas               |
| GET    | `/api/v1/reservas/{id}`               | Buscar reserva por ID                  |
| GET    | `/api/v1/reservas/usuario/{usuarioId}`| Listar reservas por usuário            |
| GET    | `/api/v1/reservas/sala/{salaId}`      | Listar reservas por sala               |
| PATCH  | `/api/v1/reservas/{id}/confirmar`     | Confirmar reserva (PENDENTE→CONFIRMADA)|
| PATCH  | `/api/v1/reservas/{id}/cancelar`      | Cancelar reserva (regra 24h)           |

### Swagger UI
```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Testes

```bash
mvn test
```

Os testes cobrem os principais use cases:
- `CriarUsuarioUseCaseTest` — criação, normalização de email, conflito de email
- `CriarReservaUseCaseTest` — criação, sala inativa, limite de reservas, conflito de horário, período inválido
- `CancelarReservaUseCaseTest` — cancelamento, fora do prazo, já cancelada
- `AlternarStatusSalaUseCaseTest` — desativar (cancelando reservas), reativar

---

## 🛠️ Tecnologias

- Java 21
- Spring Boot 3.2
- Spring Data JPA + Hibernate
- PostgreSQL 16
- Flyway (migrations)
- Lombok
- SpringDoc OpenAPI (Swagger UI)
- JUnit 5 + Mockito
- Docker + Docker Compose
- Maven (multi-módulo)
