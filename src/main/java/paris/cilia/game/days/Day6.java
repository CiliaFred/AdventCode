package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 6;

    private Map<Integer, Long> fishMaturity;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
        List<Integer> fishs = Arrays.stream(inputs.get(0).split(",")).map(Integer::parseInt).toList();
        fishMaturity = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            fishMaturity.put(i, 0L);
        }
        fishs.forEach(fish -> fishMaturity.merge(fish, 1L, Long::sum));
    }

    protected Long processFirstStar() {
        organizeFishByDays(80);
        Long nbFish = 0L;
        for (Map.Entry<Integer, Long> entry : fishMaturity.entrySet()) {
            nbFish += entry.getValue();
        }
        return nbFish;
    }

    protected Long processSecondStar() {
        organizeFishByDays(256);
        Long nbFish = 0L;
        for (Map.Entry<Integer, Long> entry : fishMaturity.entrySet()) {
            nbFish += entry.getValue();
        }
        return nbFish;
    }

    private void organizeFishByDays(int nbDays) {
        Map<Integer, Long> clone;
        for (int i = 0; i < nbDays; i++) {
            clone = new HashMap<>();
            for (Map.Entry<Integer, Long> entry : fishMaturity.entrySet()) {
                clone.put(entry.getKey() - 1, entry.getValue());
            }
            if (clone.get(-1) != null) {
                if (clone.get(6) != null) {
                    clone.put(6, clone.get(6) + clone.get(-1));
                } else {
                    clone.put(6, clone.get(-1));
                }
                clone.put(8, clone.get(-1));
            }
            clone.remove(-1);
            fishMaturity = new HashMap<>();
            fishMaturity.putAll(clone);
        }
    }

}
