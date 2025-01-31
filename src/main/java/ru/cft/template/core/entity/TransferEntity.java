package ru.cft.template.core.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import ru.cft.template.core.service.status.TransferStatus;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transfers")
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private LocalDateTime transferDateTime;

    @Column(nullable = false)
    private int amount; //сумма перевода

    @Column(nullable = false)
    private String type; // Тип перевода

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private UserEntity receiver;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    public Long getSenderId() {
        return sender != null ? sender.getId() : null; // Возвращаем ID отправителя
    }

    public Long getReceiverId() {
        return receiver != null ? receiver.getId() : null; // Возвращаем ID получателя
    }
}
