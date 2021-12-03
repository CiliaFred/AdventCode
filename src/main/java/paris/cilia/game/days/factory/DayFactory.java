package paris.cilia.game.days.factory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.days.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DayFactory {

    public static AdventCodeGameImpl getGameByDay(int day) {
        return switch (day) {
            case 1 -> new Day1();
            case 2 -> new Day2();
            case 3 -> new Day3();
            case 4 -> new Day4();
            case 5 -> new Day5();
            case 6 -> new Day6();
            case 7 -> new Day7();
            case 8 -> new Day8();
            case 9 -> new Day9();
            case 10 -> new Day10();
            case 11 -> new Day11();
            case 12 -> new Day12();
            case 13 -> new Day13();
            case 14 -> new Day14();
            case 15 -> new Day15();
            case 16 -> new Day16();
            case 17 -> new Day17();
            case 18 -> new Day18();
            case 19 -> new Day19();
            case 20 -> new Day20();
            case 21 -> new Day21();
            case 22 -> new Day22();
            case 23 -> new Day23();
            case 24 -> new Day24();
            case 25 -> new Day25();
            default -> null;
        };
    }

}
