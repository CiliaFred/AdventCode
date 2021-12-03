package paris.cilia.days;

import org.junit.jupiter.api.BeforeEach;
import paris.cilia.AdventCodeTest;
import paris.cilia.AdventCodeTestImpl;
import paris.cilia.AdventCodeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

class Day3Test extends AdventCodeTestImpl implements AdventCodeTest {

    private final static int DAY = 3;

    @BeforeEach
    public void init() throws Exception {
        inputs = AdventCodeUtils.readInputByLine(YEAR, DAY);
    }

    protected Integer processFirstStar() throws Exception {
        List<Integer> gammas = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        inputs.forEach(line -> {
            for (int i = 0; i < line.length(); i++) {
                gammas.set(i, gammas.get(i) + Integer.parseInt(String.valueOf(line.charAt(i))));
            }
        });
        AtomicReference<String> gamma = new AtomicReference<>("");
        AtomicReference<String> epsilon = new AtomicReference<>("");
        gammas.forEach(bit -> {
            gamma.set(gamma.get() + (bit <= inputs.size() / 2.0 ? "0" : "1"));
            epsilon.set(epsilon.get() + (bit < inputs.size() / 2.0 ? "1" : "0"));
        });
        Integer gam = Integer.parseInt(gamma.get(), 2);
        Integer eps = Integer.parseInt(epsilon.get(), 2);
        return gam * eps;
    }

    protected Integer processSecondStar() throws Exception {

        AtomicReference<String> patternOxygen = new AtomicReference<>("");
        List<String> datasOxygen = inputs;
        String oxygen = null;
        do {
            int countNbOne = datasOxygen.stream().mapToInt(line ->
                    Integer.parseInt(String.valueOf(line.charAt(patternOxygen.get().length()))) == 1 ? 1 : 0
            ).sum();
            patternOxygen.set(patternOxygen.get() + (countNbOne < datasOxygen.size() / 2.0 ? "0" : "1"));
            datasOxygen = datasOxygen.stream().filter(line -> line.startsWith(patternOxygen.get())).collect(Collectors.toList());
            if (datasOxygen.size() == 1) {
                oxygen = datasOxygen.get(0);
            }
        } while (oxygen == null);

        AtomicReference<String> patternCarbon = new AtomicReference<>("");
        List<String> datasCarbon = inputs;
        String carbon = null;
        do {
            int countNbOne = datasCarbon.stream().mapToInt(line ->
                    Integer.parseInt(String.valueOf(line.charAt(patternCarbon.get().length()))) == 1 ? 1 : 0
            ).sum();
            patternCarbon.set(patternCarbon.get() + (countNbOne < datasCarbon.size() / 2.0 ? "1" : "0"));
            datasCarbon = datasCarbon.stream().filter(line -> line.startsWith(patternCarbon.get())).collect(Collectors.toList());
            if (datasCarbon.size() == 1) {
                carbon = datasCarbon.get(0);
            }
        } while (carbon == null);

        return Integer.parseInt(oxygen, 2) * Integer.parseInt(carbon, 2);
    }

}
