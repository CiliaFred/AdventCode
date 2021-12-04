package paris.cilia.game.days;

import lombok.EqualsAndHashCode;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day4 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 4;

    private List<Integer> numbers;
    private final List<Grid> grids = new ArrayList<>();

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
        numbers = Arrays.stream(inputs.get(0).split(",")).map(Integer::parseInt).toList();
        for (int i = 2; i < inputs.size(); i = i + 6) {
            grids.add(new Grid(inputs.subList(i, i + 5)));
        }
    }

    protected Integer processFirstStar() {
        for (int n = 5; n < numbers.size(); n++) {
            int finalN = n;
            List<Grid> won = grids.stream().filter(grid -> grid.win(numbers.subList(0, finalN))).toList();
            if (!won.isEmpty()) {
                return won.get(0).getScore(numbers.subList(0, finalN)) * numbers.get(finalN - 1);
            }
        }
        return 0;
    }

    protected Integer processSecondStar() {
        List<Grid> lastTurnWon = new ArrayList<>();
        for (int n = 5; n < numbers.size(); n++) {
            int finalN = n;
            List<Grid> won =
                    grids.stream().filter(grid -> grid.win(numbers.subList(0, finalN))).collect(Collectors.toList());
            if (won.size() == grids.size()) {
                won.removeAll(lastTurnWon);
                return won.get(0).getScore(numbers.subList(0, finalN)) * numbers.get(finalN - 1);
            }
            lastTurnWon = won;
        }
        return 0;
    }

    @EqualsAndHashCode
    private static class Grid {

        int[][] gridTab = new int[5][5];

        public Grid(List<String> inputs) {
            initGrid(inputs);
        }

        private void initGrid(List<String> input) {
            for (int i = 0; i < 5; i++) {
                int[] line = Arrays.stream(input.get(i).split("\\s+")).mapToInt(Integer::parseInt).toArray();
                gridTab[i] = line;
            }
        }

        public Integer getScore(List<Integer> numbers) {
            int score = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (!numbers.contains(gridTab[i][j])) {
                        score += gridTab[i][j];
                    }
                }
            }
            return score;
        }

        public boolean win(List<Integer> numbers) {
            return checkWin(numbers, true) || checkWin(numbers, false);
        }

        private boolean checkWin(List<Integer> numbers, boolean line) {
            for (int i = 0; i < 5; i++) {
                boolean result = true;
                for (int j = 0; j < 5; j++) {
                    int value = line ? gridTab[i][j] : gridTab[j][i];
                    if (!numbers.contains(value)) {
                        result = false;
                        break;
                    }
                }
                if (result) {
                    return true;
                }
            }
            return false;
        }

    }

}
