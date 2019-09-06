--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.11
-- Dumped by pg_dump version 9.5.6

-- Started on 2017-05-19 19:02:10 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 11787)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2117 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 205 (class 1255 OID 1258439)
-- Name: remove_candidate(character varying); Type: FUNCTION; Schema: public; Owner: db17_lesakjan
--

CREATE FUNCTION remove_candidate(ident character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
      DELETE FROM candidate WHERE user_email = ident;
    END;
    $$;


ALTER FUNCTION public.remove_candidate(ident character varying) OWNER TO db17_lesakjan;

--
-- TOC entry 206 (class 1255 OID 1258550)
-- Name: remove_company(character varying); Type: FUNCTION; Schema: public; Owner: db17_lesakjan
--

CREATE FUNCTION remove_company(ident character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
    BEGIN
      DELETE FROM company WHERE user_email = ident;
    END;
    $$;


ALTER FUNCTION public.remove_company(ident character varying) OWNER TO db17_lesakjan;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 909977)
-- Name: _user; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE _user (
    id integer NOT NULL,
    email character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    firstname character varying(255) NOT NULL,
    lastname character varying(255) NOT NULL,
    birth date NOT NULL,
    active boolean DEFAULT false NOT NULL,
    CONSTRAINT valid_mail CHECK (((email)::text ~~ '%@%'::text))
);


ALTER TABLE _user OWNER TO db17_lesakjan;

--
-- TOC entry 171 (class 1259 OID 909975)
-- Name: _user_id_seq; Type: SEQUENCE; Schema: public; Owner: db17_lesakjan
--

CREATE SEQUENCE _user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE _user_id_seq OWNER TO db17_lesakjan;

--
-- TOC entry 2118 (class 0 OID 0)
-- Dependencies: 171
-- Name: _user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: db17_lesakjan
--

ALTER SEQUENCE _user_id_seq OWNED BY _user.id;


--
-- TOC entry 174 (class 1259 OID 909993)
-- Name: candidate; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE candidate (
    id integer NOT NULL,
    user_email character varying(255) NOT NULL,
    job_title character varying(255) NOT NULL,
    looking_for_job boolean DEFAULT true NOT NULL,
    description character varying(255)
);


ALTER TABLE candidate OWNER TO db17_lesakjan;

--
-- TOC entry 181 (class 1259 OID 910132)
-- Name: candidate_has_skill; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE candidate_has_skill (
    candidate_id integer NOT NULL,
    skill_name character varying(255) NOT NULL
);


ALTER TABLE candidate_has_skill OWNER TO db17_lesakjan;

--
-- TOC entry 173 (class 1259 OID 909991)
-- Name: candidate_id_seq; Type: SEQUENCE; Schema: public; Owner: db17_lesakjan
--

CREATE SEQUENCE candidate_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE candidate_id_seq OWNER TO db17_lesakjan;

--
-- TOC entry 2119 (class 0 OID 0)
-- Dependencies: 173
-- Name: candidate_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: db17_lesakjan
--

ALTER SEQUENCE candidate_id_seq OWNED BY candidate.id;


--
-- TOC entry 183 (class 1259 OID 911403)
-- Name: company; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE company (
    id integer NOT NULL,
    ico character varying(8) NOT NULL,
    description text NOT NULL,
    website character varying(255),
    user_email character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE company OWNER TO db17_lesakjan;

--
-- TOC entry 182 (class 1259 OID 911401)
-- Name: company_id_seq; Type: SEQUENCE; Schema: public; Owner: db17_lesakjan
--

CREATE SEQUENCE company_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE company_id_seq OWNER TO db17_lesakjan;

--
-- TOC entry 2120 (class 0 OID 0)
-- Dependencies: 182
-- Name: company_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: db17_lesakjan
--

ALTER SEQUENCE company_id_seq OWNED BY company.id;


--
-- TOC entry 180 (class 1259 OID 910116)
-- Name: post; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE post (
    id integer NOT NULL,
    title character varying(255) NOT NULL,
    date date NOT NULL,
    company_ico character varying(8) NOT NULL,
    description text NOT NULL
);


ALTER TABLE post OWNER TO db17_lesakjan;

--
-- TOC entry 192 (class 1259 OID 1258812)
-- Name: company_post_view; Type: VIEW; Schema: public; Owner: db17_lesakjan
--

CREATE VIEW company_post_view AS
 SELECT c.name,
    c.ico,
    c.website,
    c.user_email,
    c.description,
    p.title AS post_title,
    p.description AS post_description,
    p.date AS post_date
   FROM (company c
     LEFT JOIN post p ON (((c.ico)::text = (p.company_ico)::text)));


ALTER TABLE company_post_view OWNER TO db17_lesakjan;

--
-- TOC entry 187 (class 1259 OID 911463)
-- Name: experience; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE experience (
    id integer NOT NULL,
    candidate_id integer NOT NULL,
    title character varying(255) NOT NULL,
    "position" character varying(255) NOT NULL,
    date_from date NOT NULL,
    date_to date,
    description text,
    experience_type character varying(255),
    company character varying(255) NOT NULL,
    CONSTRAINT valid_date CHECK (((date_from <= date_to) OR (date_to IS NULL)))
);


ALTER TABLE experience OWNER TO db17_lesakjan;

--
-- TOC entry 186 (class 1259 OID 911461)
-- Name: experience_id_seq; Type: SEQUENCE; Schema: public; Owner: db17_lesakjan
--

CREATE SEQUENCE experience_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE experience_id_seq OWNER TO db17_lesakjan;

--
-- TOC entry 2121 (class 0 OID 0)
-- Dependencies: 186
-- Name: experience_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: db17_lesakjan
--

ALTER SEQUENCE experience_id_seq OWNED BY experience.id;


--
-- TOC entry 185 (class 1259 OID 911451)
-- Name: experience_type; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE experience_type (
    id integer NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE experience_type OWNER TO db17_lesakjan;

--
-- TOC entry 184 (class 1259 OID 911449)
-- Name: experience_type_id_seq; Type: SEQUENCE; Schema: public; Owner: db17_lesakjan
--

CREATE SEQUENCE experience_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE experience_type_id_seq OWNER TO db17_lesakjan;

--
-- TOC entry 2122 (class 0 OID 0)
-- Dependencies: 184
-- Name: experience_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: db17_lesakjan
--

ALTER SEQUENCE experience_type_id_seq OWNED BY experience_type.id;


--
-- TOC entry 176 (class 1259 OID 910042)
-- Name: file; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE file (
    id integer NOT NULL,
    path character varying(512) NOT NULL,
    name character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    _user integer NOT NULL
);


ALTER TABLE file OWNER TO db17_lesakjan;

--
-- TOC entry 175 (class 1259 OID 910040)
-- Name: file_id_seq; Type: SEQUENCE; Schema: public; Owner: db17_lesakjan
--

CREATE SEQUENCE file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE file_id_seq OWNER TO db17_lesakjan;

--
-- TOC entry 2123 (class 0 OID 0)
-- Dependencies: 175
-- Name: file_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: db17_lesakjan
--

ALTER SEQUENCE file_id_seq OWNED BY file.id;


--
-- TOC entry 178 (class 1259 OID 910095)
-- Name: message; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE message (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    content text NOT NULL,
    date date NOT NULL,
    company_ico character varying(8) NOT NULL,
    candidate_id integer NOT NULL
);


ALTER TABLE message OWNER TO db17_lesakjan;

--
-- TOC entry 177 (class 1259 OID 910093)
-- Name: message_id_seq; Type: SEQUENCE; Schema: public; Owner: db17_lesakjan
--

CREATE SEQUENCE message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE message_id_seq OWNER TO db17_lesakjan;

--
-- TOC entry 2124 (class 0 OID 0)
-- Dependencies: 177
-- Name: message_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: db17_lesakjan
--

ALTER SEQUENCE message_id_seq OWNED BY message.id;


--
-- TOC entry 179 (class 1259 OID 910114)
-- Name: post_id_seq; Type: SEQUENCE; Schema: public; Owner: db17_lesakjan
--

CREATE SEQUENCE post_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE post_id_seq OWNER TO db17_lesakjan;

--
-- TOC entry 2125 (class 0 OID 0)
-- Dependencies: 179
-- Name: post_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: db17_lesakjan
--

ALTER SEQUENCE post_id_seq OWNED BY post.id;


--
-- TOC entry 190 (class 1259 OID 912540)
-- Name: skill; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE skill (
    name character varying(255) NOT NULL,
    s_group character varying NOT NULL
);


ALTER TABLE skill OWNER TO db17_lesakjan;

--
-- TOC entry 189 (class 1259 OID 912530)
-- Name: skill_group; Type: TABLE; Schema: public; Owner: db17_lesakjan
--

CREATE TABLE skill_group (
    id integer NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE skill_group OWNER TO db17_lesakjan;

--
-- TOC entry 188 (class 1259 OID 912528)
-- Name: skill_group_id_seq; Type: SEQUENCE; Schema: public; Owner: db17_lesakjan
--

CREATE SEQUENCE skill_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE skill_group_id_seq OWNER TO db17_lesakjan;

--
-- TOC entry 2126 (class 0 OID 0)
-- Dependencies: 188
-- Name: skill_group_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: db17_lesakjan
--

ALTER SEQUENCE skill_group_id_seq OWNED BY skill_group.id;


--
-- TOC entry 191 (class 1259 OID 1250341)
-- Name: skillset; Type: VIEW; Schema: public; Owner: db17_lesakjan
--

CREATE VIEW skillset AS
 SELECT s.name AS skillname,
    sg.name AS groupname
   FROM (skill s
     LEFT JOIN skill_group sg ON (((s.s_group)::text = (sg.name)::text)));


ALTER TABLE skillset OWNER TO db17_lesakjan;

--
-- TOC entry 1933 (class 2604 OID 909980)
-- Name: id; Type: DEFAULT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY _user ALTER COLUMN id SET DEFAULT nextval('_user_id_seq'::regclass);


--
-- TOC entry 1936 (class 2604 OID 909996)
-- Name: id; Type: DEFAULT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY candidate ALTER COLUMN id SET DEFAULT nextval('candidate_id_seq'::regclass);


--
-- TOC entry 1941 (class 2604 OID 911406)
-- Name: id; Type: DEFAULT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY company ALTER COLUMN id SET DEFAULT nextval('company_id_seq'::regclass);


--
-- TOC entry 1943 (class 2604 OID 911466)
-- Name: id; Type: DEFAULT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY experience ALTER COLUMN id SET DEFAULT nextval('experience_id_seq'::regclass);


--
-- TOC entry 1942 (class 2604 OID 911454)
-- Name: id; Type: DEFAULT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY experience_type ALTER COLUMN id SET DEFAULT nextval('experience_type_id_seq'::regclass);


--
-- TOC entry 1938 (class 2604 OID 910045)
-- Name: id; Type: DEFAULT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY file ALTER COLUMN id SET DEFAULT nextval('file_id_seq'::regclass);


--
-- TOC entry 1939 (class 2604 OID 910098)
-- Name: id; Type: DEFAULT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY message ALTER COLUMN id SET DEFAULT nextval('message_id_seq'::regclass);


--
-- TOC entry 1940 (class 2604 OID 910119)
-- Name: id; Type: DEFAULT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY post ALTER COLUMN id SET DEFAULT nextval('post_id_seq'::regclass);


--
-- TOC entry 1945 (class 2604 OID 912533)
-- Name: id; Type: DEFAULT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY skill_group ALTER COLUMN id SET DEFAULT nextval('skill_group_id_seq'::regclass);


--
-- TOC entry 1947 (class 2606 OID 909990)
-- Name: _user_email_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY _user
    ADD CONSTRAINT _user_email_key UNIQUE (email);


--
-- TOC entry 1949 (class 2606 OID 909988)
-- Name: _user_id_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY _user
    ADD CONSTRAINT _user_id_key UNIQUE (id);


--
-- TOC entry 1951 (class 2606 OID 909986)
-- Name: _user_pkey; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY _user
    ADD CONSTRAINT _user_pkey PRIMARY KEY (id, email);


--
-- TOC entry 1953 (class 2606 OID 910003)
-- Name: candidate_id_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY candidate
    ADD CONSTRAINT candidate_id_key UNIQUE (id);


--
-- TOC entry 1966 (class 2606 OID 910136)
-- Name: candidates_skill; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY candidate_has_skill
    ADD CONSTRAINT candidates_skill PRIMARY KEY (candidate_id, skill_name);


--
-- TOC entry 1968 (class 2606 OID 911415)
-- Name: company_ico_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY company
    ADD CONSTRAINT company_ico_key UNIQUE (ico);


--
-- TOC entry 1970 (class 2606 OID 911413)
-- Name: company_id_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY company
    ADD CONSTRAINT company_id_key UNIQUE (id);


--
-- TOC entry 1972 (class 2606 OID 911411)
-- Name: company_pkey; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY company
    ADD CONSTRAINT company_pkey PRIMARY KEY (id, ico);


--
-- TOC entry 1980 (class 2606 OID 911472)
-- Name: experience_pkey; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY experience
    ADD CONSTRAINT experience_pkey PRIMARY KEY (id);


--
-- TOC entry 1974 (class 2606 OID 911458)
-- Name: experience_type_id_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY experience_type
    ADD CONSTRAINT experience_type_id_key UNIQUE (id);


--
-- TOC entry 1976 (class 2606 OID 1205260)
-- Name: experience_type_id_pk; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY experience_type
    ADD CONSTRAINT experience_type_id_pk PRIMARY KEY (id);


--
-- TOC entry 1978 (class 2606 OID 911460)
-- Name: experience_type_name_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY experience_type
    ADD CONSTRAINT experience_type_name_key UNIQUE (name);


--
-- TOC entry 1956 (class 2606 OID 910052)
-- Name: file_id_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY file
    ADD CONSTRAINT file_id_key UNIQUE (id);


--
-- TOC entry 1958 (class 2606 OID 910050)
-- Name: file_pkey; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY file
    ADD CONSTRAINT file_pkey PRIMARY KEY (id, path);


--
-- TOC entry 1960 (class 2606 OID 910103)
-- Name: message_pkey; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);


--
-- TOC entry 1962 (class 2606 OID 1251280)
-- Name: post_id; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY post
    ADD CONSTRAINT post_id PRIMARY KEY (id);


--
-- TOC entry 1964 (class 2606 OID 910126)
-- Name: post_id_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY post
    ADD CONSTRAINT post_id_key UNIQUE (id);


--
-- TOC entry 1982 (class 2606 OID 912537)
-- Name: skill_group_id_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY skill_group
    ADD CONSTRAINT skill_group_id_key UNIQUE (id);


--
-- TOC entry 1984 (class 2606 OID 912539)
-- Name: skill_group_name_key; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY skill_group
    ADD CONSTRAINT skill_group_name_key UNIQUE (name);


--
-- TOC entry 1986 (class 2606 OID 912535)
-- Name: skill_group_pkey; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY skill_group
    ADD CONSTRAINT skill_group_pkey PRIMARY KEY (id, name);


--
-- TOC entry 1988 (class 2606 OID 912547)
-- Name: skill_pkey; Type: CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY skill
    ADD CONSTRAINT skill_pkey PRIMARY KEY (name);


--
-- TOC entry 1954 (class 1259 OID 1211932)
-- Name: candidate_user_email_uindex; Type: INDEX; Schema: public; Owner: db17_lesakjan
--

CREATE UNIQUE INDEX candidate_user_email_uindex ON candidate USING btree (user_email);


--
-- TOC entry 2108 (class 2618 OID 1258831)
-- Name: company_post_view_ins; Type: RULE; Schema: public; Owner: db17_lesakjan
--

CREATE RULE company_post_view_ins AS
    ON INSERT TO company_post_view DO INSTEAD ( INSERT INTO company (ico, description, website, user_email, name)
  VALUES (new.ico, new.description, new.website, new.user_email, new.name);
 INSERT INTO post (title, date, company_ico, description)
  VALUES (new.post_title, now(), new.ico, new.post_description);
);


--
-- TOC entry 2109 (class 2618 OID 1258845)
-- Name: company_post_view_update; Type: RULE; Schema: public; Owner: db17_lesakjan
--

CREATE RULE company_post_view_update AS
    ON UPDATE TO company_post_view DO INSTEAD ( UPDATE company SET ico = new.ico, description = new.description, website = new.website, user_email = new.user_email, name = new.name
  WHERE ((company.ico)::text = (new.ico)::text);
 UPDATE post SET title = new.post_title, date = now(), company_ico = new.ico, description = new.post_description
  WHERE ((post.company_ico)::text = (new.ico)::text);
);


--
-- TOC entry 1994 (class 2606 OID 1257364)
-- Name: candidate_has_skill_candidate_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY candidate_has_skill
    ADD CONSTRAINT candidate_has_skill_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES candidate(id) ON DELETE CASCADE;


--
-- TOC entry 1993 (class 2606 OID 1250336)
-- Name: candidate_has_skill_skill_name_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY candidate_has_skill
    ADD CONSTRAINT candidate_has_skill_skill_name_fkey FOREIGN KEY (skill_name) REFERENCES skill(name);


--
-- TOC entry 1989 (class 2606 OID 1250331)
-- Name: candidate_user_email_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY candidate
    ADD CONSTRAINT candidate_user_email_fkey FOREIGN KEY (user_email) REFERENCES _user(email);


--
-- TOC entry 1995 (class 2606 OID 911416)
-- Name: company_user_email_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY company
    ADD CONSTRAINT company_user_email_fkey FOREIGN KEY (user_email) REFERENCES _user(email) ON DELETE CASCADE;


--
-- TOC entry 1996 (class 2606 OID 911473)
-- Name: experience_candidate_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY experience
    ADD CONSTRAINT experience_candidate_id_fkey FOREIGN KEY (candidate_id) REFERENCES candidate(id) ON DELETE CASCADE;


--
-- TOC entry 1997 (class 2606 OID 911478)
-- Name: experience_experience_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY experience
    ADD CONSTRAINT experience_experience_type_fkey FOREIGN KEY (experience_type) REFERENCES experience_type(name) ON DELETE SET NULL;


--
-- TOC entry 1990 (class 2606 OID 910053)
-- Name: file__user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY file
    ADD CONSTRAINT file__user_fkey FOREIGN KEY (_user) REFERENCES _user(id) ON DELETE CASCADE;


--
-- TOC entry 1991 (class 2606 OID 910109)
-- Name: message_candidate_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY message
    ADD CONSTRAINT message_candidate_fkey FOREIGN KEY (candidate_id) REFERENCES candidate(id) ON DELETE CASCADE;


--
-- TOC entry 1992 (class 2606 OID 1251281)
-- Name: post_company_ico_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY post
    ADD CONSTRAINT post_company_ico_fkey FOREIGN KEY (company_ico) REFERENCES company(ico);


--
-- TOC entry 1998 (class 2606 OID 912548)
-- Name: skill_s_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: db17_lesakjan
--

ALTER TABLE ONLY skill
    ADD CONSTRAINT skill_s_group_fkey FOREIGN KEY (s_group) REFERENCES skill_group(name);


--
-- TOC entry 2116 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2017-05-19 19:02:14 CEST

--
-- PostgreSQL database dump complete
--

