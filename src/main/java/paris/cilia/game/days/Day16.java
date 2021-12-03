
package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;

public class Day16 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 16;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        return 0;
    }

    protected Integer processSecondStar() {
        return 0;
    }

}
