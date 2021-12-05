package paris.cilia.game.days;

import lombok.Getter;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;

public class Day5 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 5;
    private static final int BOARD_SIZE = 1000;

    private Board board;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
        board = new Board();
    }

    protected Integer processFirstStar() {
        for (String input : inputs) {
            board.addLine(new Line(input), false);
        }
        return board.getScore();
    }

    protected Integer processSecondStar() {
        for (String input : inputs) {
            board.addLine(new Line(input), true);
        }
        return board.getScore();
    }

    private class Board {

        private final int[][] grid = new int[BOARD_SIZE][BOARD_SIZE];

        public void addLine(Line vent, boolean manageDiag) {
            if (vent.getX1() != vent.getX2() && vent.getY1() == vent.getY2()) {
                for (int j = Math.min(vent.getX1(), vent.getX2()); j <= Math.max(vent.getX1(), vent.getX2()); j++) {
                    grid[vent.getY1()][j]++;
                }
            } else if (vent.getX1() == vent.getX2() && vent.getY1() != vent.getY2()) {
                for (int i = Math.min(vent.getY1(), vent.getY2()); i <= Math.max(vent.getY1(), vent.getY2()); i++) {
                    grid[i][vent.getX1()]++;
                }
            } else if (manageDiag) {
                int count = 0;
                for (int j = Math.min(vent.getX1(), vent.getX2()); j <= Math.max(vent.getX1(), vent.getX2()); j++) {
                    int i = isReverseDiag(vent) ? Math.max(vent.getY1(), vent.getY2()) - count :
                            Math.min(vent.getY1(), vent.getY2()) + count;
                    grid[i][j]++;
                    count++;
                }
            }
        }

        private boolean isReverseDiag(Line vent) {
            return (vent.getX1() < vent.getX2() && vent.getY1() > vent.getY2()) || (vent.getX1() > vent.getX2() && vent.getY1() < vent.getY2());
        }


        public Integer getScore() {
            Integer score = 0;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (grid[i][j] > 1) {
                        score++;
                    }
                }
            }
            return score;
        }

    }

    @Getter
    private static class Line {

        private final int x1;
        private final int x2;
        private final int y1;
        private final int y2;

        public Line(String order) {
            String[] coord = order.split(" -> ");
            x1 = Integer.parseInt(coord[0].split(",")[0]);
            y1 = Integer.parseInt(coord[0].split(",")[1]);
            x2 = Integer.parseInt(coord[1].split(",")[0]);
            y2 = Integer.parseInt(coord[1].split(",")[1]);
        }
    }

}
