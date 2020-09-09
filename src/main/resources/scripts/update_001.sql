create table grabber (
   id serial primary key not null,
   title varchar(2000) unique,
   text varchar(5000),
   link varchar(2000),
   create_date timestamp without time zone
);