package ru.yandex.practicum.accountservice.dao;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user"})
@Table(
        name = "account",
        uniqueConstraints = @UniqueConstraint(
                name = "account_user_currency",
                columnNames = {"user_id", "currency"}
        )
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccountDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    private BigDecimal balance;
    private String currency;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserDao user;
}
