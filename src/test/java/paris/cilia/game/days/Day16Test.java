package paris.cilia.game.days;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.days.factory.DayFactory;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
class Day16Test {

    private static final int DAY = 16;

    @Spy
    AdventCodeGameImpl game = DayFactory.getGameByDay(DAY);

    @Test
    void firstStar() {
        game.firstStar();
        Assertions.assertEquals(12, game.getResult());
    }

    @Test
    void secondStar() {
        game.secondStar();
        Assertions.assertEquals(54, game.getResult());
    }
}