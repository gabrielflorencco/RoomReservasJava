package com.roomreservas.domain.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"usuario", "sala"})
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @NotNull(message = "Data/hora de início é obrigatória")
    @Column(nullable = false)
    private OffsetDateTime inicio;

    @NotNull(message = "Data/hora de fim é obrigatória")
    @Column(nullable = false)
    private OffsetDateTime fim;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.PENDENTE;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime dataCriacao;

    @PrePersist
    protected void prePersist() {
        this.dataCriacao = OffsetDateTime.now();
        if (this.status == null) {
            this.status = Status.PENDENTE;
        }
    }

    public enum Status {
        PENDENTE,
        CONFIRMADA,
        CANCELADA,
        CONCLUIDA
    }
}
