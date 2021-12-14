
package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day14 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 14;

    private String polymer;
    private Map<String, String> transform;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
        polymer = inputs.get(0);
        transform = new HashMap<>();
        for (int n = 2; n < inputs.size(); n++) {
            transform.put(inputs.get(n).split(" -> ")[0], inputs.get(n).split(" -> ")[1]);
        }
    }

    protected Long processFirstStar() {
        Map<String, Long> count =
                polymer.codePoints().mapToObj(Character::toString)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        // Transform
        for (int i = 0; i < polymer.length() - 1; i++) {
            transform(10, 0, count, polymer.substring(i, i + 1), polymer.substring(i + 1, i + 2));
        }
        // Calc score
        long min = count.values().stream().mapToLong(val -> val).min().getAsLong();
        long max = count.values().stream().mapToLong(val -> val).max().getAsLong();
        return max - min;
    }

    private void transform(int nbSteps, int step, Map<String, Long> count, String first, String last) {
        if (step != nbSteps) {
            String added = transform.get(first.toString() + last.toString());
            if (count.containsKey(added)) {
                count.put(added, count.get(added) + 1);
            } else {
                count.put(added, 1L);
            }
            transform(nbSteps, step + 1, count, first, added);
            transform(nbSteps, step + 1, count, added, last);
        }
    }

    private Map<String, Long> parse(Map<String, Long> pairs, Map<String, Long> count) {
        Map<String, Long> newPairs = new HashMap<>();
        for (Map.Entry<String, Long> pair : pairs.entrySet()) {
            String first = pair.getKey().charAt(0) + transform.get(pair.getKey());
            String second = transform.get(pair.getKey()) + pair.getKey().charAt(1);
            if (count.containsKey(transform.get(pair.getKey()))) {
                count.put(transform.get(pair.getKey()), count.get(transform.get(pair.getKey())) + pair.getValue());
            } else {
                count.put(transform.get(pair.getKey()), pair.getValue());
            }
            if (newPairs.containsKey(first)) {
                newPairs.put(first, newPairs.get(first) + pair.getValue());
            } else {
                newPairs.put(first, pair.getValue());
            }
            if (newPairs.containsKey(second)) {
                newPairs.put(second, newPairs.get(second) + pair.getValue());
            } else {
                newPairs.put(second, pair.getValue());
            }
        }
        return newPairs;
    }

    protected Long processSecondStar() {
        Map<String, Long> count =
                polymer.codePoints().mapToObj(Character::toString)
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Generate pairs
        Map<String, Long> pairs = new HashMap<>();
        for (int i = 0; i < polymer.length() - 1; i++) {
            String key = polymer.substring(i, i + 2);
            if (pairs.containsKey(key)) {
                pairs.put(key, pairs.get(key) + 1);
            } else {
                pairs.put(key, 1L);
            }
        }

        int step = 0;
        while (step < 40) {
            pairs = parse(pairs, count);
            step++;
        }

        // Calc score
        long min = count.values().stream().mapToLong(val -> val).min().getAsLong();
        long max = count.values().stream().mapToLong(val -> val).max().getAsLong();
        return max - min;
    }

}
