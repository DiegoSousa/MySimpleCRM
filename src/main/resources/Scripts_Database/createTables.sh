#!/bin/sh

# Requisitos:
#
# May need to run these commands before using this script:
#
# sudo apt-get install postgresql-client-common 
# sudo apt-get install postgresql-client-9.1


echo "=============================================="
echo "Create Tables from MessengerConcurrent..."
echo
echo "Alert: It may be necessary to close any instance of postgreSQL as psql or pgadmin."
echo
echo "Please provide a password if required: "
echo
echo "Message from console:"
echo
psql -h localhost -U postgres -d messengerConcurrent -t -c "CREATE TABLE customers
(
  id_customer bigserial NOT NULL,
  date_of_birth date NOT NULL,
  cpf character varying(14) NOT NULL,
  isactive boolean NOT NULL,
  login character varying(255) NOT NULL,
  name character varying(255) NOT NULL,
  password character varying(255) NOT NULL,
  phone character varying(16),
  CONSTRAINT customers_pkey PRIMARY KEY (id_customer ),
  CONSTRAINT customers_cpf_key UNIQUE (cpf ),
  CONSTRAINT customers_login_key UNIQUE (login ),
  CONSTRAINT customers_phone_key UNIQUE (phone )
)
WITH (
  OIDS=FALSE
);

ALTER TABLE customers
  OWNER TO postgres;

CREATE TABLE products
(
  id_product bigserial NOT NULL,
  isactive boolean NOT NULL,
  name character varying(255) NOT NULL,
  price double precision NOT NULL,
  quantity integer NOT NULL,
  CONSTRAINT products_pkey PRIMARY KEY (id_product ),
  CONSTRAINT products_name_key UNIQUE (name )
)
WITH (
  OIDS=FALSE
);

ALTER TABLE products
  OWNER TO postgres;

CREATE TABLE promotions
(
  id_promotion bigserial NOT NULL,
  isactive boolean NOT NULL,
  name character varying(255) NOT NULL,
  promotional_price double precision NOT NULL,
  stock_of_products_in_promotion integer NOT NULL,
  CONSTRAINT promotions_pkey PRIMARY KEY (id_promotion ),
  CONSTRAINT promotions_name_key UNIQUE (name )
)
WITH (
  OIDS=FALSE
);

ALTER TABLE promotions
  OWNER TO postgres;

CREATE TABLE products_in_promotion
(
  id_promotion bigint NOT NULL,
  id_product bigint NOT NULL,
  CONSTRAINT fk79c3b4e460d2bea3 FOREIGN KEY (id_product)
      REFERENCES products (id_product) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk79c3b4e4953b00cb FOREIGN KEY (id_promotion)
      REFERENCES promotions (id_promotion) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE products_in_promotion
  OWNER TO postgres;

CREATE TABLE purchases
(
  id_purchase bigserial NOT NULL,
  finalprice double precision NOT NULL,
  id_customer bigint,
  CONSTRAINT purchases_pkey PRIMARY KEY (id_purchase ),
  CONSTRAINT fk95379b9279707397 FOREIGN KEY (id_customer)
      REFERENCES customers (id_customer) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE purchases
  OWNER TO postgres;

CREATE TABLE purchased_products
(
  id_purchasefk bigint NOT NULL,
  quantity_product integer,
  id_productsfk bigint NOT NULL,
  CONSTRAINT purchased_products_pkey PRIMARY KEY (id_purchasefk , id_productsfk ),
  CONSTRAINT fk9b4caaa03cf03562 FOREIGN KEY (id_purchasefk)
      REFERENCES purchases (id_purchase) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk9b4caaa0485837a5 FOREIGN KEY (id_productsfk)
      REFERENCES products (id_product) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE purchased_products
  OWNER TO postgres;

CREATE TABLE purchased_promotions
(
  id_purchasefk bigint NOT NULL,
  quantity_promotion integer,
  id_promotionsfk bigint NOT NULL,
  CONSTRAINT purchased_promotions_pkey PRIMARY KEY (id_purchasefk , id_promotionsfk ),
  CONSTRAINT fkcd92126c3cf03562 FOREIGN KEY (id_purchasefk)
      REFERENCES purchases (id_purchase) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkcd92126cd9a87be5 FOREIGN KEY (id_promotionsfk)
      REFERENCES promotions (id_promotion) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

ALTER TABLE purchased_promotions
  OWNER TO postgres"
echo
echo "Finished Script!"
echo "=============================================="

