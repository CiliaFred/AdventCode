package paris.cilia.game.days;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.*;

public class Day9 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 9;
    private static final String PREVIOUS = "previous";
    private static final String CURRENT = "current";
    private static final String NEXT = "next";

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        int score = 0;
        for (Point point : getLowPoints()) {
            score += point.getValue() + 1;
        }
        return score;
    }

    protected Integer processSecondStar() {
        // Identify LowPoints
        List<Point> lowPoints = getLowPoints();

        // Get all basins size
        List<Integer> basinSizes = new ArrayList<>();
        for (Point point : lowPoints) {
            Set<Point> basin = new HashSet<>();
            buildBasin(basin, point);
            basinSizes.add(basin.size());
        }

        // Keep the largest three
        Collections.sort(basinSizes);
        List<Integer> podium = basinSizes.subList(basinSizes.size() - 3, basinSizes.size());

        // Calc score
        return podium.stream().mapToInt(Integer::intValue).reduce(1, (a, b) -> a * b);
    }

    private void buildBasin(Set<Point> basin, Point lowPoint) {
        basin.add(lowPoint);
        List<Point> neighbors = lowPoint.getLowNeighbors(inputs);
        for (Point neighbor : neighbors) {
            if (!basin.contains(neighbor)) {
                buildBasin(basin, neighbor);
            }
        }
    }

    private List<Point> getLowPoints() {
        Map<String, List<Integer>> workOnIt = new HashMap<>();
        List<Point> points = new ArrayList<>();
        for (int j = 0; j <= inputs.size(); j++) {
            workOnIt.put(PREVIOUS, workOnIt.get(CURRENT));
            workOnIt.put(CURRENT, workOnIt.get(NEXT));
            if (j < inputs.size()) {
                List<Integer> line = inputs.get(j).chars().map(Character::getNumericValue).boxed().toList();
                workOnIt.put(NEXT, line);
            } else {
                workOnIt.put(NEXT, null);
            }
            if (workOnIt.get(CURRENT) != null) {
                for (int i = 0; i < workOnIt.get(CURRENT).size(); i++) {
                    if (isLowPoint(i, workOnIt)) {
                        Point low = new Point(i, j - 1, workOnIt.get(CURRENT).get(i));
                        points.add(low);
                    }
                }
            }
        }
        return points;
    }

    private boolean isLowPoint(int index, Map<String, List<Integer>> values) {
        return isMinHorizontaly(index, values) && isMinVerticaly(index, values);
    }

    private boolean isMinHorizontaly(int index, Map<String, List<Integer>> values) {
        if (index == 0) {
            return values.get(CURRENT).get(index) < values.get(CURRENT).get(index + 1);
        } else if (index == values.get(CURRENT).size() - 1) {
            return values.get(CURRENT).get(index) < values.get(CURRENT).get(index - 1);
        } else {
            return values.get(CURRENT).get(index) < values.get(CURRENT).get(index + 1) && values.get(CURRENT).get(index) < values.get(CURRENT).get(index - 1);
        }
    }

    private boolean isMinVerticaly(int index, Map<String, List<Integer>> values) {
        if (values.get(PREVIOUS) == null) {
            return values.get(CURRENT).get(index) < values.get(NEXT).get(index);
        } else if (values.get(NEXT) == null) {
            return values.get(CURRENT).get(index) < values.get(PREVIOUS).get(index);
        } else {
            return values.get(CURRENT).get(index) < values.get(PREVIOUS).get(index) && values.get(CURRENT).get(index) < values.get(NEXT).get(index);
        }
    }

    @EqualsAndHashCode
    @Getter
    @RequiredArgsConstructor
    private class Point {

        private final int index;
        private final int line;
        private final int value;

        public List<Point> getLowNeighbors(List<String> inputs) {
            List<Point> result = new ArrayList<>();
            List<Integer> currentLine = inputs.get(getLine()).chars().map(Character::getNumericValue).boxed().toList();
            if (index != 0 && currentLine.get(index - 1) < 9) {
                result.add(new Point(index - 1, getLine(), currentLine.get(index - 1)));
            }
            if (index != currentLine.size() - 1 && currentLine.get(index + 1) < 9) {
                result.add(new Point(index + 1, getLine(), currentLine.get(index + 1)));
            }
            if (line != 0) {
                List<Integer> previousLine = inputs.get(getLine() - 1).chars().map(Character::getNumericValue).boxed().toList();
                if (previousLine.get(index) < 9) {
                    result.add(new Point(index, getLine() - 1, previousLine.get(index)));
                }
            }
            if (line != inputs.size() - 1) {
                List<Integer> nextLine = inputs.get(getLine() + 1).chars().map(Character::getNumericValue).boxed().toList();
                if (nextLine.get(index) < 9) {
                    result.add(new Point(index, getLine() + 1, nextLine.get(index)));
                }
            }
            return result;
        }
    }

}
