package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;

public class Day2 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 2;

    private static final String FORWARD = "forward";
    private static final String DOWN = "down";
    private static final String SEPARATOR = " ";

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
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

    protected Integer processSecondStar() {
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
