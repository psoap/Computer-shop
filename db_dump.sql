--
-- PostgreSQL database dump
--

-- Dumped from database version 10.2
-- Dumped by pg_dump version 10.2

SET client_encoding = 'UTF8';

--
-- Name: category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE category (
    id smallint NOT NULL,
    parent_id smallint
);


--
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE category_id_seq
    AS smallint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE category_id_seq OWNED BY category.id;


--
-- Name: category_translate; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE category_translate (
    category_id smallint NOT NULL,
    name character varying(16) NOT NULL,
    lang_code character varying(2) NOT NULL
);


--
-- Name: user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "user" (
    id bigint NOT NULL,
    email character varying(127) NOT NULL,
    login character varying(16) NOT NULL,
    password character varying(255) NOT NULL,
    role_id smallint DEFAULT 1 NOT NULL,
    balance numeric(12,2) DEFAULT 0
);


--
-- Name: customer_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE customer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: customer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE customer_id_seq OWNED BY "user".id;


--
-- Name: user_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_role (
    id smallint NOT NULL,
    name character varying(16) NOT NULL
);


--
-- Name: customer_role_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE customer_role_id_seq
    AS smallint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: customer_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE customer_role_id_seq OWNED BY user_role.id;


--
-- Name: delivery_profile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE delivery_profile (
    id bigint NOT NULL,
    first_name character varying(32) NOT NULL,
    last_name character varying(32) NOT NULL,
    patronymic character varying(32) NOT NULL,
    address_location character varying(255) NOT NULL,
    phone_number character varying(16) NOT NULL,
    user_id bigint NOT NULL
);


--
-- Name: delivery_profile_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE delivery_profile_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: delivery_profile_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE delivery_profile_id_seq OWNED BY delivery_profile.id;


--
-- Name: lang; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE lang (
    code character varying(2) NOT NULL,
    name character varying(16) NOT NULL
);


--
-- Name: order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "order" (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    delivery_profile_id bigint,
    status_id smallint DEFAULT 1 NOT NULL,
    total_price numeric(12,2) DEFAULT 0 NOT NULL,
    change_date timestamp with time zone NOT NULL
);


--
-- Name: order_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE order_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE order_id_seq OWNED BY "order".id;


--
-- Name: order_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE order_product (
    order_id bigint NOT NULL,
    product_id bigint NOT NULL,
    quantity smallint NOT NULL
);


--
-- Name: order_status; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE order_status (
    id smallint NOT NULL,
    name character varying(32) NOT NULL
);


--
-- Name: order_status_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE order_status_id_seq
    AS smallint
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: order_status_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE order_status_id_seq OWNED BY order_status.id;


--
-- Name: product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE product (
    id bigint NOT NULL,
    name character varying(127) NOT NULL,
    short_description character varying(250) NOT NULL,
    description character varying NOT NULL,
    price numeric NOT NULL,
    category_id smallint,
    image_url character varying
);


--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE product_id_seq OWNED BY product.id;


--
-- Name: category id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY category ALTER COLUMN id SET DEFAULT nextval('category_id_seq'::regclass);


--
-- Name: delivery_profile id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY delivery_profile ALTER COLUMN id SET DEFAULT nextval('delivery_profile_id_seq'::regclass);


--
-- Name: order id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "order" ALTER COLUMN id SET DEFAULT nextval('order_id_seq'::regclass);


--
-- Name: order_status id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY order_status ALTER COLUMN id SET DEFAULT nextval('order_status_id_seq'::regclass);


