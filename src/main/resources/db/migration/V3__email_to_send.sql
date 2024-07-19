create sequence email_to_send_seq start with 1 increment 50;

create table email_to_send (id int8 not null, body varchar(255), recipient_email varchar(255), status varchar(255), subject varchar(255), version int8, primary key (id));