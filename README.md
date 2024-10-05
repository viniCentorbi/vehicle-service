# Vehicle Service

O **Vehicle Service** é um microsserviço que faz parte do sistema de gerenciamento de estacionamentos. Ele é responsável pelo CRUD de veículos, armazenando e gerenciando informações sobre veículos registrados.

## Funcionalidades

- **Cadastrar Veículo:** Permite registrar novos veículos no sistema.
- **Consultar Veículo:** Permite a consulta de informações de um veículo específico pelo ID.
- **Consultar Páginada de Veículos:** Permite a consulta de informações de todos os veículos cadastrados.
- **Consulta Páginada de Veículos pelo Tipo:** Permite a consulta páginada pelo tipo de veículo (Carro ou Moto).
- **Atualizar Veículo:** Permite a atualização dos dados de um veículo existente.
- **Excluir Veículo:** Permite a exclusão de um veículo do sistema.

## Tecnologias Utilizadas

- **Java 22+**
- **Spring Boot**
- **Maven**
- **PostgreSQL** - Banco de dados

## Estrutura de Diretórios

```bash
vehicle-service/
├── src/
│   ├── main/
│   │   ├── java/com/api/vehicle
│   │   │   ├── controller/
│   │   │   ├── converter/
│   │   │   ├── enums/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   ├── service/
│   └── test/
├── application.yml
└── pom.xml
```
