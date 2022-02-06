CREATE TABLE transfer (
	id int8 NOT NULL,
	source_id int8 NOT NULL,
	target_id int8 NOT NULL,
	amount decimal(15,2) NOT NULL,
	transfer_time timestamp NOT NULL,
	CONSTRAINT transfer_id PRIMARY KEY (id),
	CONSTRAINT transfer_id_source FOREIGN KEY (source_id) REFERENCES account(id),
	CONSTRAINT transfer_id_target FOREIGN KEY (target_id) REFERENCES account(id)
);