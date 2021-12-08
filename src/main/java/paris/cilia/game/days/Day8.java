package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class Day8 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 8;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Long processFirstStar() {
        AtomicLong score = new AtomicLong(0);
        inputs.forEach(input -> {
            String digits = input.split(" \\| ")[0];
            String numbers = input.split(" \\| ")[1];
            List<String> uniqueDigit =
                    Arrays.stream(digits.split(" ")).filter(this::digitGotUniqueLength).map(digit -> {
                        char[] sorted = digit.toCharArray();
                        Arrays.sort(sorted);
                        return new String(sorted);
                    }).toList();
            score.addAndGet(Arrays.stream(numbers.split(" ")).map(number -> {
                char[] sorted = number.toCharArray();
                Arrays.sort(sorted);
                return new String(sorted);
            }).filter(uniqueDigit::contains).count());
        });
        return score.get();
    }

    protected Long processSecondStar() {
        AtomicLong score = new AtomicLong(0);
        inputs.forEach(input -> {
            String digits = input.split(" \\| ")[0];
            String numbers = input.split(" \\| ")[1];
            Digit digit = new Digit(digits);

            String value =
                    Arrays.stream(numbers.split(" ")).map(number -> {
                        char[] sortedNumber = number.toCharArray();
                        Arrays.sort(sortedNumber);
                        return new String(sortedNumber);
                    }).map(number -> {
                        return digit.reverseMapping.get(number).toString();
                    }).collect(Collectors.joining());
            score.addAndGet(Long.parseLong(value));
        });
        return score.get();
    }

    private boolean digitGotUniqueLength(String digit) {
        return digit.length() == 2 ||
                digit.length() == 3 ||
                digit.length() == 4 ||
                digit.length() == 7;
    }

    private class Digit {
        Map<Integer, String> mapping = new HashMap<>();
        Map<String, Integer> reverseMapping = new HashMap<>();

        public Digit(String digits) {
            while (mapping.size() != 10) {
                Arrays.stream(digits.split(" ")).forEach(digit -> {
                    char[] sortedDigit = digit.toCharArray();
                    Arrays.sort(sortedDigit);
                    if (digit.length() == 2) {
                        reverseMapping.put(new String(sortedDigit), 1);
                        mapping.put(1, digit);
                    } else if (digit.length() == 3) {
                        reverseMapping.put(new String(sortedDigit), 7);
                        mapping.put(7, digit);
                    } else if (digit.length() == 4) {
                        reverseMapping.put(new String(sortedDigit), 4);
                        mapping.put(4, digit);
                    } else if (digit.length() == 7) {
                        reverseMapping.put(new String(sortedDigit), 8);
                        mapping.put(8, digit);
                    } else if (digit.length() == 6 && mapping.containsKey(4) && mapping.containsKey(7)) {
                        if (contain(mapping.get(4), digit)) {
                            reverseMapping.put(new String(sortedDigit), 9);
                            mapping.put(9, digit);
                        } else {
                            if (contain(mapping.get(7), digit)) {
                                reverseMapping.put(new String(sortedDigit), 0);
                                mapping.put(0, digit);
                            } else if (mapping.containsKey(0) && !contain(mapping.get(0), digit)) {
                                reverseMapping.put(new String(sortedDigit), 6);
                                mapping.put(6, digit);
                            }
                        }
                    } else if (digit.length() == 5) {
                        if (mapping.containsKey(7) && contain(mapping.get(7), digit)) {
                            reverseMapping.put(new String(sortedDigit), 3);
                            mapping.put(3, digit);
                        } else if (mapping.containsKey(6) && contain(digit, mapping.get(6))) {
                            reverseMapping.put(new String(sortedDigit), 5);
                            mapping.put(5, digit);
                        } else if (mapping.containsKey(6)) {
                            reverseMapping.put(new String(sortedDigit), 2);
                            mapping.put(2, digit);
                        }
                    } else if (digit.length() == 5 && mapping.containsKey(7)) {
                        if (contain(mapping.get(7), digit)) {
                            reverseMapping.put(new String(sortedDigit), 3);
                            mapping.put(3, digit);
                        }
                    }
                });
            }
        }

        private boolean contain(String pattern, String value) {
            boolean result = true;
            for (int i = 0; i < pattern.length(); i++) {
                result = result && value.contains(Character.toString(pattern.toCharArray()[i]));
            }
            return result;
        }
    }

}
