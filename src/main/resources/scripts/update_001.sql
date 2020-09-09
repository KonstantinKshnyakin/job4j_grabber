create table grabber (
   id serial primary key not null,
   title varchar(2000) unique,
   text varchar(2000) unique,
   create_date timestamp without time zone
);