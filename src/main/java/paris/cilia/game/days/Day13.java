
package paris.cilia.game.days;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 13;
    private static final String FOLD_PREFIX = "fold along ";

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        List<Fold> folds = inputs.stream().filter(line -> line.startsWith(FOLD_PREFIX) && !"".equalsIgnoreCase(line.trim())).map(Fold::new).toList();
        Set<Coord> coords = inputs.stream().filter(line -> !line.startsWith(FOLD_PREFIX) && !"".equalsIgnoreCase(line.trim())).map(Coord::new).collect(Collectors.toSet());

        for (Fold fold : folds) {
            Set<Coord> newCoords = new HashSet<>();
            for (Coord coord : coords) {
                newCoords.add(new Coord(fold, coord));
            }
            coords = new HashSet<>(newCoords);
            break;
        }

        // For debugging
        List<String> result = coords.stream().map(Coord::toString).collect(Collectors.toList());
        Collections.sort(result);

        return result.size();
    }

    protected Integer processSecondStar() {
        List<Fold> folds = inputs.stream().filter(line -> line.startsWith(FOLD_PREFIX) && !"".equalsIgnoreCase(line.trim())).map(Fold::new).toList();
        Set<Coord> coords = inputs.stream().filter(line -> !line.startsWith(FOLD_PREFIX) && !"".equalsIgnoreCase(line.trim())).map(Coord::new).collect(Collectors.toSet());

        for (Fold fold : folds) {
            Set<Coord> newCoords = new HashSet<>();
            for (Coord coord : coords) {
                newCoords.add(new Coord(fold, coord));
            }
            coords = new HashSet<>(newCoords);
        }

        // For debugging
        List<String> result = coords.stream().map(Coord::toString).collect(Collectors.toList());
        Collections.sort(result);

        displayCoords(coords);

        return result.size();
    }

    private void displayCoords(Set<Coord> coords) {
        int maxX = coords.stream().mapToInt(Coord::getX).max().getAsInt();
        int maxY = coords.stream().mapToInt(Coord::getY).max().getAsInt();

        char[][] table = new char[maxX + 1][maxY + 1];
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                table[x][y] = ' ';
            }
        }
        for (Coord coord : coords) {
            table[coord.getX()][coord.getY()] = '#';
        }
        for (int y = 0; y <= maxY; y++) {
            for (int x = 0; x <= maxX; x++) {
                System.out.print(table[x][y]);
            }
            System.out.println("");
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static class Fold {

        private final Axes axe;
        private final int value;

        public Fold(String line) {
            this.value = Integer.parseInt(line.split("=")[1]);
            this.axe = Axes.valueOf(line.split("=")[0].replace(FOLD_PREFIX, "").toUpperCase());
        }

    }

    @EqualsAndHashCode
    @Getter
    @RequiredArgsConstructor
    private static class Coord {

        private final int x;
        private final int y;

        public Coord(String line) {
            this.x = Integer.parseInt(line.split(",")[0]);
            this.y = Integer.parseInt(line.split(",")[1]);
        }

        public Coord(Fold fold, Coord toFold) {
            if (fold.getAxe() == Axes.X) {
                this.y = toFold.getY();
                if (toFold.getX() < fold.getValue()) {
                    this.x = toFold.getX();
                } else {
                    this.x = (fold.getValue() - (toFold.getX() % fold.getValue())) % fold.getValue();
                }
            } else {
                this.x = toFold.getX();
                if (toFold.getY() < fold.getValue()) {
                    this.y = toFold.getY();
                } else {
                    this.y = (fold.getValue() - (toFold.getY() % fold.getValue())) % fold.getValue();
                }
            }
        }

        public String toString() {
            return String.join(",", String.valueOf(x), String.valueOf(y));
        }

    }

    private enum Axes {
        X, Y
    }

}
