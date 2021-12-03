package paris.cilia.game.days;

import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 3;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        List<Integer> gammas = IntStream.of(new int[inputs.get(0).length()]).mapToObj(val -> 0).collect(Collectors.toList());
        inputs.forEach(line -> {
            for (int i = 0; i < line.length(); i++) {
                gammas.set(i, gammas.get(i) + Integer.parseInt(String.valueOf(line.charAt(i))));
            }
        });
        AtomicReference<String> gamma = new AtomicReference<>("");
        AtomicReference<String> epsilon = new AtomicReference<>("");
        gammas.forEach(bit -> {
            gamma.set(gamma.get() + (bit <= inputs.size() / 2. ? "0" : "1"));
            epsilon.set(epsilon.get() + (bit < inputs.size() / 2. ? "1" : "0"));
        });
        return Integer.parseInt(gamma.get(), 2) * Integer.parseInt(epsilon.get(), 2);
    }

    protected Integer processSecondStar() {
        String oxygen = recursiveLoop(inputs, new AtomicReference<>(""), true);
        String carbon = recursiveLoop(inputs, new AtomicReference<>(""), false);
        return Integer.parseInt(oxygen, 2) * Integer.parseInt(carbon, 2);
    }

    private String recursiveLoop(List<String> datas, AtomicReference<String> pattern, boolean oxygen) {
        if (datas.size() == 1) {
            return datas.get(0);
        } else {
            long nbOne = datas.stream().filter(line -> line.charAt(pattern.get().length()) == '1').count();
            if (oxygen) {
                pattern.set(pattern.get() + (nbOne < datas.size() / 2. ? "0" : "1"));
            } else {
                pattern.set(pattern.get() + (nbOne < datas.size() / 2. ? "1" : "0"));
            }
            datas = datas.stream().filter(line -> line.startsWith(pattern.get())).toList();
            return recursiveLoop(datas, pattern, oxygen);
        }
    }

}
