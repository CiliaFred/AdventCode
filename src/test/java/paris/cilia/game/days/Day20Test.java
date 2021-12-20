package paris.cilia.game.days;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.days.factory.DayFactory;

@ExtendWith(MockitoExtension.class)
class Day20Test {

    private static final int DAY = 20;

    @Spy
    AdventCodeGameImpl game = DayFactory.getGameByDay(DAY);

    @Test
    void firstStar() {
        game.firstStar();
        Assertions.assertEquals(35, game.getResult());
    }

    @Test
    void secondStar() {
        game.secondStar();
        Assertions.assertEquals(3351, game.getResult());
    }
}