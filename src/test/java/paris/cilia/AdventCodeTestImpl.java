package paris.cilia;

import org.junit.jupiter.api.Test;

import java.util.List;

public abstract class AdventCodeTestImpl implements AdventCodeTest {

    protected static final int YEAR = 2021;

    protected List<String> inputs;

    protected abstract Object processFirstStar() throws Exception;

    protected abstract Object processSecondStar() throws Exception;

    private void displayResult(String prefix, Object result) {
        String text = String.join(" -> ", prefix, String.valueOf(result));
        System.out.println(text);
    }

    @Test
    public void firstStar() throws Exception {
        displayResult(">> First  *", processFirstStar());
    }

    @Test
    public void secondStar() throws Exception {
        displayResult(">> Second *", processSecondStar());
    }


}


