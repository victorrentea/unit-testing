-- create table email_to_send (id int8 not null, body varchar(255), recipient_email varchar(255), status varchar(255), subject varchar(255), version int8, primary key (id));

insert into email_to_send (id, body, recipient_email, status, subject, version)
values (1, 'body', 'recipient_email', 'status', 'subject', 0);


