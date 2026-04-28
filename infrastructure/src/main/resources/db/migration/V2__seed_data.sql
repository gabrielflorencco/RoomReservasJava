-- V2__seed_data.sql
-- Dados iniciais para desenvolvimento/testes

INSERT INTO usuarios (id, nome, email) VALUES
    ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Ana Silva',    'ana.silva@email.com'),
    ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'Bruno Costa',  'bruno.costa@email.com'),
    ('c3d4e5f6-a7b8-9012-cdef-123456789012', 'Carla Mendes', 'carla.mendes@email.com');

INSERT INTO salas (id, nome, capacidade, ativa, valor_diaria) VALUES
    ('d4e5f6a7-b8c9-0123-defa-234567890123', 'Sala Alpha',   10, TRUE,  500.00),
    ('e5f6a7b8-c9d0-1234-efab-345678901234', 'Sala Beta',    20, TRUE,  800.00),
    ('f6a7b8c9-d0e1-2345-fabc-456789012345', 'Sala Gamma',   50, TRUE, 1500.00),
    ('a7b8c9d0-e1f2-3456-abcd-567890123456', 'Sala Delta',    5, FALSE,  300.00);
