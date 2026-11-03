# RoomReservas — Java Spring

🏢 **Sistema de reserva de salas** construído com **Clean Architecture**, **Java 21**, **Spring Boot 3**, **Lombok**, **JPA** e **PostgreSQL**.

> ⚠️ **Estado deste repositório**: A estrutura de Clean Architecture em 4 módulos Maven foi implementada, com entidades, casos de uso, controladores e configurações básicas. No entanto, a cobertura de testes automatizados e o pipeline CI/CD ainda são limitados. O projeto original em C# (`RoomReservas`) foi reimplementado aqui em Java como exercício de migração e demonstração da arquitetura em outro ecossistema.

---

## 🏗️ Arquitetura

O projeto segue **Clean Architecture** em **4 módulos Maven** separados:

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

- **Domain**: Contém as entidades puros (`Usuario`, `Sala`, `Reserva`), enumerações de status e exceções de domínio. Nenhuma dependência externa.
- **Application**: Contém os cases of use (use cases) via manipuladores (handlers), DTOs para entrada/saida e regras de negócio. Dependência apenas do Domain.
- **Infrastructure**: Implementa os repositórios (Spring Data JPA), configurações Flyway para migrations, e adapters para o PostgreSQL. Dependência de Domain (para interfaces) e do Spring.
- **Presentation**: Controllers REST que expõem a API, configurador do Swagger/OpenAPI e handler global de exceções. Dependência de Application e Infrastructure (via Spring).

---

## 🗂️ Entidades

| Entidade   | Atributos principais                                                  |
|------------|-----------------------------------------------------------------------|
| `Usuario`  | id (UUID), nome, email (único)                                        |
| `Sala`     | id (UUID), nome, capacidade, ativa, valorDiaria                       |
| `Reserva`  | id (UUID), usuario, sala, inicio, fim, valorTotal, status, dataCriacao |

**Status da Reserva:** `PENDENTE` → `CONFIRMADA` / `CANCELADA` / `CONCLUIDA`

---

## ✅ Regras de Negócio (Resumo)

| Regra | Descrição |
|---|---|
| **Conflito de horários** | É impossível reservar uma sala já ocupada no período solicitado |
| **Sala inativa** | Salas desativadas não aceitam novas reservas; ao desativar, cancela automaticamente reservas futuras ativas |
| **Limite de reservas** | Usuário pode ter no máximo **3 reservas ativas** (PENDENTE ou CONFIRMADA) simultâneas |
| **Cancelamento com prazo** | Cancelamento somente permitido com **pelo menos 24h de antecedência** |
| **Cálculo do valor total** | Proporcional ao `valorDiaria` da sala, calculado por horas (`horas / 24 * valorDiaria`) |
| **Duração mínima** | Reserva deve ter ao menos **30 minutos** |

---

## 🚀 Como Executar

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

## 📚 Endpoints da API

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

## 🛠️ Tecnologias

| Tecnologia | Versão / Observação |
|---|---|
| Java | 21 |
| Spring Boot | 3.2 |
| Spring Data JPA + Hibernate | ORM para PostgreSQL |
| PostgreSQL | 16 |
| Flyway | Gerenciador de migrações de esquema |
| Lombok | Redução de boilerplate (getters, setters, construtores) |
| SpringDoc OpenAPI | Swagger UI integrado |
| JUnit 5 | Framework de testes |
| Mockito | Mocking para testes |
| Docker + Docker Compose | Containerização |
| Maven | Build multi-módulo |

---

## 🧪 Testes

```bash
mvn test
```

> ⚠️ **Atenção**: Os testes cobrem os principais use cases listados abaixo, mas a cobertura ainda é limitada. Mais testes de integração e E2E são necessários para um nível production-ready.

### Testes cobrem os seguintes use cases

| Caso de Teste | Descrição |
|---|---|
| `CriarUsuarioUseCaseTest` | Criação, normalização de email, conflito de email |
| `CriarReservaUseCaseTest` | Criação, sala inativa, limite de reservas, conflito de horário, período inválido |
| `CancelarReservaUseCaseTest` | Cancelamento, fora do prazo, já cancelada |
| `AlternarStatusSalaUseCaseTest` | Desativar (cancelando reservas), reativar |

---

## 📂 Estrutura Completa dos Módulos

### `roomreservas/domain/`

