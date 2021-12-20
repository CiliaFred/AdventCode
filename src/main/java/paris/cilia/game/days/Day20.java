
package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;

public class Day20 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 20;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        String algortihm = inputs.get(0);

        char[][] input = new char[inputs.get(2).length()][inputs.size() - 2];
        for (int y = 0; y < inputs.size() - 2; y++) {
            for (int x = 0; x < inputs.get(2).length(); x++) {
                input[x][y] = inputs.get(y + 2).charAt(x);
            }
        }
        input = addBorder(input, '.');
        int nbStep = 2;
        int nbBorder = nbStep + 1;
        for (int i = 0; i < nbStep; i++) {
            input = addBorder(input, '.');
        }
        display(input, true, nbBorder);
        for (int i = 1; i <= nbStep; i++) {
            char[][] output = clone(input, false, nbBorder);
            for (int x = 0; x < input.length; x++) {
                for (int y = 0; y < input[0].length; y++) {
                    output[x][y] = algortihm.charAt(getIndexFromInput(x, y, input));
                }
            }
            input = clone(output, false, nbBorder);
            display(input, true, nbBorder);
        }

        display(input, false, nbStep);
        int count = 0;
        for (int x = (nbStep * 3); x < input.length - (nbStep * 3); x++) {
            for (int y = (nbStep * 3); y < input[0].length - (nbStep * 3); y++) {
                if (input[x][y] == '#') {
                    count++;
                }
            }
        }

        return count;
    }

    protected Integer processSecondStar() {
        String algortihm = inputs.get(0);

        char[][] input = new char[inputs.get(2).length()][inputs.size() - 2];
        for (int y = 0; y < inputs.size() - 2; y++) {
            for (int x = 0; x < inputs.get(2).length(); x++) {
                input[x][y] = inputs.get(y + 2).charAt(x);
            }
        }
        input = addBorder(input, '.');
        int nbStep = 50;
        int nbBorder = nbStep + 1;
        for (int i = 0; i < nbStep; i++) {
            input = addBorder(input, '.');
        }
        display(input, true, nbBorder);
        for (int i = 1; i <= nbStep; i++) {
            char[][] output = clone(input, false, nbBorder);
            for (int x = 0; x < input.length; x++) {
                for (int y = 0; y < input[0].length; y++) {
                    output[x][y] = algortihm.charAt(getIndexFromInput(x, y, input));
                }
            }
            input = clone(output, false, nbBorder);
        }

        display(input, false, 30);
        int count = 0;
        for (int x = (30 * 3); x < input.length - (30 * 3); x++) {
            for (int y = (30 * 3); y < input[0].length - (30 * 3); y++) {
                if (input[x][y] == '#') {
                    count++;
                }
            }
        }

        return count;
    }

    private char[][] clone(char[][] toClone, boolean cutBorder, int nbBorder) {
        int offset = 0;
        if (cutBorder) {
            offset = 3 * nbBorder;
        }
        char[][] clone = new char[toClone.length - (2 * offset)][toClone[0].length - (2 * offset)];
        for (int i = offset; i < toClone.length - offset; i++) {
            if (toClone[i].length - offset - offset >= 0)
                System.arraycopy(toClone[i], offset, clone[i - offset], 0, toClone[i].length - (2 * offset));
        }
        return clone;
    }

    private char[][] addBorder(char[][] toUpdate, char borderChar) {
        char[][] updated = new char[toUpdate.length + 6][toUpdate[0].length + 6];
        for (int i = 0; i < toUpdate.length + 6; i++) {
            for (int j = 0; j < toUpdate[0].length + 6; j++) {
                if (j < 3 || j > toUpdate[0].length - 3 || i < 3 || i > toUpdate.length - 3) {
                    updated[i][j] = borderChar;
                }
            }
        }
        for (int x = 0; x < toUpdate.length; x++) {
            for (int y = 0; y < toUpdate[0].length; y++) {
                updated[x + 3][y + 3] = toUpdate[x][y];
            }
        }
        return updated;
    }

    private void display(char[][] input, boolean border, int nbBorder) {
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        int offset = nbBorder * 3;
        if (border) {
            offset = 0;
        }
        for (int y = offset; y < input[0].length - offset; y++) {
            for (int x = offset; x < input.length - offset; x++) {
                System.out.print(input[x][y]);
            }
            System.out.println("");
        }
    }

    private int getIndexFromInput(int x, int y, char[][] input) {
        StringBuilder binCode = new StringBuilder();
        for (int bY = y - 1; bY <= y + 1; bY++) {
            for (int bX = x - 1; bX <= x + 1; bX++) {
                if (bX < 0 || bX >= input.length) {
                    binCode.append("0");
                } else {
                    if (bY < 0 || bY >= input[0].length) {
                        binCode.append("0");
                    } else {
                        binCode.append(input[bX][bY] == '#' ? "1" : "0");
                    }
                }
            }
        }
        return Integer.parseInt(binCode.toString(), 2);
    }
}