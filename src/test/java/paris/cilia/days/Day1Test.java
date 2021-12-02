package paris.cilia.days;

import org.junit.jupiter.api.BeforeEach;
import paris.cilia.AdventCodeTest;
import paris.cilia.AdventCodeTestImpl;
import paris.cilia.AdventCodeUtils;

import java.util.stream.Collectors;

class Day1Test extends AdventCodeTestImpl implements AdventCodeTest {

    private final static int DAY = 1;

    @BeforeEach
    public void init() throws Exception {
        inputs = AdventCodeUtils.readInputByLine(YEAR, DAY);
    }

    protected Long processFirstStar() throws Exception {
        return AdventCodeUtils.sliding(inputs, 2)
                .filter(window -> Integer.parseInt(window.get(0)) < Integer.parseInt(window.get(1)))
                .count();
    }

    protected Long processSecondStar() throws Exception {
        int offset = 1;
        int size = 3;
        return AdventCodeUtils.sliding(inputs, offset + size)
                .map(window -> window.stream().map(Integer::parseInt).collect(Collectors.toList()))
                .filter(window -> {
                    int value1 = window.subList(0, size).stream().reduce(0, Integer::sum);
                    int value2 = window.subList(offset, offset + size).stream().reduce(0, Integer::sum);
                    return value1 < value2;
                })
                .count();
    }

}
