CREATE TABLE account (
	id int8 NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	balance decimal(15,2) NOT NULL,
	CONSTRAINT account_id PRIMARY KEY (id)
);