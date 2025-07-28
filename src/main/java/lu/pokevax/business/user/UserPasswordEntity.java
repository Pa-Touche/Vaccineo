package lu.pokevax.business.user;

import lombok.*;
import lu.pokevax.technical.utils.BaseEntity;

import javax.persistence.*;

@Entity(name = "user")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordEntity extends BaseEntity {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * Does not contain actual password but
     */
    @Column(name = "surname")
    private String passwordHash;


    @Column(name = "salt")
    private String salt;
}
