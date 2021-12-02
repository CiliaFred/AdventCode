package paris.cilia.days;

import org.junit.jupiter.api.BeforeEach;
import paris.cilia.AdventCodeTest;
import paris.cilia.AdventCodeTestImpl;
import paris.cilia.AdventCodeUtils;

import java.util.concurrent.atomic.AtomicInteger;

class Day2Test extends AdventCodeTestImpl implements AdventCodeTest {

    private final static int DAY = 2;

    private static final String FORWARD = "forward";
    private static final String DOWN = "down";
    private static final String SEPARATOR = " ";

    @BeforeEach
    public void init() throws Exception {
        inputs = AdventCodeUtils.readInputByLine(YEAR, DAY);
    }

    protected Integer processFirstStar() throws Exception {
        AtomicInteger position = new AtomicInteger(0);
        AtomicInteger depth = new AtomicInteger(0);
        inputs.forEach(line -> {
                    if (line.startsWith(FORWARD)) {
                        position.getAndAdd(getLineValue(line));
                    } else {
                        depth.getAndAdd(getLineValue(line) * (line.startsWith(DOWN) ? 1 : -1));
                    }
                }
        );
        return position.get() * depth.get();
    }

    protected Integer processSecondStar() throws Exception {
        AtomicInteger depth = new AtomicInteger(0);
        AtomicInteger position = new AtomicInteger(0);
        AtomicInteger aim = new AtomicInteger(0);
        inputs.forEach(line -> {
                    if (line.startsWith(FORWARD)) {
                        position.getAndAdd(getLineValue(line));
                        depth.set(depth.get() + (aim.get() * getLineValue(line)));
                    } else {
                        aim.set(aim.get() + (getLineValue(line) * (line.startsWith(DOWN) ? 1 : -1)));
                    }
                }
        );
        return position.get() * depth.get();
    }

    private Integer getLineValue(String line) {
        return Integer.parseInt(line.substring(line.lastIndexOf(SEPARATOR) + 1));
    }

}
