O PizzaHub é uma API REST desenvolvida para o gerenciamento completo de uma pizzaria. O projeto foi construído utilizando Java 17 e Spring Boot 3, aplicando conceitos de Domain Driven Design (DDD) para garantir uma arquitetura limpa, escalável e de fácil manutenção.

Tecnologias UtilizadasO projeto utiliza as tecnologias mais modernas do ecossistema Java:

  Java 17: Linguagem principal.
  
  Spring Boot 3.5.4: Framework base para a aplicação.
  
  Spring Data JPA: Para persistência de dados e abstração de consultas.
  
  Spring Security: Implementação de camadas de segurança e autenticação.
  
  PostgreSQL: Banco de dados relacional para ambiente de produção/runtime.
  
  Maven: Gerenciador de dependências e build.
  
  Mockito & JUnit: Para garantir a qualidade do código através de testes automatizados.

 Arquitetura e PadrõesDiferente de projetos simples, o PizzaHub foca em boas práticas de engenharia:

  DDD (Domain Driven Design): Organização do código baseada no domínio do negócio.
  
  REST API: Comunicação padronizada via protocolos HTTP.
  
  Injeção de Dependência: Para baixo acoplamento entre as classes.
  
  Funcionalidades Principais
 
 Cadastro e gerenciamento de produtos (Pizzas, bebidas, etc).
 
   Fluxo completo de pedidos e vendas.
   
   Cálculo dinâmico de descontos e promoções.
   
   Persistência de dados em banco relacional.
   
   Camada de segurança para proteção de endpoints.

lista completa dos endpoints da aplicação, organizada por controlador:

CardapioController
GET /cardapio/recuperaAtivo
PUT /cardapio/atualizaAtivo/{id}
GET /cardapio/buscaPorId/{id}
GET /cardapio/lista
ClienteController
POST /clientes/cadastrar
GET /clientes/buscaPorCpf/{cpf}
DescontosController
GET /descontos/descontosDisponiveis
GET /descontos/DecideDesconto/{id}
PedidoController
POST /pedidos/submetePedido
GET /pedidos/status/{id}
GET /pedidos/cancelaPedido/{id}
GET /pedidos/pedidosEntreguesEntre/{dataInicio}/{dataFinal}
GET /pedidos/pagarPedido/{id}


