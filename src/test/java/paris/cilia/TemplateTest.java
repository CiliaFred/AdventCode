package paris.cilia;

import org.junit.jupiter.api.BeforeEach;

class TemplateTest extends AdventCodeTestImpl implements AdventCodeTest {

    private final static int DAY = 1;

    @BeforeEach
    public void init() throws Exception {
        inputs = AdventCodeUtils.readInputByLine(YEAR, DAY);
    }

    protected Object processFirstStar() throws Exception {
        return null;
    }

    protected Object processSecondStar() throws Exception {
        return null;
    }

}
