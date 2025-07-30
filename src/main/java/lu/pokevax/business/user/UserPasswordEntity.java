package lu.pokevax.business.user;

import lombok.*;
import lu.pokevax.technical.utils.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private UserEntity user;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Long userId;

    /**
     * Does not contain actual password but
     */
    @Column(name = "surname")
    private String passwordHash;


    @Column(name = "salt")
    private String salt;
}
