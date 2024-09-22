-- Create the Client table
CREATE TABLE Client (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        address VARCHAR(255) NOT NULL,
                        phone VARCHAR(20) NOT NULL,
                        is_professional BOOLEAN NOT NULL
);
CREATE TYPE ProjectStatus AS ENUM ('Canceled', 'Confirmed','Pending');

-- Create the Project table
CREATE TABLE Project (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         profit_margin DECIMAL(5,2) NOT NULL,
                         total_cost DECIMAL(10,2) NOT NULL,
                         status ProjectStatus DEFAULT 'Pending',
                         client_id INTEGER REFERENCES Client(id)
);

-- Create the Component table (parent table for inheritance)
CREATE TABLE Component (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           type VARCHAR(20) NOT NULL,
                           unit_cost DECIMAL(10,2) NOT NULL,
                           quantity DECIMAL(10,2) NOT NULL,
                           vat_rate DECIMAL(5,2) NOT NULL,
                           project_id INTEGER REFERENCES Project(id)
);

-- Create the Material table (inherits from Component)
CREATE TABLE Material (
                          transport_cost DECIMAL(10,2) NOT NULL,
                          quality_coefficient DECIMAL(3,2) NOT NULL
) INHERITS (Component);

-- Create the Labor table (inherits from Component)
CREATE TABLE Labor (
                       hourly_rate DECIMAL(10,2) NOT NULL,
                       hours_worked DECIMAL(10,2) NOT NULL,
                       productivity_factor DECIMAL(3,2) NOT NULL
) INHERITS (Component);

-- Create the Quote table
CREATE TABLE Quote (
                       id SERIAL PRIMARY KEY,
                       estimated_amount DECIMAL(10,2) NOT NULL,
                       issue_date DATE NOT NULL,
                       validity_date DATE NOT NULL,
                       is_accepted BOOLEAN NOT NULL,
                       project_id INTEGER REFERENCES Project(id)
);
