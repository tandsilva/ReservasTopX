package com.reservastopx.model;



import com.reservastopx.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valor;

    @Column(nullable = false)
    private String metodoPagamento = "PIX"; // fixo como PIX

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private LocalDateTime dataPagamento;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurante;
}
