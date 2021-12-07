package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class Day7 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 7;

    private List<Integer> position;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
        position = Arrays.stream(inputs.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    protected Integer processFirstStar() {
        int medianePosition = (position.size() + 1) / 2;
        Collections.sort(position);
        int target = position.get(medianePosition - 1);

        int score = 0;
        for (Integer value : position) {
            score += (Math.max(target, value) - Math.min(target, value));
        }
        return score;
    }

    protected Integer processSecondStar() {
        Collections.sort(position);
        Integer median = position.get(((position.size() + 1) / 2) - 1);
        OptionalDouble average = position.stream().mapToInt(Integer::intValue).average();
        int score = 0;
        if (average.isPresent()) {
            long target = median < average.getAsDouble() ? Math.round(average.getAsDouble() - 1) : Math.round(average.getAsDouble());
            for (Integer value : position) {
                long dist = (Math.max(target, value) - Math.min(target, value));
                score += (dist * (dist + 1)) / 2;
            }
        }
        return score;
    }

}