- **Entidades** (`Usuario`, `Sala`, `Reserva`): Anotadas com `@Entity`, `@Id`, relacionamentos JPA
- **Enumeradores** (`Status`): `Pendente`, `Confirmada`, `Cancelada`, `Concluída`
- **Exceções** de domínio (`ReservaJaConfirmadaException`, `SalaInativaException`, etc.)
- **Interfaces de repositório** (`UsuarioRepository`, `SalaRepository`, `ReservaRepository`): Extendem `JpaRepository`

### `roomreservas/application/`

- **Commands** (manipuladores de comando): `CriarUsuarioHandler`, `CriarReservaHandler`, `AlternarStatusSalaHandler`
- **Queries** (manipuladores de query): `ListarUsuariosHandler`, `ListarSalasAtivasHandler`
- **DTOs**: `UsuarioDTO`, `SalaDTO`, `ReservaDTO` para transferência de dados
- **Regras de negócio**: Validação de conflitos, limites, cálculo de valor

### `roomreservas/infrastructure/`

- **Configuração JPA**: `application.properties` / `application.yml`
- **Entity configurations**: Mapeamentos adicionais (`@Entity`, `@Table`, `@Column`)
- **Repositórios implementados**: `UsuarioRepositoryImpl`, `SalaRepositoryImpl`, `ReservaRepositoryImpl` (caso necessário)
- **Migrações Flyway**: `db/migration/V1__ inicial.sql`, `V2__...`, etc.
- **ServicesContainer**: Configuração de injecao de dependência para o pacote presentation

### `roomreservas/presentation/`

- **Controllers REST**: `UsuarioController`, `SalaController`, `ReservaController`
- **Configuração Swagger**: `SwaggerConfig` para documentação automática
- **Exception Handler global**: Tratamento centralizado de erros e exceções

---

## 📦 Dependências NuGet (apenas para contexto)

> *Não se aplica a este projeto Java. As dependências são gerenciadas via Maven/Gradle.*

---

## ⚠️ Limitações Conhecidas

1. **Sem CI/CD configurado** — Não há pipeline GitHub Actions automatizado para testes ou build
2. **Cobertura de testes limitada** — Apenas 4 casos de teste básicos identificados; faltam testes de integração e E2E
3. **Migrações Flyway em desenvolvimento** — O esquema pode evoluir; consulte `db/migration/`
4. **Valor fixo por hora** — O `valorTotal` usa cálculo proporcional (configurável)
5. **Sem autenticação/autorização** — A API atual não implementa segurança (JWT, Spring Security)
6. **Projeto em fase de migração** — Estrutura Clean Architecture implementada, mas funcionalidades ainda estão em desenvolvimento

---

## 📅 Próximas Etapas Planejadas

- Implementar autenticação JWT / Spring Security
- Adicionar mais casos de teste (integração com banco real)
- Implementar Docker multi-stage para produção
- Configurar GitHub Actions (CI/CD)
- Adicionar mais regras de negócio (limpeza de reservas expiradas, notificações)
- Melhorar cálculos de valor (considerar horários comerciais, descontos)

---

## 📄 Licença

Este projeto está licenciado sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

**Gabriel Florenço** — Desenvolvedor Java / .NET  
GitHub: [@gabrielflorencco](https://github.com/gabrielflorencco)  
Contexto: Migração do projeto original C# (`RoomReservas`) para Java Spring Boot, demonstrando Clean Architecture em outro ecossistema.

---

## 🔄 Relationship with RoomReservas (C#)

Este repositório (RoomReservasJava) é a **reimplementação em Java Spring Boot** do projeto original em C#.

- **RoomReservas (C#)**: Implementação original com Clean Architecture, CQRS e SOLID. Estrutura de 5 projetos (Domain, Application, Infrastructure, API, Tests).
- **RoomReservasJava (Java)**: Mesma lógica de negócios reescrita para o ecossistema Spring Boot. 4 módulos Maven (domain, application, infrastructure, presentation). Demonstra que a arquitetura transcende linguagem.

Ambos os repositórios compartilham as mesmas regras de negócio e estrutura arquitetural, mas diferem nas tecnologias usadas (.NET vs. Java). Consulte `RoomReservas` para a versão original em C#.

**Última atualização:** 2026-11-02  
**Status:** Em desenvolvimento — estrutura arquitetural implementada, regras de negócio operacionais, testes básicos.