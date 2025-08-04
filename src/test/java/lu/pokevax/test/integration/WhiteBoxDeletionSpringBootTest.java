package lu.pokevax.test.integration;

import lu.pokevax.business.notification.VaccineNotificationService;
import lu.pokevax.business.vaccine.administered.AdministeredVaccineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Meant to check if all {@link lu.pokevax.business.user.UserEntity} "attached" entities are properly deleted.
 * https://en.wikipedia.org/wiki/White-box_testing.
 */
public class WhiteBoxDeletionSpringBootTest extends BaseSpringBootTest {
    @Autowired
    private AdministeredVaccineService administeredVaccineService;

    @Autowired
    private VaccineNotificationService vaccineNotificationService;

    @Test
    void verify_all_entities_are_deleted() {

    }
}
