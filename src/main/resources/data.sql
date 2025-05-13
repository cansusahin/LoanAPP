INSERT INTO customer (name, surname, credit_limit, used_credit_limit) VALUES ('Cansu', 'Sahin', '1000000.0', '30000.0');
INSERT INTO customer (name, surname, credit_limit, used_credit_limit) VALUES ('Ozge', 'Sahin', '3000000.0', '50000.0');
INSERT INTO users (username, password, role, customer_id) VALUES ('cansusahin', '$2a$12$G0taA68wbLDGnGwHSKsrYOYQZjxzp9BKnZPdpRu/v8dv0E8Z.YudO', 'ROLE_CUSTOMER', 1);
INSERT INTO users (username, password, role) VALUES ('admin', '$2b$12$X0QtAths1V51Q/SZsKcsfu0xGojvbrfcIjt.WL5O0pz7Psq4IoSI.', 'ROLE_ADMIN');
INSERT INTO users (username, password, role, customer_id) VALUES ('ozgesahin', '$2b$12$xGkWZaJmxcPngHBhqaPkS.O5x3PM28Cn7xbkCAYwN0zGPDHWYD.RG', 'ROLE_CUSTOMER', 2);
