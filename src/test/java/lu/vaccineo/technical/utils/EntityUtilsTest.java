package lu.vaccineo.technical.utils;

import lu.vaccineo.technical.persistence.EntityContract;
import lu.vaccineo.test.unit.BaseUnitTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityUtilsTest extends BaseUnitTest {

    @Test
    void entityEquals_shouldReturnTrue_whenSameReference() {
        // PREPARE
        DummyEntity entity = new DummyEntity(getRandomInteger());

        // EXECUTE
        boolean result = EntityUtils.entityEquals(entity, entity);

        // CHECK
        assertThat(result).isTrue();
    }

    private static Integer getRandomInteger() {
        int min = 1;
        int max = 1000;
        return min + (int) (Math.random() * (max - min + 1));
    }

    @Test
    void entityEquals_shouldReturnFalse_whenOtherIsNull() {
        // PREPARE
        DummyEntity entity = new DummyEntity(getRandomInteger());

        // EXECUTE
        boolean result = EntityUtils.entityEquals(entity, null);

        // CHECK
        assertThat(result).isFalse();
    }

    @Test
    void entityEquals_shouldReturnFalse_whenClassesDiffer() {
        // PREPARE
        DummyEntity entity1 = new DummyEntity(getRandomInteger());
        DummyEntityOtherType entity2 = new DummyEntityOtherType(getRandomInteger());

        // EXECUTE
        boolean result = EntityUtils.entityEquals(entity1, entity2);

        // CHECK
        assertThat(result).isFalse();
    }

    @Test
    void entityEquals_shouldReturnTrue_whenSameClassAndSameId() {
        // PREPARE
        Integer sharedId = getRandomInteger();
        DummyEntity entity1 = new DummyEntity(sharedId);
        DummyEntity entity2 = new DummyEntity(sharedId);

        // EXECUTE
        boolean result = EntityUtils.entityEquals(entity1, entity2);

        // CHECK
        assertThat(result).isTrue();
    }

    @Test
    void entityEquals_shouldReturnFalse_whenSameClassButDifferentIds() {
        // PREPARE
        DummyEntity entity1 = new DummyEntity(getRandomInteger());
        DummyEntity entity2 = new DummyEntity(getRandomInteger());

        // EXECUTE
        boolean result = EntityUtils.entityEquals(entity1, entity2);

        // CHECK
        assertThat(result).isFalse();
    }


    // Dummy test implementations of EntityContract
    static class DummyEntity implements EntityContract {
        private final Integer id;

        DummyEntity(Integer id) {
            this.id = id;
        }

        @Override
        public Integer getId() {
            return id;
        }
    }

    static class DummyEntityOtherType implements EntityContract {
        private final Integer id;

        DummyEntityOtherType(Integer id) {
            this.id = id;
        }

        @Override
        public Integer getId() {
            return id;
        }
    }
}
