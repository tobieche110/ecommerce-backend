--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: discount; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discount (
    id bigint NOT NULL,
    discount_amount double precision NOT NULL,
    discount_percentage double precision NOT NULL,
    free_cheapest_product_count integer NOT NULL,
    product_count_max integer NOT NULL,
    product_count_min integer NOT NULL,
    role_for_discount character varying(255),
    name character varying(255),
    CONSTRAINT discount_role_for_discount_check CHECK (((role_for_discount)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying, 'VIP'::character varying])::text[])))
);


ALTER TABLE public.discount OWNER TO postgres;

--
-- Name: discount_dates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discount_dates (
    discount_id bigint NOT NULL,
    valid_dates date
);


ALTER TABLE public.discount_dates OWNER TO postgres;

--
-- Name: discount_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.discount_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.discount_seq OWNER TO postgres;

--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id bigint NOT NULL,
    name character varying(255),
    price double precision NOT NULL,
    stock integer
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: product_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.product_seq OWNER TO postgres;

--
-- Name: sales_order_products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales_order_products (
    sales_order_id bigint NOT NULL,
    products_id bigint
);


ALTER TABLE public.sales_order_products OWNER TO postgres;

--
-- Name: sales_orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sales_orders (
    id bigint NOT NULL,
    total_price double precision NOT NULL,
    user_id bigint NOT NULL,
    date date
);


ALTER TABLE public.sales_orders OWNER TO postgres;

--
-- Name: sales_orders_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sales_orders_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sales_orders_seq OWNER TO postgres;

--
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."user" (
    id bigint NOT NULL,
    password character varying(255),
    role character varying(255),
    username character varying(255),
    date_when_became_vip date,
    date_when_lost_vip date,
    total_spent_last_month double precision
);


ALTER TABLE public."user" OWNER TO postgres;

--
-- Name: user_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_seq OWNER TO postgres;

--
-- Data for Name: discount; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.discount (id, discount_amount, discount_percentage, free_cheapest_product_count, product_count_max, product_count_min, role_for_discount, name) FROM stdin;
1	0	25	0	4	4	\N	4 productos
54	300	0	0	0	10	\N	Carrito Promocionable por fecha
55	500	0	1	0	10	VIP	Carrito VIP
102	100	0	0	0	10	\N	Carrito Comun
\.


--
-- Data for Name: discount_dates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.discount_dates (discount_id, valid_dates) FROM stdin;
54	2024-09-21
54	2024-09-22
54	2024-09-23
54	2024-09-24
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (id, name, price, stock) FROM stdin;
102	Mochila	1000	10
202	Casco de moto	5000	8
1	Blusa	200	6
253	Zapatillas	3000	20
\.


--
-- Data for Name: sales_order_products; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sales_order_products (sales_order_id, products_id) FROM stdin;
1052	253
1052	253
1052	253
1052	253
1052	253
1052	253
1052	253
1052	253
1052	253
1052	253
\.


--
-- Data for Name: sales_orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sales_orders (id, total_price, user_id, date) FROM stdin;
1052	29700	854	2024-09-23
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."user" (id, password, role, username, date_when_became_vip, date_when_lost_vip, total_spent_last_month) FROM stdin;
852	$2a$10$ArBcUBmKMNExuzUxRVpdyeuqTV2BHr.EouT6c0SbLYM15PlOhlAm.	ADMIN	Admin	\N	\N	0
853	$2a$10$.2pZkQ6kVb3B/ei8aGwvZ.xrr/7GLcF381lYcsY5r/lu1NuI6zhoe	USER	User	\N	\N	0
854	$2a$10$.CzLWp9ME.4VP6rehvePSu/oJhc9S9AB/NmvxdOuNRsktLsdus3IK	VIP	Vip	2024-09-23	\N	29700
\.


--
-- Name: discount_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discount_seq', 151, true);


--
-- Name: product_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_seq', 301, true);


--
-- Name: sales_orders_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sales_orders_seq', 1101, true);


--
-- Name: user_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_seq', 901, true);


--
-- Name: discount discount_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_pkey PRIMARY KEY (id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: sales_orders sales_orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_orders
    ADD CONSTRAINT sales_orders_pkey PRIMARY KEY (id);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: sales_order_products fk7ywpn7pp4axxweno4omr2tc2n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_order_products
    ADD CONSTRAINT fk7ywpn7pp4axxweno4omr2tc2n FOREIGN KEY (sales_order_id) REFERENCES public.sales_orders(id);


--
-- Name: sales_orders fkh1rmmwilw3dmwn3uu6jwfetlw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sales_orders
    ADD CONSTRAINT fkh1rmmwilw3dmwn3uu6jwfetlw FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: discount_dates fknopgpppfogsb1ae7tpni02lfo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_dates
    ADD CONSTRAINT fknopgpppfogsb1ae7tpni02lfo FOREIGN KEY (discount_id) REFERENCES public.discount(id);


--
-- PostgreSQL database dump complete
--

