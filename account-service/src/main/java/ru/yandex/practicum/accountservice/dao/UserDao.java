package ru.yandex.practicum.accountservice.dao;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.accountservice.dto.UserDto;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString(exclude = { "accountDao"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(uniqueConstraints = { @UniqueConstraint(name = "login", columnNames = "login")} )
public class UserDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private String login;
    private LocalDate birthdate;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountDao> accountDao;

    public UserDao(String name, String password, String login, LocalDate birthdate) {
        this.name = name;
        this.password = password;
        this.login = login;
        this.birthdate = birthdate;
    }

    public UserDao(UserDto userDto) {
        this.name = userDto.getName();
        this.password = userDto.getPassword();
        this.login = userDto.getLogin();
        this.birthdate = userDto.getBirthdate();
    }

    public UserDao() {

    }
}