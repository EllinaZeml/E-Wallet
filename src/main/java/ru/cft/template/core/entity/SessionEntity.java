package ru.cft.template.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.cft.template.core.service.status.SessionStatus;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="sessions")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.INACTIVE;

    @Column(nullable = false)
    private LocalDateTime sessionTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
