package paris.cilia;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.days.factory.DayFactory;

public class Launch {

    private static final int DAY = 15;

    public static void main(String[] args) {
        AdventCodeGame game = DayFactory.getGameByDay(DAY);
        game.firstStar();
        game.secondStar();
    }

}
