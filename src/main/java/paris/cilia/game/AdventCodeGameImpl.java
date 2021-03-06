package paris.cilia.game;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public abstract class AdventCodeGameImpl implements AdventCodeGame {

    protected static final int YEAR = 2021;

    protected List<String> inputs;

    @Getter
    protected Object result;

    protected abstract Object processFirstStar();

    protected abstract Object processSecondStar();

    protected abstract void init() throws FileNotFoundException;

    private void displayResult(String prefix, Object result) {
        String text = String.join(" -> ", prefix, String.valueOf(result));
        System.out.println(text);
    }

    public void firstStar() {
        System.out.println("Start : " + new Date());
        try {
            init();
            result = processFirstStar();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        displayResult(">> First  *", result);
        System.out.println("End : " + new Date());
    }

    public void secondStar() {
        System.out.println("Start : " + new Date());
        try {
            init();
            result = processSecondStar();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        displayResult(">> Second *", result);
        System.out.println("End : " + new Date());
    }

}


