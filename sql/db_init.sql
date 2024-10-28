
create table bank(
                     id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                     name varchar(300) NOT NULL,
                     bank_id_code varchar(9) NOT NULL
);
create table legalform(
                          id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                          name varchar NOT NULL
);

create table client(
                       id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                       name varchar(300) NOT NULL,
                       short_name varchar(100) NOT NULL,
                       address varchar NOT NULL,
                       legal_form_id int references legalform(id) ON DELETE CASCADE
);
create table deposit(
                        id int PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
                        client_id int references client(id) ON DELETE CASCADE ,
                        bank_id int references bank(id) ON DELETE CASCADE ,
                        create_date DATE DEFAULT current_date,
                        percentage int NOT NULL,
                        period int NOT NULL
);
insert into legalform(name) values ('Федеральное государственное учреждение'),
                                   ('Муниципальное бюджетное общеобразовательное учреждение'),
                                   ('Федеральное казённое учреждение'),
                                   ('Муниципальное учреждение здравоохранения'),
                                   ('Федеральное государственное унитарное предприятие');
insert into bank(name, bank_id_code) values
    ('Сбербанк', '123456789'),
    ('ВТБ', '987654321'),
    ('Альфа-Банк', '456789123'),
    ('Россельхозбанк', '321654987'),
    ('Тинькофф Банк', '159753468');

insert into client(name, short_name, address, legal_form_id) values
                                                                 ('ООО "Ромашка"', 'Ромашка', 'г. Москва, ул. Цветочная, д. 1', 1),
                                                                 ('ЗАО "Лотос"', 'Лотос', 'г. Санкт-Петербург, пр. Невский, д. 2', 2),
                                                                 ('ИП Иванов И.И.', 'Иванов', 'г. Казань, ул. Центральная, д. 3', 5),
                                                                 ('Государственное учреждение "Здоровье"', 'Здоровье', 'г. Екатеринбург, ул. Медицинская, д. 4', 4),
                                                                 ('Федеральное казённое учреждение "Образование"', 'Образование', 'г. Новосибирск, ул. Ученая, д. 5', 3);
INSERT INTO deposit(client_id, bank_id, create_date, percentage, period) values
                                                                             (1, 1, CURRENT_DATE - INTERVAL '1 DAY', 5, 12),
                                                                             (2, 2, CURRENT_DATE, 4, 6),
                                                                             (3, 3, CURRENT_DATE - INTERVAL '1 DAY', 6, 24),
                                                                             (4, 4, CURRENT_DATE - INTERVAL '1 DAY', 3, 18),
                                                                             (5, 5, CURRENT_DATE, 7, 36);