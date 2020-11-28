create table role (
   id serial primary key not null,
   role varchar(200)
);

create table person (
   id serial primary key not null,
   name varchar (200),
   login varchar(200),
   email varchar(200),
   password varchar(2000),
   role_id int,
   constraint fk_role foreign key (role_id) references role(id)
);

create table room (
   id serial primary key not null,
   name varchar (200),
   person_id int,
   constraint fk_person foreign key (person_id) references person(id)
);

create table message (
   id serial primary key not null,
   txt text,
   person_id int,
   room_id int,
   created data,
   constraint fk_person foreign key (person_id) references person(id),
   constraint fk_room foreign key (room_id) references room(id)
);