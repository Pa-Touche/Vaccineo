package lu.vaccineo.business.user;

import lombok.*;
import lu.vaccineo.technical.persistence.LocalDateAttributeConverter;
import lu.vaccineo.technical.utils.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity(name = "user")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "name", "surname"})
public class UserEntity extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "surname")
    @NotNull
    private String surname;

    @Column(name = "email", unique = true, nullable = false)
    @NotNull
    private String email;

    @Column(name = "birth_date", nullable = false)
    @NotNull
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate birthDate;
}
