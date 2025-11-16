CREATE TABLE IF NOT EXISTS clientes (
    cpf VARCHAR(15) NOT NULL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    celular VARCHAR(20) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(200) NOT NULL,      
    role VARCHAR(20) NOT NULL         
);

create table if not exists ingredientes (
  id bigint primary key,
  descricao varchar(255) not null
);

create table if not exists itensEstoque(
    id bigint primary key,
    quantidade int,
    ingrediente_id bigint,
    foreign key (ingrediente_id) references ingredientes(id)
);

-- Tabela Pedido
create table if not exists pedidos (
  id bigint primary key,
  cliente_cpf bigint,
  dataHoraPagamento timestamp not null,
  status_pedido VARCHAR(20) NOT NULL -- atualizar depois para um create type
  valor numeric(8,2) not null,
  impostos numeric(8,2) not null,
  descontos numeric(8,2) not null,
  valorCobrado numeric(8,2) not null,
  foreign key (cliente_cpf) references clientes(cpf)
)

--Tabela ItemPedido
create table if not exists item_pedido (
    pedido_id bigint not null,
    produto_id bigint not null,
    primary key (pedido_id, produto_id),
    quantidade int not null,
    foreign key (pedido_id) references pedidos(id),
    foreign key (produto_id) references produtos(id)
);


-- Tabela Receita
create table if not exists receitas (
  id bigint primary key,
  titulo varchar(255) not null
);

-- Tabela de relacionamento entre Receita e Ingrediente
create table if not exists receita_ingrediente (
  receita_id bigint not null,
  ingrediente_id bigint not null,
  primary key (receita_id, ingrediente_id),
  foreign key (receita_id) references receitas(id),
  foreign key (ingrediente_id) references ingredientes(id)
);

-- Tabela de Produtos
create table if not exists produtos (
  id bigint primary key,
  descricao varchar(255) not null,
  preco bigint
);

-- Tabela de relacionamento entre Produto e Receita
create table if not exists produto_receita (
  produto_id bigint not null,
  receita_id bigint not null,
  primary key (produto_id,receita_id),
  foreign key (produto_id) references produtos(id),
  foreign key (receita_id) references receitas(id)
);

-- Tabela de Cardapios
create table if not exists cardapios (
  id bigint primary key,
  titulo varchar(255) not null,
  ativo BOOLEAN not null
);

-- Tabela de relacionamento entre Cardapio e Produto
create table if not exists cardapio_produto (
  cardapio_id bigint not null,
  produto_id bigint not null,
  primary key (cardapio_id,produto_id),
  foreign key (cardapio_id) references cardapios(id),
  foreign key (produto_id) references produtos(id)
);