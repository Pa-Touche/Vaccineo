package lu.vaccineo.business.user;

import lombok.*;
import lu.vaccineo.technical.utils.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * This is simplified representation of password storage.
 * <p>
 * Using Spring-Securities tooling would allow to create a single column that contains:
 * - Salt
 * - Algorithm type: this is very convenient as it allows to upgrade to more robust algorithms.
 * - hashed password (using the specified algorithm)
 */
@Entity(name = "user_password")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordEntity extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * Does not contain actual password but
     */
    @Column(name = "password_hash", nullable = false)
    @NotNull
    private String passwordHash;


    @Column(name = "salt", nullable = false)
    private String salt;
}
