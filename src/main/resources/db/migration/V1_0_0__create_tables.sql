create table categorias
(
	id_categoria bigserial not null
		constraint categorias_pkey
			primary key,
	categoria varchar(255)
);


create table clientes
(
	id_cliente bigserial not null
		constraint clientes_pkey
			primary key,
	bairro varchar(255),
	cep varchar(255),
	cidade varchar(255),
	email varchar(255),
	estado varchar(255),
	nome varchar(255),
	rua varchar(255),
	senha varchar(255)
);

create table pedidos
(
	id_pedido bigserial not null
		constraint pedidos_pkey
			primary key,
	data timestamp,
	sessao varchar(255),
	status integer not null,
	cliente_id bigint
		constraint fkg7202lk0hwxn04bmdl2thth5b
			references clientes
);

create table produtos
(
	id_produto bigserial not null
		constraint produtos_pkey
			primary key,
	descricao varchar(255),
	foto varchar(255),
	preco numeric(19,2),
	produto varchar(255),
	quantidade integer not null,
	categoria_id bigint
		constraint fk8rqw0ljwdaom34jr2t46bjtrn
			references categorias
);

create table pedido_itens
(
	id_item bigserial not null
		constraint pedido_itens_pkey
			primary key,
	produto varchar(255),
	quantidade integer not null,
	subtotal numeric(19,2),
	valor numeric(19,2),
	id_pedido bigint
		constraint fkrgh7l50r1judvc3ok9y8sfrv3
			references pedidos,
	id_produto bigint
		constraint fk83345b0ifop00o8g4ttonqm77
			references produtos
);
