# Desafio Sea Corporation API

API REST em Spring Boot para cadastro/autenticação de usuários e fluxo de solicitações de serviço.

## Tecnologias

- Java 21
- Spring Boot
- Spring Security com JWT
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker Compose

## Como subir tudo com Docker Compose

Na raiz do projeto, execute:

```bash
docker compose up --build
```

A API ficará disponível em:

```text
http://localhost:8080
```

O PostgreSQL ficará disponível em:

```text
localhost:5432
Database: dev
User: dev
Password: dev
```

Para acompanhar os logs:

```bash
docker compose logs -f app
```

Para parar os containers:

```bash
docker compose down
```

Para apagar também o volume do banco e recriar tudo do zero:

```bash
docker compose down -v
docker compose up --build
```

## Como rodar as migrações

O projeto usa Flyway e possui a migração:

```text
src/main/resources/db/migration/V1__create_tables.sql
```

As migrações rodam automaticamente quando a aplicação sobe, porque o `docker-compose.yml` define:

```yaml
SPRING_FLYWAY_ENABLED: "true"
SPRING_FLYWAY_LOCATIONS: classpath:db/migration
SPRING_JPA_HIBERNATE_DDL_AUTO: validate
```

Ou seja, o fluxo recomendado é simplesmente:

```bash
docker compose up --build
```

Para validar no banco se as tabelas foram criadas:

```bash
docker compose exec postgres psql -U dev -d dev -c "\dt"
```

Para consultar o histórico do Flyway:

```bash
docker compose exec postgres psql -U dev -d dev -c "select * from flyway_schema_history;"
```

## Como criar um ADMIN inicial

O projeto já possui um seed automático em:

```text
src/main/java/com/sea/desafioseacorporation/bootstrap/AdminSeeder.java
```

Ao iniciar a aplicação, se ainda não existir um usuário com o email abaixo, ele será criado automaticamente:

```text
Email: admin@admin.com
Senha: admin123
Role: ADMIN
```

Para fazer login como ADMIN:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@admin.com",
    "password": "admin123"
  }'
```

A resposta retorna um JWT no campo `token`. Use esse token nas rotas protegidas:

```bash
export ADMIN_TOKEN="COLE_O_TOKEN_AQUI"
```

## Exemplos de requisições

### Registrar cliente

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Cliente Teste",
    "email": "cliente@teste.com",
    "password": "123456"
  }'
```

### Login de cliente

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "cliente@teste.com",
    "password": "123456"
  }'
```

```bash
export CLIENT_TOKEN="COLE_O_TOKEN_AQUI"
```

### Consultar usuário autenticado

```bash
curl http://localhost:8080/auth/me \
  -H "Authorization: Bearer $CLIENT_TOKEN"
```

### Criar usuário interno como ADMIN

Roles aceitas:

```text
ADMIN, ANALISTA, CLIENTE
```

```bash
curl -X POST http://localhost:8080/admin/users \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Analista SP",
    "email": "analista@teste.com",
    "password": "123456",
    "role": "ANALISTA"
  }'
```

### Definir cobertura de um analista como ADMIN

Troque `1` pelo ID real do analista.

```bash
curl -X PUT http://localhost:8080/admin/analistas/1/cover \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "states": ["SP", "RJ"]
  }'
```

### Criar rascunho de solicitação como CLIENTE

```bash
curl -X POST http://localhost:8080/solicitations \
  -H "Authorization: Bearer $CLIENT_TOKEN"
```

A resposta retorna o `id` da solicitação. Use esse ID nos próximos passos.

```bash
export SOLICITATION_ID="COLE_O_ID_AQUI"
```

### Atualizar passo 1 da solicitação

`serviceType` aceita: `INSTALLATION`, `MAINTENANCE`, `INSPECTION`.

`priority` aceita: `LOW`, `MEDIUM`, `HIGH`.

```bash
curl -X PATCH http://localhost:8080/solicitations/$SOLICITATION_ID/step-1 \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "serviceType": "INSTALLATION",
    "title": "Instalação de equipamento",
    "description": "Preciso instalar um equipamento no local informado.",
    "priority": "MEDIUM"
  }'
```

### Atualizar passo 2 da solicitação

```bash
curl -X PATCH http://localhost:8080/solicitations/$SOLICITATION_ID/step-2 \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "cep": "01001000",
    "number": "100",
    "complement": "Sala 10"
  }'
```

### Atualizar passo 3 da solicitação

```bash
curl -X PATCH http://localhost:8080/solicitations/$SOLICITATION_ID/step-3 \
  -H "Authorization: Bearer $CLIENT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "priority": "HIGH",
    "preferredDate": "2026-06-15",
    "estimatedValue": 1500,
    "termsAccepted": true
  }'
```

### Enviar solicitação

```bash
curl -X POST http://localhost:8080/solicitations/$SOLICITATION_ID/submit \
  -H "Authorization: Bearer $CLIENT_TOKEN"
```

## Endpoints disponíveis

| Método | URL | Autenticação |
|---|---|---|
| POST | `/auth/register` | Pública |
| POST | `/auth/login` | Pública |
| GET | `/auth/me` | JWT |
| POST | `/admin/users` | JWT ADMIN |
| PUT | `/admin/analistas/{id}/cover` | JWT ADMIN |
| POST | `/solicitations` | JWT CLIENTE |
| PATCH | `/solicitations/{id}/step-1` | JWT CLIENTE |
| PATCH | `/solicitations/{id}/step-2` | JWT CLIENTE |
| PATCH | `/solicitations/{id}/step-3` | JWT CLIENTE |
| POST | `/solicitations/{id}/submit` | JWT CLIENTE |

## Swagger / OpenAPI

Caso esteja usando o arquivo `openapi.yaml`, ele pode ser importado no Swagger Editor ou Postman.


## Variáveis principais

| Variável | Valor padrão no Docker Compose |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/dev` |
| `SPRING_DATASOURCE_USERNAME` | `dev` |
| `SPRING_DATASOURCE_PASSWORD` | `dev` |
| `SPRING_FLYWAY_ENABLED` | `true` |
| `APP_SECURITY_JWT_SECRET` | `minha-chave-super-secreta-para-jwt-com-pelo-menos-32-caracteres` |
| `APP_SECURITY_JWT_EXPIRATION_MS` | `86400000` |
