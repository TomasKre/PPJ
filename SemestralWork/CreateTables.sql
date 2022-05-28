-- Drop database
ALTER TABLE City DROP CONSTRAINT City_Country
DROP TABLE City
DROP TABLE Country

-- Tables
-- Table: City
CREATE TABLE City (
    id int IDENTITY(1,1) NOT NULL,
    name nvarchar(64)  NOT NULL,
    country nchar(2)  NOT NULL,
    lon real  NOT NULL,
    lat real  NOT NULL,
    CONSTRAINT City_pk PRIMARY KEY  (id,country)
);

-- Table: Country
CREATE TABLE Country (
    country nchar(2)  NOT NULL,
    country_long nchar(3)  NULL,
    name nvarchar(64)  NULL,
    CONSTRAINT Country_pk PRIMARY KEY  (country)
);

-- Foreign keys
-- Reference: City_Country (table: City)
ALTER TABLE City ADD CONSTRAINT City_Country
    FOREIGN KEY (country)
    REFERENCES Country (country);