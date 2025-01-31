package ru.cft.template.core.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotBlank(message ="фамилия не может быть пустой")
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+$")
    @Size(max = 50)
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message ="имя не может быть пустым")
    @Pattern(regexp = "^[А-ЯЁ][а-яё]+$")
    @Size(max = 50)
    @Column(nullable = false)
    private String firstName;

    @Pattern(regexp = "^[А-ЯЁ][а-яё]+$")
    @Size(max = 50)
    private String midName;

    @NotEmpty(message = "номер телефона должен быть указан")
    @Pattern(regexp = "^7\\d{10}$")
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @NotEmpty
    @Email(message = "Неверный формат электронной почты")
    @Column(nullable = false,unique = true)
    private String email;

    @NotEmpty(message = "дата рождения не может быть пустой")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Дата рождения должна быть в формате yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate birthDate;

    @Size(min = 8, max = 64, message = "пароль должен быть от 8 до 64 символов")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[A-Za-z\\d!?]{8,64}$",
            message = "пароль должен содержать хотя бы одну заглавную букву, одну строчную букву, одну цифру и один спецсимвол (!?).")
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private WalletEntity wallet;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<TransferEntity> sentTransfers = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<TransferEntity> receiverTransfers = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<SessionEntity> sessions = new ArrayList<>();
}
