package com.kingmartinien.iutnotifyapi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ACTIVATIONS")
@EntityListeners(AuditingEntityListener.class)
public class Activation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activation_id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "activated")
    private Boolean activated;

    @OneToOne
    @JoinColumn(name = "user_id_fk", nullable = false)
    private User user;

}
