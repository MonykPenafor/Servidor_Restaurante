# Restaurante API - Servidor
## Projeto: Sistema de Gestão de Vendas e Entregas de Refeições

Este projeto é uma API REST desenvolvida em Java utilizando Maven. Ela fornece serviços para um restaurante, permitindo a gestão de pedidos, cardápios, e outras coisas de acordo com [este documento de especificação do software](https://github.com/MonykPenafor/Servidor_Restaurante/blob/main/Documento%20para%20cria%C3%A7%C3%A3o%20do%20software.pdf).

Todos os serviços podem ser encontrados na pasta [servicos](src/main/java/ifmt/cba/servico).

Tenho um projeto que consome estes serviços mediante testes unitarios: [Cliente_Restaurante](https://github.com/MonykPenafor/Cliente_Restaurante/tree/main/src/main/java/ifmt/cba/dto).

### Funcionalidades

- **Gestão de Produtos**: Cadastro e manutenção de produtos utilizados nas refeições.
- **Gestão de Grupos Alimentares**: Classificação de produtos em grupos alimentares.
- **Gestão de Tipos de Preparo**: Definição dos custos de preparo dos itens do cardápio.
- **Cardápios Diários**: Criação e gerenciamento de cardápios diários pelo chefe de cozinha.
- **Sistema de Pedidos**: Realização de pedidos pelos clientes através do aplicativo.
- **Controle de Estoque**: Gestão do estoque de produtos, incluindo entrada e baixa de itens.
- **Relatórios para o Gerente-Geral**: Consultas sobre produção, descartes e desempenho do restaurante.

### Banco de Dados

O sistema utiliza um banco de dados relacional, sendo suportado pelas seguintes tecnologias:
- **H2 Database**: Para testes e desenvolvimento local.
- **PostgreSQL**: Para produção, com suporte a operações complexas e escalabilidade.


### Estrutura do Projeto

- **dto**: Data Transfer Objects para comunicação entre camadas.
- **entity**: Classes de entidade representando os dados no banco.
- **execução**: Scripts para povoamento básico do banco de dados.
- **negocio**: Implementação das regras de negócio.
- **persistencia**: Classes DAO (Data Access Object) para operações no banco de dados, serviços e utilitários.
- **servico**: Implementação da lógica de serviços para acesso e manipulação de dados.
- **util**: Classes utilitárias para funções auxiliares do sistema.


### Tecnologias Utilizadas

- **Java 19**: Linguagem de programação principal.
- **Maven**: Gerenciamento de dependências e construção do projeto.
- **Jersey**: Para a construção de APIs REST.
- **Hibernate**: Para a persistência dos dados.
- **ModelMapper**: Para a conversão entre objetos DTO e entidades.
- **JUnit**: Para testes automatizados.


 ## User Stories

- **US01**: Como Gerente de Produção, quero fazer a gestão do cadastro de produtos.
- **US02**: Como Gerente de Produção, quero fazer a gestão do cadastro de grupos alimentares.
- **US03**: Como Gerente de Produção, quero fazer a gestão do cadastro de tipos de preparo de alimentos.
- **US04**: Como Chefe de Cozinha, quero fazer gestão de cardápios de refeições.
- **US05**: Como Cliente, quero gerenciar meu perfil no aplicativo.
- **US06**: Como Cliente, quero realizar um pedido de refeição a partir do cardápio do dia.
- **US07**: Como Cliente, quero acompanhar o estado do meu pedido.
- **US08**: Como Agente de Produção, quero consultar pedidos em estado Pendente.
- **US09**: Como Agente de Produção, quero mudar o estado de um pedido para Pronto.
- **US10**: Como Gerente de Produção, quero consultar pedidos prontos para entrega.
- **US11**: Como Chefe de Cozinha, quero colocar um cardápio em produção.
- **US12**: Como Gerente de Estoque, quero consultar produtos abaixo do estoque de segurança.
- **US13**: Como Gerente de Estoque, quero registrar entrada de produtos no estoque.
- **US14**: Como Gerente de Estoque, quero registrar a baixa de produtos descartados.
- **US15**: Como Gerente-Geral, quero consultar a produção de refeições por dia.
- **US16**: Como Gerente-Geral, quero consultar produtos descartados por período.
- **US17**: Como Gerente-Geral, quero consultar produtos mais consumidos.
- **US18**: Como Gerente-Geral, quero consultar produtos menos consumidos.
- **US19**: Como Gerente-Geral, quero consultar o tempo médio de produção.
- **US20**: Como Gerente-Geral, quero configurar um percentual de custo fixo.
- **US21**: Como Gerente-Geral, quero consultar o tempo médio entre produção e entrega.
- **US22**: Como Gerente-Geral, quero configurar tabela de custo de entrega por bairro.
- **US23**: Como Administrador, quero gerir cadastro dos entregadores.
- **US24**: Como Administrador, quero gerir cadastro de colaboradores internos.
- **US25**: Como Entregador, quero registrar a finalização da entrega.
- **US26**: Como Cliente, quero registrar avaliação do pedido recebido.
