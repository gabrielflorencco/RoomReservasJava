-- V1__create_tables.sql
-- RoomReservas - Schema inicial

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Tabela de Usuários
CREATE TABLE usuarios (
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    nome        VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (id),
    CONSTRAINT uq_usuarios_email UNIQUE (email)
);

-- Tabela de Salas
CREATE TABLE salas (
    id           UUID           NOT NULL DEFAULT gen_random_uuid(),
    nome         VARCHAR(100)   NOT NULL,
    capacidade   INTEGER        NOT NULL CHECK (capacidade >= 1),
    ativa        BOOLEAN        NOT NULL DEFAULT TRUE,
    valor_diaria NUMERIC(10, 2) NOT NULL CHECK (valor_diaria > 0),
    CONSTRAINT pk_salas PRIMARY KEY (id)
);

-- Tabela de Reservas
CREATE TABLE reservas (
    id           UUID           NOT NULL DEFAULT gen_random_uuid(),
    usuario_id   UUID           NOT NULL,
    sala_id      UUID           NOT NULL,
    inicio       TIMESTAMPTZ    NOT NULL,
    fim          TIMESTAMPTZ    NOT NULL,
    valor_total  NUMERIC(10, 2) NOT NULL,
    status       VARCHAR(20)    NOT NULL DEFAULT 'PENDENTE',
    data_criacao TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_reservas PRIMARY KEY (id),
    CONSTRAINT fk_reservas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT fk_reservas_sala    FOREIGN KEY (sala_id)    REFERENCES salas (id),
    CONSTRAINT chk_reservas_periodo CHECK (fim > inicio),
    CONSTRAINT chk_reservas_status CHECK (status IN ('PENDENTE', 'CONFIRMADA', 'CANCELADA', 'CONCLUIDA'))
);

-- Índices para performance
CREATE INDEX idx_reservas_usuario_id ON reservas (usuario_id);
CREATE INDEX idx_reservas_sala_id    ON reservas (sala_id);
CREATE INDEX idx_reservas_status     ON reservas (status);
CREATE INDEX idx_reservas_inicio     ON reservas (inicio);
CREATE INDEX idx_reservas_fim        ON reservas (fim);
