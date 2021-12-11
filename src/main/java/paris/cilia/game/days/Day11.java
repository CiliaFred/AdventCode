
package paris.cilia.game.days;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day11 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 11;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        Octopus[][] octopuses = generateMap(inputs);

        int stepNumber = 100;
        int score = 0;
        for (int i = 0; i < stepNumber; i++) {
            increaseValue(octopuses);
            Set<Octopus> flashing = getFlashingOctopuses(octopuses);
            score += flashing.size();
        }

        return score;
    }

    protected Integer processSecondStar() {
        Octopus[][] octopuses = generateMap(inputs);

        int stepNumber = 0;
        boolean allFlash = false;
        while (!allFlash) {
            increaseValue(octopuses);
            Set<Octopus> flashing = getFlashingOctopuses(octopuses);
            allFlash = flashing.size() == (inputs.size() * inputs.get(0).length());
            stepNumber++;
        }

        return stepNumber;
    }

    private Octopus[][] generateMap(List<String> inputs) {
        Octopus[][] map = new Octopus[inputs.get(0).length()][inputs.size()];
        for (int y = 0; y < inputs.size(); y++) {
            for (int x = 0; x < inputs.get(y).length(); x++) {
                Octopus octopus = new Octopus(x, y);
                octopus.setValue(Character.getNumericValue(inputs.get(y).charAt(x)));
                map[x][y] = octopus;
            }
        }
        return map;
    }

    private void increaseValue(Octopus[][] map) {
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                Octopus octopus = map[x][y];
                octopus.setValue(octopus.getValue() + 1);
            }
        }
    }

    private Set<Octopus> getFlashingOctopuses(Octopus[][] map) {
        Set<Octopus> octopuses = new HashSet<>();
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                Octopus octopus = map[x][y];
                if (octopus.isFlashing() && !octopuses.contains(octopus)) {
                    octopuses.add(octopus);
                    propagateFlashing(octopus, map, octopuses);
                }
            }
        }
        return octopuses;
    }

    private void propagateFlashing(Octopus octopus, Octopus[][] map, Set<Octopus> flashing) {
        for (Octopus neighbor : octopus.getNonFlashingNeighbors(map)) {
            if (!neighbor.isFlashing()) {
                neighbor.setValue(neighbor.getValue() + 1);
            }
            if (neighbor.isFlashing() && !flashing.contains(neighbor)) {
                flashing.add(neighbor);
                propagateFlashing(neighbor, map, flashing);
            }
        }
    }

    @EqualsAndHashCode
    @Getter
    @RequiredArgsConstructor
    private static class Octopus {

        private final int x;
        private final int y;
        private int value;

        public Set<Octopus> getNonFlashingNeighbors(Octopus[][] map) {
            Set<Octopus> neighbors = new HashSet<>();
            if (x > 0) {
                neighbors.add(map[x - 1][y]);
                if (y > 0) {
                    neighbors.add(map[x - 1][y - 1]);
                    neighbors.add(map[x][y - 1]);
                }
                if (y < map[0].length - 1) {
                    neighbors.add(map[x - 1][y + 1]);
                    neighbors.add(map[x][y + 1]);
                }
            }
            if (x < map.length - 1) {
                neighbors.add(map[x + 1][y]);
                if (y > 0) {
                    neighbors.add(map[x][y - 1]);
                    neighbors.add(map[x + 1][y - 1]);
                }
                if (y < map[0].length - 1) {
                    neighbors.add(map[x][y + 1]);
                    neighbors.add(map[x + 1][y + 1]);
                }
            }
            return neighbors.stream().filter(octopus -> !octopus.isFlashing()).collect(Collectors.toSet());
        }

        public boolean isFlashing() {
            return value == 0;
        }

        public void setValue(int value) {
            this.value = value % 10;
        }

        public String toString() {
            return String.valueOf(value);
        }

    }

}
