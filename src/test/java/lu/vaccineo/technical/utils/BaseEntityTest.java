package lu.vaccineo.technical.utils;

import lu.vaccineo.test.unit.BaseUnitTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseEntityTest extends BaseUnitTest {

    @Test
    void equals_shouldReturnTrue_forSameInstance() {
        // PREPARE
        TestEntity entity = new TestEntity(getRandomInteger());

        // EXECUTE
        boolean result = entity.equals(entity);

        // CHECK
        assertThat(result).isTrue();
    }

    @Test
    void equals_shouldReturnFalse_forNull() {
        // PREPARE
        TestEntity entity = new TestEntity(getRandomInteger());

        // EXECUTE
        boolean result = entity.equals(null);

        // CHECK
        assertThat(result).isFalse();
    }

    @Test
    void equals_shouldReturnFalse_forDifferentClass() {
        // PREPARE
        TestEntity entity1 = new TestEntity(getRandomInteger());
        DifferentEntity entity2 = new DifferentEntity(getRandomInteger());

        // EXECUTE
        boolean result = entity1.equals(entity2);

        // CHECK
        assertThat(result).isFalse();
    }

    @Test
    void equals_shouldReturnTrue_forSameClassAndSameId() {
        // PREPARE
        Integer id = getRandomInteger();
        TestEntity entity1 = new TestEntity(id);
        TestEntity entity2 = new TestEntity(id);

        // EXECUTE
        boolean result = entity1.equals(entity2);

        // CHECK
        assertThat(result).isTrue();
    }

    @Test
    void equals_shouldReturnFalse_forSameClassAndDifferentId() {
        // PREPARE
        TestEntity entity1 = new TestEntity(getRandomInteger());
        TestEntity entity2 = new TestEntity(getRandomInteger());

        // EXECUTE
        boolean result = entity1.equals(entity2);

        // CHECK
        assertThat(result).isFalse();
    }

    @Test
    void hashCode_shouldReturnClassHashCode() {
        // PREPARE
        TestEntity entity = new TestEntity(getRandomInteger());

        // EXECUTE
        int hashCode = entity.hashCode();

        // CHECK
        assertThat(hashCode).isEqualTo(TestEntity.class.hashCode());
    }

    // === Test Classes ===

    static class TestEntity extends BaseEntity {
        private final Integer id;

        TestEntity(Integer id) {
            this.id = id;
        }

        @Override
        public Integer getId() {
            return id;
        }
    }

    static class DifferentEntity extends BaseEntity {
        private final Integer id;

        DifferentEntity(Integer id) {
            this.id = id;
        }

        @Override
        public Integer getId() {
            return id;
        }
    }
}
