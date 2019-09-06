CREATE TABLE _user (
    id serial NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    active BIT(1) NOT NULL,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    birth DATE NOT NULL,
    PRIMARY KEY (id, email),
    CONSTRAINT valid_mail CHECK(email LIKE '%@%')
    );
 
CREATE TABLE candidate (
    id serial NOT NULL UNIQUE,
    user_email VARCHAR(255) NOT NULL,
    job_title VARCHAR(255) NOT NULL,
    looking_for_job BIT(1) NOT NULL,
    PRIMARY KEY (id, user_email),
    FOREIGN KEY (user_email) REFERENCES _user(email) ON DELETE CASCADE
    );

 CREATE TABLE experience_type (
    id serial NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE ,
    PRIMARY KEY (id, name)
    );

CREATE TABLE experience (
    id serial NOT NULL UNIQUE,
    candidate_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    date_from DATE NOT NULL,
    date_to DATE, 
    description TEXT,
    experience_type VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (candidate_id) REFERENCES candidate(id) ON DELETE CASCADE,
    FOREIGN KEY (experience_type) REFERENCES experience_type(name) ON DELETE SET NULL,

    CONSTRAINT valid_date CHECK((date_from <= date_to) OR (date_to IS NULL))
    );
 
CREATE TABLE file (
    id serial NOT NULL UNIQUE,
    path VARCHAR(512) NOT NULL,
    name VARCHAR(255) NOT NULL,
    filename VARCHAR(255) NOT NULL,
    _user INT NOT NULL,
    FOREIGN KEY (_user) REFERENCES _user(id) ON DELETE CASCADE,
    PRIMARY KEY (id, path)
    );
 

 
CREATE TABLE company (
    id serial NOT NULL UNIQUE,
    ico VARCHAR(8) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    website VARCHAR(255),
    user_email VARCHAR(255) NOT NULL,
    PRIMARY KEY (id, ico),
    FOREIGN KEY (user_email) REFERENCES _user(email) ON DELETE CASCADE
    );

CREATE TABLE skill_group (
    id serial NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id, name)
    );

CREATE TABLE skill (
    name VARCHAR(255) NOT NULL,
    s_group VARCHAR NOT NULL,
    FOREIGN KEY (s_group) REFERENCES skill_group(name),
    PRIMARY KEY (name)
    );
 
CREATE TABLE message (
    id serial NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    date DATE NOT NULL,
    company VARCHAR(8) NOT NULL,
    candidate INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (company) REFERENCES company(ico) ON DELETE CASCADE,
    FOREIGN KEY (candidate) REFERENCES candidate(id) ON DELETE CASCADE
    );
 
CREATE TABLE post (
    id serial NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    company VARCHAR(8) NOT NULL,
    description TEXT NOT NULL,
    PRIMARY KEY (id, title, company, date),
    FOREIGN KEY (company) REFERENCES company(ico) ON DELETE CASCADE
    );

CREATE TABLE candidate_has_skill (
    candidate_id INT NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (candidate_id) REFERENCES candidate(id),
    FOREIGN KEY (skill_name) REFERENCES skill(name),
    CONSTRAINT candidates_skill PRIMARY KEY (candidate_id, skill_name)
    );