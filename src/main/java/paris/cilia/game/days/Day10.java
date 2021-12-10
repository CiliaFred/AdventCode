
package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Day10 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 10;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        List<Character> errors = new ArrayList<>();
        inputs.forEach(line -> {
            Deque<Character> opened = new ConcurrentLinkedDeque<>();
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '('
                        || line.charAt(i) == '['
                        || line.charAt(i) == '{'
                        || line.charAt(i) == '<') {
                    opened.push(line.charAt(i));
                } else if (line.charAt(i) == ')') {
                    if (opened.peek() == Character.valueOf('(')) {
                        opened.pop();
                    } else {
                        errors.add(line.charAt(i));
                        break;
                    }
                } else if (line.charAt(i) == ']') {
                    if (opened.peek() == Character.valueOf('[')) {
                        opened.pop();
                    } else {
                        errors.add(line.charAt(i));
                        break;
                    }
                } else if (line.charAt(i) == '}') {
                    if (opened.peek() == Character.valueOf('{')) {
                        opened.pop();
                    } else {
                        errors.add(line.charAt(i));
                        break;
                    }
                } else if (line.charAt(i) == '>') {
                    if (opened.peek() == Character.valueOf('<')) {
                        opened.pop();
                    } else {
                        errors.add(line.charAt(i));
                        break;
                    }
                }
            }
        });

        AtomicInteger score = new AtomicInteger(0);
        errors.forEach(ch -> {
            if (ch == ')') {
                score.getAndAdd(3);
            } else if (ch == ']') {
                score.getAndAdd(57);
            } else if (ch == '}') {
                score.getAndAdd(1197);
            } else if (ch == '>') {
                score.getAndAdd(25137);
            }
        });
        return score.get();
    }

    protected Long processSecondStar() {
        List<Long> lineScore = new ArrayList<>();
        for (String line : inputs) {
            Deque<Character> opened = new ConcurrentLinkedDeque<>();
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '('
                        || line.charAt(i) == '['
                        || line.charAt(i) == '{'
                        || line.charAt(i) == '<') {
                    opened.push(line.charAt(i));
                } else if (line.charAt(i) == ')') {
                    if (opened.peek() == Character.valueOf('(')) {
                        opened.pop();
                    } else {
                        break;
                    }
                } else if (line.charAt(i) == ']') {
                    if (opened.peek() == Character.valueOf('[')) {
                        opened.pop();
                    } else {
                        break;
                    }
                } else if (line.charAt(i) == '}') {
                    if (opened.peek() == Character.valueOf('{')) {
                        opened.pop();
                    } else {
                        break;
                    }
                } else if (line.charAt(i) == '>') {
                    if (opened.peek() == Character.valueOf('<')) {
                        opened.pop();
                    } else {
                        break;
                    }
                }
                if (i == line.length() - 1 && !opened.isEmpty()) {
                    StringBuilder missing = new StringBuilder();
                    try {
                        while (true) {
                            if (opened.peek() == Character.valueOf('(')) {
                                missing.append(")");
                            } else if (opened.peek() == Character.valueOf('[')) {
                                missing.append("]");
                            } else if (opened.peek() == Character.valueOf('{')) {
                                missing.append("}");
                            } else if (opened.peek() == Character.valueOf('<')) {
                                missing.append(">");
                            }
                            opened.pop();
                        }
                    } catch (NoSuchElementException e) {
                        long score = 0;
                        for (int j = 0; j < missing.length(); j++) {
                            score *= 5;
                            if (missing.charAt(j) == ')') {
                                score += 1;
                            } else if (missing.charAt(j) == ']') {
                                score += 2;
                            } else if (missing.charAt(j) == '}') {
                                score += 3;
                            } else if (missing.charAt(j) == '>') {
                                score += 4;
                            }
                        }
                        lineScore.add(score);
                    }
                }
            }
        }

        Collections.sort(lineScore);

        return lineScore.get(lineScore.size() / 2);
    }

}
