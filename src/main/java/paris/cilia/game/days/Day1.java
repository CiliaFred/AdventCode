package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;

public class Day1 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 1;

    protected void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Long processFirstStar() {
        return AdventCodeUtils.sliding(inputs, 2)
                .filter(window -> Integer.parseInt(window.get(0)) < Integer.parseInt(window.get(1)))
                .count();
    }

    protected Long processSecondStar() {
        int offset = 1;
        int size = 3;
        return AdventCodeUtils.sliding(inputs, offset + size)
                .map(window -> window.stream().map(Integer::parseInt).toList())
                .filter(window -> {
                    int value1 = window.subList(0, size).stream().reduce(0, Integer::sum);
                    int value2 = window.subList(offset, offset + size).stream().reduce(0, Integer::sum);
                    return value1 < value2;
                })
                .count();
    }

}