--
-- Name: product id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY product ALTER COLUMN id SET DEFAULT nextval('product_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('customer_id_seq'::regclass);


--
-- Name: user_role id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_role ALTER COLUMN id SET DEFAULT nextval('customer_role_id_seq'::regclass);


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO category (id, parent_id) VALUES (31, NULL);
INSERT INTO category (id, parent_id) VALUES (32, 31);
INSERT INTO category (id, parent_id) VALUES (33, 31);
INSERT INTO category (id, parent_id) VALUES (34, NULL);


--
-- Data for Name: category_translate; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO category_translate (category_id, name, lang_code) VALUES (31, 'Components', 'en');
INSERT INTO category_translate (category_id, name, lang_code) VALUES (31, 'Комплектующие', 'ru');
INSERT INTO category_translate (category_id, name, lang_code) VALUES (32, 'Processors', 'en');
INSERT INTO category_translate (category_id, name, lang_code) VALUES (32, 'Процессоры', 'ru');
INSERT INTO category_translate (category_id, name, lang_code) VALUES (33, 'GPUs', 'en');
INSERT INTO category_translate (category_id, name, lang_code) VALUES (33, 'Видеокарты', 'ru');
INSERT INTO category_translate (category_id, name, lang_code) VALUES (34, 'Laptops', 'en');
INSERT INTO category_translate (category_id, name, lang_code) VALUES (34, 'Ноутбуки', 'ru');


--
-- Data for Name: delivery_profile; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO delivery_profile (id, first_name, last_name, patronymic, address_location, phone_number, user_id) VALUES (34, 'Владимир', 'Путин', 'Владимирович', 'Москва, Тверской, Красная площадь, 1', '+74956953776', 25);


--
-- Data for Name: lang; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO lang (code, name) VALUES ('en', 'English');
INSERT INTO lang (code, name) VALUES ('ru', 'Russian');


--
-- Data for Name: order; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO "order" (id, user_id, delivery_profile_id, status_id, total_price, change_date) VALUES (38, 25, 34, 2, 815000.00, '2018-10-25 10:54:55.536+06');
INSERT INTO "order" (id, user_id, delivery_profile_id, status_id, total_price, change_date) VALUES (39, 25, 34, 3, 522300.00, '2018-10-25 14:05:02.288+06');
INSERT INTO "order" (id, user_id, delivery_profile_id, status_id, total_price, change_date) VALUES (40, 25, 34, 4, 815000.00, '2018-10-25 14:05:27.218+06');
INSERT INTO "order" (id, user_id, delivery_profile_id, status_id, total_price, change_date) VALUES (41, 25, NULL, 1, 0.00, '2018-10-25 14:06:15.164+06');


--
-- Data for Name: order_product; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO order_product (order_id, product_id, quantity) VALUES (38, 26, 1);
INSERT INTO order_product (order_id, product_id, quantity) VALUES (39, 35, 1);
INSERT INTO order_product (order_id, product_id, quantity) VALUES (39, 40, 2);
INSERT INTO order_product (order_id, product_id, quantity) VALUES (40, 26, 1);
INSERT INTO order_product (order_id, product_id, quantity) VALUES (41, 34, 3);


--
-- Data for Name: order_status; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO order_status (id, name) VALUES (1, 'Cart');
INSERT INTO order_status (id, name) VALUES (2, 'Paid');
INSERT INTO order_status (id, name) VALUES (4, 'Delivered');
INSERT INTO order_status (id, name) VALUES (3, 'Shipping');


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (26, 'Ноутбук Apple MacBook Pro с дисплеем Retina', 'Процессор: Intel Core i5 / Частота процессора, ГГц: 2.3 / Объем памяти: 8 Гб / Жесткий диск: Отсутствует / SSD: 256 Гб / Диагональ экрана, дюйм: 13.3 / Разрешение экрана: 2560 x 1600 WQXGA / Операционная система: Mac OS Mojave / Вес: 1.3 кг', '<p>Новый уровень безопасности
Apple T2 - чип второго поколения для Mac, созданный для более надёжной защиты информации на вашем MacBook Pro. 13 дюймовая панель Touch Bar и оснащённая сопроцессором Secure Enclave, которая обеспечивает работу функции безопасной загрузки и отвечает за хранение зашифрованных данных. В Apple T2 интегрировано множество дискретных контроллеров, в том числе контроллер управления системой, аудиоконтроллер и SSD-контроллер. Кроме того, чип поддерживает функцию «Привет, Siri» на MacBook Pro - ваш голосовой помощник всегда готов открыть приложение, найти документ, включить музыку или ответить на вопрос.
<p>Самый мощный и универсальный порт
Thunderbolt 3 объединил высочайшую пропускную способность с функциональностью стандартного интерфейса USB‑C. В итоге получился высокоскоростной и универсальный порт. Он позволяет передавать данные, заряжать устройства и выводить видео на дополнительные мониторы через один и тот же разъём. При этом его скорость в два раза выше, чем у Thunderbolt 2 - до 40 Гбит/с. MacBook Pro 13 дюймов имеет до четырёх таких портов, чтобы можно было подключаться с обеих сторон. А подсоединить ваши устройства можно всего одним кабелем или кабелем с адаптером. Поскольку порт Thunderbolt 3 симметричен, то при подключении не нужно думать, где у него верх, а где низ.
<p>Больше функций в одно касание
Очень долго верхнюю строку клавиатуры занимали функциональные клавиши. Мы заменили их более универсальным и удобным элементом управления - сенсорной панелью Touch Bar. В зависимости от того, чем вы занимаетесь, на ней автоматически отображаются нужные инструменты - например, настройка громкости и яркости, функции управления фото и видео, эмодзи или предиктивный ввод текста.
<p>Всё, что нужно. Там, где нужно
Панель Touch Bar поддерживает многие встроенные приложения на вашем MacBook Pro и помогает быстрее ответить на письма, отформатировать документы и справиться с другими повседневными делами. Интерфейс Touch Bar меняется в зависимости от приложения, которое вы используете. Таким образом, необходимые вам инструменты всегда на видном месте. Кроме того, возможности Touch Bar можно использовать в приложениях сторонних разработчиков.
<table>
<tr>
<td>Количество ядер процессора</td>
<td>2</td>
</tr>
<tr>
<td>Тип видеокарты</td>
<td>встроенная</td>
</tr>
<tr>
<td>Видеокарта</td>
<td>Intel Iris Graphics 6100</td>
</tr>
<tr>
<td>Установленная ОС</td>
<td>macOS</td>
</tr>
<tr>
<td>Тип жесткого диска</td>
<td>SSD</td>
</tr>
<tr>
<td>Вес</td>
<td>1.58 кг</td>
</tr>
</table>', 815000, 34, '/uploads/FC400D9.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (28, 'Ноутбук Apple MacBook Pro с дисплеем Retina', 'Процессор: Intel Core i5 / Частота процессора, ГГц: 2.3 / Объем памяти: 8 Гб / Жесткий диск: Отсутствует / SSD: 256 Гб / Диагональ экрана, дюйм: 13.3 / Разрешение экрана: 2560 x 1600 WQXGA / Операционная система: Mac OS Mojave / Вес: 1.3 кг', '<p>Новый уровень безопасности
Apple T2 - чип второго поколения для Mac, созданный для более надёжной защиты информации на вашем MacBook Pro. 13 дюймовая панель Touch Bar и оснащённая сопроцессором Secure Enclave, которая обеспечивает работу функции безопасной загрузки и отвечает за хранение зашифрованных данных. В Apple T2 интегрировано множество дискретных контроллеров, в том числе контроллер управления системой, аудиоконтроллер и SSD-контроллер. Кроме того, чип поддерживает функцию «Привет, Siri» на MacBook Pro - ваш голосовой помощник всегда готов открыть приложение, найти документ, включить музыку или ответить на вопрос.
<p>Самый мощный и универсальный порт
Thunderbolt 3 объединил высочайшую пропускную способность с функциональностью стандартного интерфейса USB‑C. В итоге получился высокоскоростной и универсальный порт. Он позволяет передавать данные, заряжать устройства и выводить видео на дополнительные мониторы через один и тот же разъём. При этом его скорость в два раза выше, чем у Thunderbolt 2 - до 40 Гбит/с. MacBook Pro 13 дюймов имеет до четырёх таких портов, чтобы можно было подключаться с обеих сторон. А подсоединить ваши устройства можно всего одним кабелем или кабелем с адаптером. Поскольку порт Thunderbolt 3 симметричен, то при подключении не нужно думать, где у него верх, а где низ.
<p>Больше функций в одно касание
Очень долго верхнюю строку клавиатуры занимали функциональные клавиши. Мы заменили их более универсальным и удобным элементом управления - сенсорной панелью Touch Bar. В зависимости от того, чем вы занимаетесь, на ней автоматически отображаются нужные инструменты - например, настройка громкости и яркости, функции управления фото и видео, эмодзи или предиктивный ввод текста.
<p>Всё, что нужно. Там, где нужно
Панель Touch Bar поддерживает многие встроенные приложения на вашем MacBook Pro и помогает быстрее ответить на письма, отформатировать документы и справиться с другими повседневными делами. Интерфейс Touch Bar меняется в зависимости от приложения, которое вы используете. Таким образом, необходимые вам инструменты всегда на видном месте. Кроме того, возможности Touch Bar можно использовать в приложениях сторонних разработчиков.
<table>
<tr>
<td>Количество ядер процессора</td>
<td>2</td>
</tr>
<tr>
<td>Тип видеокарты</td>
<td>встроенная</td>
</tr>
<tr>
<td>Видеокарта</td>
<td>Intel Iris Graphics 6100</td>
</tr>
<tr>
<td>Установленная ОС</td>
<td>macOS</td>
</tr>
<tr>
<td>Тип жесткого диска</td>
<td>SSD</td>
</tr>
<tr>
<td>Вес</td>
<td>1.58 кг</td>
</tr>
</table>', 815000, 34, '/uploads/65AFF32.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (27, 'Ноутбук Apple MacBook Pro с дисплеем Retina', 'Процессор: Intel Core i5 / Частота процессора, ГГц: 2.3 / Объем памяти: 8 Гб / Жесткий диск: Отсутствует / SSD: 256 Гб / Диагональ экрана, дюйм: 13.3 / Разрешение экрана: 2560 x 1600 WQXGA / Операционная система: Mac OS Mojave / Вес: 1.3 кг', '<p>Новый уровень безопасности
Apple T2 - чип второго поколения для Mac, созданный для более надёжной защиты информации на вашем MacBook Pro. 13 дюймовая панель Touch Bar и оснащённая сопроцессором Secure Enclave, которая обеспечивает работу функции безопасной загрузки и отвечает за хранение зашифрованных данных. В Apple T2 интегрировано множество дискретных контроллеров, в том числе контроллер управления системой, аудиоконтроллер и SSD-контроллер. Кроме того, чип поддерживает функцию «Привет, Siri» на MacBook Pro - ваш голосовой помощник всегда готов открыть приложение, найти документ, включить музыку или ответить на вопрос.
<p>Самый мощный и универсальный порт
Thunderbolt 3 объединил высочайшую пропускную способность с функциональностью стандартного интерфейса USB‑C. В итоге получился высокоскоростной и универсальный порт. Он позволяет передавать данные, заряжать устройства и выводить видео на дополнительные мониторы через один и тот же разъём. При этом его скорость в два раза выше, чем у Thunderbolt 2 - до 40 Гбит/с. MacBook Pro 13 дюймов имеет до четырёх таких портов, чтобы можно было подключаться с обеих сторон. А подсоединить ваши устройства можно всего одним кабелем или кабелем с адаптером. Поскольку порт Thunderbolt 3 симметричен, то при подключении не нужно думать, где у него верх, а где низ.
<p>Больше функций в одно касание
Очень долго верхнюю строку клавиатуры занимали функциональные клавиши. Мы заменили их более универсальным и удобным элементом управления - сенсорной панелью Touch Bar. В зависимости от того, чем вы занимаетесь, на ней автоматически отображаются нужные инструменты - например, настройка громкости и яркости, функции управления фото и видео, эмодзи или предиктивный ввод текста.
<p>Всё, что нужно. Там, где нужно
Панель Touch Bar поддерживает многие встроенные приложения на вашем MacBook Pro и помогает быстрее ответить на письма, отформатировать документы и справиться с другими повседневными делами. Интерфейс Touch Bar меняется в зависимости от приложения, которое вы используете. Таким образом, необходимые вам инструменты всегда на видном месте. Кроме того, возможности Touch Bar можно использовать в приложениях сторонних разработчиков.
<table>
<tr>
<td>Количество ядер процессора</td>
<td>2</td>
</tr>
<tr>
<td>Тип видеокарты</td>
<td>встроенная</td>
</tr>
<tr>
<td>Видеокарта</td>
<td>Intel Iris Graphics 6100</td>
</tr>
<tr>
<td>Установленная ОС</td>
<td>macOS</td>
</tr>
<tr>
<td>Тип жесткого диска</td>
<td>SSD</td>
</tr>
<tr>
<td>Вес</td>
<td>1.58 кг</td>
</tr>
</table>', 815000, 34, '/uploads/39DCFD2.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (29, 'Ноутбук Apple MacBook Pro с дисплеем Retina', 'Процессор: Intel Core i5 / Частота процессора, ГГц: 2.3 / Объем памяти: 8 Гб / Жесткий диск: Отсутствует / SSD: 256 Гб / Диагональ экрана, дюйм: 13.3 / Разрешение экрана: 2560 x 1600 WQXGA / Операционная система: Mac OS Mojave / Вес: 1.3 кг', '<p>Новый уровень безопасности
Apple T2 - чип второго поколения для Mac, созданный для более надёжной защиты информации на вашем MacBook Pro. 13 дюймовая панель Touch Bar и оснащённая сопроцессором Secure Enclave, которая обеспечивает работу функции безопасной загрузки и отвечает за хранение зашифрованных данных. В Apple T2 интегрировано множество дискретных контроллеров, в том числе контроллер управления системой, аудиоконтроллер и SSD-контроллер. Кроме того, чип поддерживает функцию «Привет, Siri» на MacBook Pro - ваш голосовой помощник всегда готов открыть приложение, найти документ, включить музыку или ответить на вопрос.
<p>Самый мощный и универсальный порт
Thunderbolt 3 объединил высочайшую пропускную способность с функциональностью стандартного интерфейса USB‑C. В итоге получился высокоскоростной и универсальный порт. Он позволяет передавать данные, заряжать устройства и выводить видео на дополнительные мониторы через один и тот же разъём. При этом его скорость в два раза выше, чем у Thunderbolt 2 - до 40 Гбит/с. MacBook Pro 13 дюймов имеет до четырёх таких портов, чтобы можно было подключаться с обеих сторон. А подсоединить ваши устройства можно всего одним кабелем или кабелем с адаптером. Поскольку порт Thunderbolt 3 симметричен, то при подключении не нужно думать, где у него верх, а где низ.
<p>Больше функций в одно касание
Очень долго верхнюю строку клавиатуры занимали функциональные клавиши. Мы заменили их более универсальным и удобным элементом управления - сенсорной панелью Touch Bar. В зависимости от того, чем вы занимаетесь, на ней автоматически отображаются нужные инструменты - например, настройка громкости и яркости, функции управления фото и видео, эмодзи или предиктивный ввод текста.
<p>Всё, что нужно. Там, где нужно
Панель Touch Bar поддерживает многие встроенные приложения на вашем MacBook Pro и помогает быстрее ответить на письма, отформатировать документы и справиться с другими повседневными делами. Интерфейс Touch Bar меняется в зависимости от приложения, которое вы используете. Таким образом, необходимые вам инструменты всегда на видном месте. Кроме того, возможности Touch Bar можно использовать в приложениях сторонних разработчиков.
<table>
<tr>
<td>Количество ядер процессора</td>
<td>2</td>
</tr>
<tr>
<td>Тип видеокарты</td>
<td>встроенная</td>
</tr>
<tr>
<td>Видеокарта</td>
<td>Intel Iris Graphics 6100</td>
</tr>
<tr>
<td>Установленная ОС</td>
<td>macOS</td>
</tr>
<tr>
<td>Тип жесткого диска</td>
<td>SSD</td>
</tr>
<tr>
<td>Вес</td>
<td>1.58 кг</td>
</tr>
</table>', 815000, 34, '/uploads/F02630D.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (33, 'Ноутбук Apple MacBook Pro с дисплеем Retina', 'Процессор: Intel Core i5 / Частота процессора, ГГц: 2.3 / Объем памяти: 8 Гб / Жесткий диск: Отсутствует / SSD: 256 Гб / Диагональ экрана, дюйм: 13.3 / Разрешение экрана: 2560 x 1600 WQXGA / Операционная система: Mac OS Mojave / Вес: 1.3 кг', '<p>Новый уровень безопасности
Apple T2 - чип второго поколения для Mac, созданный для более надёжной защиты информации на вашем MacBook Pro. 13 дюймовая панель Touch Bar и оснащённая сопроцессором Secure Enclave, которая обеспечивает работу функции безопасной загрузки и отвечает за хранение зашифрованных данных. В Apple T2 интегрировано множество дискретных контроллеров, в том числе контроллер управления системой, аудиоконтроллер и SSD-контроллер. Кроме того, чип поддерживает функцию «Привет, Siri» на MacBook Pro - ваш голосовой помощник всегда готов открыть приложение, найти документ, включить музыку или ответить на вопрос.
<p>Самый мощный и универсальный порт
Thunderbolt 3 объединил высочайшую пропускную способность с функциональностью стандартного интерфейса USB‑C. В итоге получился высокоскоростной и универсальный порт. Он позволяет передавать данные, заряжать устройства и выводить видео на дополнительные мониторы через один и тот же разъём. При этом его скорость в два раза выше, чем у Thunderbolt 2 - до 40 Гбит/с. MacBook Pro 13 дюймов имеет до четырёх таких портов, чтобы можно было подключаться с обеих сторон. А подсоединить ваши устройства можно всего одним кабелем или кабелем с адаптером. Поскольку порт Thunderbolt 3 симметричен, то при подключении не нужно думать, где у него верх, а где низ.
<p>Больше функций в одно касание
Очень долго верхнюю строку клавиатуры занимали функциональные клавиши. Мы заменили их более универсальным и удобным элементом управления - сенсорной панелью Touch Bar. В зависимости от того, чем вы занимаетесь, на ней автоматически отображаются нужные инструменты - например, настройка громкости и яркости, функции управления фото и видео, эмодзи или предиктивный ввод текста.
<p>Всё, что нужно. Там, где нужно
Панель Touch Bar поддерживает многие встроенные приложения на вашем MacBook Pro и помогает быстрее ответить на письма, отформатировать документы и справиться с другими повседневными делами. Интерфейс Touch Bar меняется в зависимости от приложения, которое вы используете. Таким образом, необходимые вам инструменты всегда на видном месте. Кроме того, возможности Touch Bar можно использовать в приложениях сторонних разработчиков.
<table>
<tr>
<td>Количество ядер процессора</td>
<td>2</td>
</tr>
<tr>
<td>Тип видеокарты</td>
<td>встроенная</td>
</tr>
<tr>
<td>Видеокарта</td>
<td>Intel Iris Graphics 6100</td>
</tr>
<tr>
<td>Установленная ОС</td>
<td>macOS</td>
</tr>
<tr>
<td>Тип жесткого диска</td>
<td>SSD</td>
</tr>
<tr>
<td>Вес</td>
<td>1.58 кг</td>
</tr>
</table>', 815000, 34, '/uploads/335BFA1.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (32, 'Ноутбук Apple MacBook Pro с дисплеем Retina', 'Процессор: Intel Core i5 / Частота процессора, ГГц: 2.3 / Объем памяти: 8 Гб / Жесткий диск: Отсутствует / SSD: 256 Гб / Диагональ экрана, дюйм: 13.3 / Разрешение экрана: 2560 x 1600 WQXGA / Операционная система: Mac OS Mojave / Вес: 1.3 кг', '<p>Новый уровень безопасности
Apple T2 - чип второго поколения для Mac, созданный для более надёжной защиты информации на вашем MacBook Pro. 13 дюймовая панель Touch Bar и оснащённая сопроцессором Secure Enclave, которая обеспечивает работу функции безопасной загрузки и отвечает за хранение зашифрованных данных. В Apple T2 интегрировано множество дискретных контроллеров, в том числе контроллер управления системой, аудиоконтроллер и SSD-контроллер. Кроме того, чип поддерживает функцию «Привет, Siri» на MacBook Pro - ваш голосовой помощник всегда готов открыть приложение, найти документ, включить музыку или ответить на вопрос.
<p>Самый мощный и универсальный порт
Thunderbolt 3 объединил высочайшую пропускную способность с функциональностью стандартного интерфейса USB‑C. В итоге получился высокоскоростной и универсальный порт. Он позволяет передавать данные, заряжать устройства и выводить видео на дополнительные мониторы через один и тот же разъём. При этом его скорость в два раза выше, чем у Thunderbolt 2 - до 40 Гбит/с. MacBook Pro 13 дюймов имеет до четырёх таких портов, чтобы можно было подключаться с обеих сторон. А подсоединить ваши устройства можно всего одним кабелем или кабелем с адаптером. Поскольку порт Thunderbolt 3 симметричен, то при подключении не нужно думать, где у него верх, а где низ.
<p>Больше функций в одно касание
Очень долго верхнюю строку клавиатуры занимали функциональные клавиши. Мы заменили их более универсальным и удобным элементом управления - сенсорной панелью Touch Bar. В зависимости от того, чем вы занимаетесь, на ней автоматически отображаются нужные инструменты - например, настройка громкости и яркости, функции управления фото и видео, эмодзи или предиктивный ввод текста.
<p>Всё, что нужно. Там, где нужно
Панель Touch Bar поддерживает многие встроенные приложения на вашем MacBook Pro и помогает быстрее ответить на письма, отформатировать документы и справиться с другими повседневными делами. Интерфейс Touch Bar меняется в зависимости от приложения, которое вы используете. Таким образом, необходимые вам инструменты всегда на видном месте. Кроме того, возможности Touch Bar можно использовать в приложениях сторонних разработчиков.
<table>
<tr>
<td>Количество ядер процессора</td>
<td>2</td>
</tr>
<tr>
<td>Тип видеокарты</td>
<td>встроенная</td>
</tr>
<tr>
<td>Видеокарта</td>
<td>Intel Iris Graphics 6100</td>
</tr>
<tr>
<td>Установленная ОС</td>
<td>macOS</td>
</tr>
<tr>
<td>Тип жесткого диска</td>
<td>SSD</td>
</tr>
<tr>
<td>Вес</td>
<td>1.58 кг</td>
</tr>
</table>', 815000, 34, '/uploads/03DFC4B.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (31, 'Ноутбук Apple MacBook Pro с дисплеем Retina', 'Процессор: Intel Core i5 / Частота процессора, ГГц: 2.3 / Объем памяти: 8 Гб / Жесткий диск: Отсутствует / SSD: 256 Гб / Диагональ экрана, дюйм: 13.3 / Разрешение экрана: 2560 x 1600 WQXGA / Операционная система: Mac OS Mojave / Вес: 1.3 кг', '<p>Новый уровень безопасности
Apple T2 - чип второго поколения для Mac, созданный для более надёжной защиты информации на вашем MacBook Pro. 13 дюймовая панель Touch Bar и оснащённая сопроцессором Secure Enclave, которая обеспечивает работу функции безопасной загрузки и отвечает за хранение зашифрованных данных. В Apple T2 интегрировано множество дискретных контроллеров, в том числе контроллер управления системой, аудиоконтроллер и SSD-контроллер. Кроме того, чип поддерживает функцию «Привет, Siri» на MacBook Pro - ваш голосовой помощник всегда готов открыть приложение, найти документ, включить музыку или ответить на вопрос.
<p>Самый мощный и универсальный порт
Thunderbolt 3 объединил высочайшую пропускную способность с функциональностью стандартного интерфейса USB‑C. В итоге получился высокоскоростной и универсальный порт. Он позволяет передавать данные, заряжать устройства и выводить видео на дополнительные мониторы через один и тот же разъём. При этом его скорость в два раза выше, чем у Thunderbolt 2 - до 40 Гбит/с. MacBook Pro 13 дюймов имеет до четырёх таких портов, чтобы можно было подключаться с обеих сторон. А подсоединить ваши устройства можно всего одним кабелем или кабелем с адаптером. Поскольку порт Thunderbolt 3 симметричен, то при подключении не нужно думать, где у него верх, а где низ.
<p>Больше функций в одно касание
Очень долго верхнюю строку клавиатуры занимали функциональные клавиши. Мы заменили их более универсальным и удобным элементом управления - сенсорной панелью Touch Bar. В зависимости от того, чем вы занимаетесь, на ней автоматически отображаются нужные инструменты - например, настройка громкости и яркости, функции управления фото и видео, эмодзи или предиктивный ввод текста.
<p>Всё, что нужно. Там, где нужно
Панель Touch Bar поддерживает многие встроенные приложения на вашем MacBook Pro и помогает быстрее ответить на письма, отформатировать документы и справиться с другими повседневными делами. Интерфейс Touch Bar меняется в зависимости от приложения, которое вы используете. Таким образом, необходимые вам инструменты всегда на видном месте. Кроме того, возможности Touch Bar можно использовать в приложениях сторонних разработчиков.
<table>
<tr>
<td>Количество ядер процессора</td>
<td>2</td>
</tr>
<tr>
<td>Тип видеокарты</td>
<td>встроенная</td>
</tr>
<tr>
<td>Видеокарта</td>
<td>Intel Iris Graphics 6100</td>
</tr>
<tr>
<td>Установленная ОС</td>
<td>macOS</td>
</tr>
<tr>
<td>Тип жесткого диска</td>
<td>SSD</td>
</tr>
<tr>
<td>Вес</td>
<td>1.58 кг</td>
</tr>
</table>', 815000, 34, '/uploads/75EA10D.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (30, 'Ноутбук Apple MacBook Pro с дисплеем Retina', 'Процессор: Intel Core i5 / Частота процессора, ГГц: 2.3 / Объем памяти: 8 Гб / Жесткий диск: Отсутствует / SSD: 256 Гб / Диагональ экрана, дюйм: 13.3 / Разрешение экрана: 2560 x 1600 WQXGA / Операционная система: Mac OS Mojave / Вес: 1.3 кг', '<p>Новый уровень безопасности
Apple T2 - чип второго поколения для Mac, созданный для более надёжной защиты информации на вашем MacBook Pro. 13 дюймовая панель Touch Bar и оснащённая сопроцессором Secure Enclave, которая обеспечивает работу функции безопасной загрузки и отвечает за хранение зашифрованных данных. В Apple T2 интегрировано множество дискретных контроллеров, в том числе контроллер управления системой, аудиоконтроллер и SSD-контроллер. Кроме того, чип поддерживает функцию «Привет, Siri» на MacBook Pro - ваш голосовой помощник всегда готов открыть приложение, найти документ, включить музыку или ответить на вопрос.
<p>Самый мощный и универсальный порт
Thunderbolt 3 объединил высочайшую пропускную способность с функциональностью стандартного интерфейса USB‑C. В итоге получился высокоскоростной и универсальный порт. Он позволяет передавать данные, заряжать устройства и выводить видео на дополнительные мониторы через один и тот же разъём. При этом его скорость в два раза выше, чем у Thunderbolt 2 - до 40 Гбит/с. MacBook Pro 13 дюймов имеет до четырёх таких портов, чтобы можно было подключаться с обеих сторон. А подсоединить ваши устройства можно всего одним кабелем или кабелем с адаптером. Поскольку порт Thunderbolt 3 симметричен, то при подключении не нужно думать, где у него верх, а где низ.
<p>Больше функций в одно касание
Очень долго верхнюю строку клавиатуры занимали функциональные клавиши. Мы заменили их более универсальным и удобным элементом управления - сенсорной панелью Touch Bar. В зависимости от того, чем вы занимаетесь, на ней автоматически отображаются нужные инструменты - например, настройка громкости и яркости, функции управления фото и видео, эмодзи или предиктивный ввод текста.
<p>Всё, что нужно. Там, где нужно
Панель Touch Bar поддерживает многие встроенные приложения на вашем MacBook Pro и помогает быстрее ответить на письма, отформатировать документы и справиться с другими повседневными делами. Интерфейс Touch Bar меняется в зависимости от приложения, которое вы используете. Таким образом, необходимые вам инструменты всегда на видном месте. Кроме того, возможности Touch Bar можно использовать в приложениях сторонних разработчиков.
<table>
<tr>
<td>Количество ядер процессора</td>
<td>2</td>
</tr>
<tr>
<td>Тип видеокарты</td>
<td>встроенная</td>
</tr>
<tr>
<td>Видеокарта</td>
<td>Intel Iris Graphics 6100</td>
</tr>
<tr>
<td>Установленная ОС</td>
<td>macOS</td>
</tr>
<tr>
<td>Тип жесткого диска</td>
<td>SSD</td>
</tr>
<tr>
<td>Вес</td>
<td>1.58 кг</td>
</tr>
</table>', 815000, 34, '/uploads/56E4D6A.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (35, 'Процессор Intel Core i3 8300, LGA1151, OEM', 'Тип процессора: Intel Core i3 / Сокет: LGA1151 / Количество ядер: 4 ядра / Количество потоков: 4 / Тактовая частота, ГГц: 3.7 / Микроархитектура: Coffee Lake / Объем кэша L3: 8 Мб / Интегрированная графическая система: Intel UHD Graphics 630', '<p>Подробные характеристики<br/>
Socket
LGA1151 v2<br/>
Объем кэша L3
8192 КБ<br/>
Количество ядер
4 <br/>
Частота процессора
3700 МГц<br/>
Интегрированное графическое ядро
да<br/>
Комплектация
BOX, OEM<br/>
<p>Общие характеристики<br/>
Socket
LGA1151 v2<br/>
Игровой
есть<br/>
<p>Ядро
Ядро
Coffee Lake<br/>
Количество ядер
4<br/>
Техпроцесс
14 нм<br/>
<p>Частотные характеристики<br/>
Тактовая частота
3700 МГц<br/>
Количестве потоков
4<br/>
Системная шина
DMI<br/>
Коэффициент умножения
37<br/>
Интегрированное графическое ядро
UHD 630, 1150 МГц<br/>
Встроенный контроллер памяти
есть, полоса 37.5 ГБ/с<br/>
Максимальный объем памяти
64 ГБ<br/>
Тип памяти
DDR4-2400<br/>
Максимальное количество каналов памяти
2<br/>
<p>Кэш<br/>
Объем кэша L1
64 КБ<br/>
Объем кэша L2
1024 КБ<br/>
Объем кэша L3
8192 КБ<br/>
<p>Наборы команд<br/>
Инструкции
MMX, SSE, SSE2, SSE3, SSE4, AVX, AVX2<br/>
Поддержка AMD64/EM64T<br/>
есть<br/>
Поддержка NX Bit
есть<br/>
Поддержка Virtualization Technology
есть<br/>
<p>Дополнительно<br/>
Типичное тепловыделение
62 Вт<br/>
Максимальная рабочая температура
100 °C<br/>
Макс. кол-во каналов PCI Express
16<br/>
Дополнительная информация
обращаем внимание, что данный процессор совместим только с чипсетами 3xx серии<br/>', 75100, 32, '/uploads/EC5CF64.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (34, 'Процессор Intel Core i3 8300, LGA1151, OEM', 'Тип процессора: Intel Core i3 / Сокет: LGA1151 / Количество ядер: 4 ядра / Количество потоков: 4 / Тактовая частота, ГГц: 3.7 / Микроархитектура: Coffee Lake / Объем кэша L3: 8 Мб / Интегрированная графическая система: Intel UHD Graphics 630', '<p>Подробные характеристики<br/>
Socket
LGA1151 v2<br/>
Объем кэша L3
8192 КБ<br/>
Количество ядер
4 <br/>
Частота процессора
3700 МГц<br/>
Интегрированное графическое ядро
да<br/>
Комплектация
BOX, OEM<br/>
<p>Общие характеристики<br/>
Socket
LGA1151 v2<br/>
Игровой
есть<br/>
<p>Ядро
Ядро
Coffee Lake<br/>
Количество ядер
4<br/>
Техпроцесс
14 нм<br/>
<p>Частотные характеристики<br/>
Тактовая частота
3700 МГц<br/>
Количестве потоков
4<br/>
Системная шина
DMI<br/>
Коэффициент умножения
37<br/>
Интегрированное графическое ядро
UHD 630, 1150 МГц<br/>
Встроенный контроллер памяти
есть, полоса 37.5 ГБ/с<br/>
Максимальный объем памяти
64 ГБ<br/>
Тип памяти
DDR4-2400<br/>
Максимальное количество каналов памяти
2<br/>
<p>Кэш<br/>
Объем кэша L1
64 КБ<br/>
Объем кэша L2
1024 КБ<br/>
Объем кэша L3
8192 КБ<br/>
<p>Наборы команд<br/>
Инструкции
MMX, SSE, SSE2, SSE3, SSE4, AVX, AVX2<br/>
Поддержка AMD64/EM64T<br/>
есть<br/>
Поддержка NX Bit
есть<br/>
Поддержка Virtualization Technology
есть<br/>
<p>Дополнительно<br/>
Типичное тепловыделение
62 Вт<br/>
Максимальная рабочая температура
100 °C<br/>
Макс. кол-во каналов PCI Express
16<br/>
Дополнительная информация
обращаем внимание, что данный процессор совместим только с чипсетами 3xx серии<br/>', 75100, 32, '/uploads/CD412B5.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (40, 'Видеокарта PCI-E 8192Mb PNY GTX 1080, GeForce GTX1080', 'Модель чипсета: GeForce GTX1080 / Частота видеопроцессора (GPU): 1607 MHz (базовая) - 1733 MHz (разогнанная) / Частота видеопамяти, МГц: 10000 / Тип видеопамяти: GDDR5X / Объем видеопамяти: 8 Гб / Разрядность шины видеопамяти: 256 бит', '<p>Описание Видеокарта PCI-E 8192Mb PNY GTX 1080, GeForce GTX1080<br/>
Революционный дизайн
Новая видеокарта GeForce GTX 1080 искусно изготовлена из материалов класса премиум качества таким образом, чтобы максимально отводить тепло за счет камер испарения. Она работает так же хорошо, как и выглядит.

<p>Поддержка VR<br/>
Откройте для себя виртуальную реальность нового поколения, низкие задержки и совместимость plug-and-play с лучшими VR-гарнитурами - все это благодаря технологиям NVIDIA VRWorks. Звуковое сопровождение, физика и тактильные ощущения в VR позволят вам прочувствовать каждый момент игрового процесса.

<p>Самые современные передовые технологии<br/>
Архитектура Pascal удовлетворяет требованиям дисплеев нового поколения, включая VR-дисплеи и дисплеи ультравысокого разрешения, поддерживая подключение нескольких мониторов. Она оснащена технологиями NVIDIA GameWorks, которые обеспечивают плавный и кинематографичный игровой процесс. Кроме того, она поддерживает революционную технологию для записи изображения с охватом в 360 градусов.

<p>Производительность<br/>
Видеокарты на базе архитектуры Pascal обеспечивают высочайшую производительность и энергоэффективность. Они созданы с использованием сверскоростной технологии FinFET и поддерживают DirectX 12 для самого быстрого, плавного и энергоэффективного игрового процесса. GeForce GTX 1080, флагманский GPU с архитектурой Pascal, имеет высокоскоростную память GDDR5X (G5X) для обеспечения невероятных игровых возможностей.

<p>Самая современная архитектура GPU<br/>
Видеокарты серии GeForce GTX 10 созданы на основе архитектуры Pascal и обеспечивают увеличение производительности до 3-х раз по сравнению с видеокартами предыдущего поколения, а также они поддерживают новые игровые технологии и революционные возможности VR.

<table>
<tr>
<td>Модель</td>
<td>GTX 1080</td>
</tr>
<tr>
<td>Тип видеопамяти</td>
<td>GDDR5X</td>
</tr>
<tr>
<td>Объем видеопамяти</td>
<td>8 Гб</td>
</tr>
<tr>
<td>Минимальная мощность блока питания, не менее</td>
<td>500 Вт</td>
</tr>
<tr>
<td>Длина видеокарты</td>
<td>28 см</td>
</tr>
<tr>
<td>Срок гарантии (мес.)</td>
<td>12</td>
</tr>
</table>', 223600, 33, '/uploads/F2E23EA.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (42, 'Видеокарта PCI-E 8192Mb PNY GTX 1080, GeForce GTX1080', 'Модель чипсета: GeForce GTX1080 / Частота видеопроцессора (GPU): 1607 MHz (базовая) - 1733 MHz (разогнанная) / Частота видеопамяти, МГц: 10000 / Тип видеопамяти: GDDR5X / Объем видеопамяти: 8 Гб / Разрядность шины видеопамяти: 256 бит', '<p>Описание Видеокарта PCI-E 8192Mb PNY GTX 1080, GeForce GTX1080<br/>
Революционный дизайн
Новая видеокарта GeForce GTX 1080 искусно изготовлена из материалов класса премиум качества таким образом, чтобы максимально отводить тепло за счет камер испарения. Она работает так же хорошо, как и выглядит.

<p>Поддержка VR<br/>
Откройте для себя виртуальную реальность нового поколения, низкие задержки и совместимость plug-and-play с лучшими VR-гарнитурами - все это благодаря технологиям NVIDIA VRWorks. Звуковое сопровождение, физика и тактильные ощущения в VR позволят вам прочувствовать каждый момент игрового процесса.

<p>Самые современные передовые технологии<br/>
Архитектура Pascal удовлетворяет требованиям дисплеев нового поколения, включая VR-дисплеи и дисплеи ультравысокого разрешения, поддерживая подключение нескольких мониторов. Она оснащена технологиями NVIDIA GameWorks, которые обеспечивают плавный и кинематографичный игровой процесс. Кроме того, она поддерживает революционную технологию для записи изображения с охватом в 360 градусов.

<p>Производительность<br/>
Видеокарты на базе архитектуры Pascal обеспечивают высочайшую производительность и энергоэффективность. Они созданы с использованием сверскоростной технологии FinFET и поддерживают DirectX 12 для самого быстрого, плавного и энергоэффективного игрового процесса. GeForce GTX 1080, флагманский GPU с архитектурой Pascal, имеет высокоскоростную память GDDR5X (G5X) для обеспечения невероятных игровых возможностей.

<p>Самая современная архитектура GPU<br/>
Видеокарты серии GeForce GTX 10 созданы на основе архитектуры Pascal и обеспечивают увеличение производительности до 3-х раз по сравнению с видеокартами предыдущего поколения, а также они поддерживают новые игровые технологии и революционные возможности VR.

<table>
<tr>
<td>Модель</td>
<td>GTX 1080</td>
</tr>
<tr>
<td>Тип видеопамяти</td>
<td>GDDR5X</td>
</tr>
<tr>
<td>Объем видеопамяти</td>
<td>8 Гб</td>
</tr>
<tr>
<td>Минимальная мощность блока питания, не менее</td>
<td>500 Вт</td>
</tr>
<tr>
<td>Длина видеокарты</td>
<td>28 см</td>
</tr>
<tr>
<td>Срок гарантии (мес.)</td>
<td>12</td>
</tr>
</table>', 223600, 33, '/uploads/38CBC15.jpg');
INSERT INTO product (id, name, short_description, description, price, category_id, image_url) VALUES (41, 'Видеокарта PCI-E 4096Mb PNY GTX 960, GeForce GTX1080', 'Модель чипсета: GeForce GTX960 / Частота видеопроцессора (GPU): 1607 MHz (базовая) - 1733 MHz (разогнанная) / Частота видеопамяти, МГц: 10000 / Тип видеопамяти: GDDR5X / Объем видеопамяти: 8 Гб / Разрядность шины видеопамяти: 256 бит', '<p>Описание Видеокарта PCI-E 8192Mb PNY GTX 1080, GeForce GTX1080<br/>
Революционный дизайн
Новая видеокарта GeForce GTX 1080 искусно изготовлена из материалов класса премиум качества таким образом, чтобы максимально отводить тепло за счет камер испарения. Она работает так же хорошо, как и выглядит.

<p>Поддержка VR<br/>
Откройте для себя виртуальную реальность нового поколения, низкие задержки и совместимость plug-and-play с лучшими VR-гарнитурами - все это благодаря технологиям NVIDIA VRWorks. Звуковое сопровождение, физика и тактильные ощущения в VR позволят вам прочувствовать каждый момент игрового процесса.

<p>Самые современные передовые технологии<br/>
Архитектура Pascal удовлетворяет требованиям дисплеев нового поколения, включая VR-дисплеи и дисплеи ультравысокого разрешения, поддерживая подключение нескольких мониторов. Она оснащена технологиями NVIDIA GameWorks, которые обеспечивают плавный и кинематографичный игровой процесс. Кроме того, она поддерживает революционную технологию для записи изображения с охватом в 360 градусов.

<p>Производительность<br/>
Видеокарты на базе архитектуры Pascal обеспечивают высочайшую производительность и энергоэффективность. Они созданы с использованием сверскоростной технологии FinFET и поддерживают DirectX 12 для самого быстрого, плавного и энергоэффективного игрового процесса. GeForce GTX 1080, флагманский GPU с архитектурой Pascal, имеет высокоскоростную память GDDR5X (G5X) для обеспечения невероятных игровых возможностей.

<p>Самая современная архитектура GPU<br/>
Видеокарты серии GeForce GTX 10 созданы на основе архитектуры Pascal и обеспечивают увеличение производительности до 3-х раз по сравнению с видеокартами предыдущего поколения, а также они поддерживают новые игровые технологии и революционные возможности VR.

<table>
<tr>
<td>Модель</td>
<td>GTX 1080</td>
</tr>
<tr>
<td>Тип видеопамяти</td>
<td>GDDR5X</td>
</tr>
<tr>
<td>Объем видеопамяти</td>
<td>8 Гб</td>
</tr>
<tr>
<td>Минимальная мощность блока питания, не менее</td>
<td>500 Вт</td>
</tr>
<tr>
<td>Длина видеокарты</td>
<td>28 см</td>
</tr>
<tr>
<td>Срок гарантии (мес.)</td>
<td>12</td>
</tr>
</table>', 165600, 33, '/uploads/6B8393C.jpg');


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO "user" (id, email, login, password, role_id, balance) VALUES (31, 'user@gmail.com', 'user1', '95C946BF622EF93B0A211CD0FD028DFDFCF7E39E', 1, 9.00);
INSERT INTO "user" (id, email, login, password, role_id, balance) VALUES (25, 'admin@gmail.com', 'admin', 'F865B53623B121FD34EE5426C792E5C33AF8C227', 2, 7847704.00);


--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO user_role (id, name) VALUES (2, 'Admin');
INSERT INTO user_role (id, name) VALUES (1, 'User');


--
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('category_id_seq', 38, true);


--
-- Name: customer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('customer_id_seq', 31, true);


--
-- Name: customer_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('customer_role_id_seq', 2, true);


--
-- Name: delivery_profile_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('delivery_profile_id_seq', 34, true);


--
-- Name: order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('order_id_seq', 41, true);


--
-- Name: order_status_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('order_status_id_seq', 5, true);


--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('product_id_seq', 43, true);


--
-- Name: lang Lang_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY lang
    ADD CONSTRAINT "Lang_pkey" PRIMARY KEY (code);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: user customer_login_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT customer_login_key UNIQUE (login);


--
-- Name: user customer_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- Name: user_role customer_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT customer_role_pkey PRIMARY KEY (id);


--
-- Name: delivery_profile delivery_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY delivery_profile
    ADD CONSTRAINT delivery_profile_pkey PRIMARY KEY (id);


--
-- Name: order order_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_pkey PRIMARY KEY (id);


--
-- Name: order_product order_product_order_id_product_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY order_product
    ADD CONSTRAINT order_product_order_id_product_id_key UNIQUE (order_id, product_id);


--
-- Name: order_status order_status_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY order_status
    ADD CONSTRAINT order_status_pkey PRIMARY KEY (id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: category category_parent_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES category(id);


--
-- Name: category_translate category_translate_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY category_translate
    ADD CONSTRAINT category_translate_category_id_fkey FOREIGN KEY (category_id) REFERENCES category(id);


--
-- Name: category_translate category_translate_lang_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY category_translate
    ADD CONSTRAINT category_translate_lang_code_fkey FOREIGN KEY (lang_code) REFERENCES lang(code);


--
-- Name: user customer_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT customer_role_id_fkey FOREIGN KEY (role_id) REFERENCES user_role(id);


--
-- Name: delivery_profile delivery_profile_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY delivery_profile
    ADD CONSTRAINT delivery_profile_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: order order_delivery_profile_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_delivery_profile_id_fkey FOREIGN KEY (delivery_profile_id) REFERENCES delivery_profile(id);


--
-- Name: order_product order_product_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY order_product
    ADD CONSTRAINT order_product_order_id_fkey FOREIGN KEY (order_id) REFERENCES "order"(id);


--
-- Name: order_product order_product_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY order_product
    ADD CONSTRAINT order_product_product_id_fkey FOREIGN KEY (product_id) REFERENCES product(id);


--
-- Name: order order_status_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_status_id_fkey FOREIGN KEY (status_id) REFERENCES order_status(id);


--
-- Name: order order_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: product product_category_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_category_id_fkey FOREIGN KEY (category_id) REFERENCES category(id);


--
-- PostgreSQL database dump complete
--