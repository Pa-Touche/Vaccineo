package lu.vaccineo.test.unit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j
public abstract class BaseUnitTest {

    protected static Integer getRandomInteger() {
        int min = 1;
        int max = 1000;
        return min + (int) (Math.random() * (max - min + 1));
    }
}
