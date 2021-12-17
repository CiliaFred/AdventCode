
package paris.cilia.game.days;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 17;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        TargetZone target = generateMap(inputs.get(0));
        return (target.getMinY() + 1) * target.getMinY() / 2;
    }

    protected Integer processSecondStar() {
        TargetZone target = generateMap(inputs.get(0));
        int count = 0;
        for (int x = 0; x <= target.getMaxX(); x++) {
            for (int y = target.getMinY(); y <= (target.getMinY() + 1) * target.getMinY() / 2; y++) {
                Probe probe = new Probe();
                probe.setV(new Velocity(new AtomicInteger(x), new AtomicInteger(y)));
                while (!probe.isLost(target) && !probe.reachTarget(target)) {
                    probe.applyStep();
                }
                if (probe.reachTarget(target)) {
                    count++;
                }
            }
        }
        return count;
    }

    private TargetZone generateMap(String line) {
        // target area: x=248..285, y=-85..-56
        Pattern p = Pattern.compile("[^\\d-]*(-?[\\d]+)[.]{2}(-?[\\d]+)[^\\d-]*(-?[\\d]+)[.]{2}(-?[\\d]+)");
        Matcher m = p.matcher(line);
        if (m.matches()) {
            return new TargetZone(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
        }
        return new TargetZone(0, 0, 0, 0);
    }

    @Getter
    @RequiredArgsConstructor
    private static class TargetZone {

        private final int minX;
        private final int maxX;
        private final int minY;
        private final int maxY;

    }

    @Getter
    @NoArgsConstructor
    private static class Probe {

        private final AtomicInteger x = new AtomicInteger(0);
        private final AtomicInteger y = new AtomicInteger(0);

        @Setter
        private Velocity v;

        public boolean isLostOnLeftSide(TargetZone targetZone) {
            return this.x.get() < targetZone.getMinX() && getV().getVX().get() <= 0;
        }

        public boolean isLostOnRightSide(TargetZone targetZone) {
            return this.x.get() > targetZone.getMaxX() && getV().getVX().get() >= 0;
        }

        public boolean isLostInDeep(TargetZone targetZone) {
            return this.y.get() < targetZone.getMinY() && getV().getVY().get() <= 0;
        }

        public boolean isLost(TargetZone targetZone) {
            return isLostOnLeftSide(targetZone)
                    || isLostOnRightSide(targetZone)
                    || isLostInDeep(targetZone);
        }

        public void applyStep() {
            this.x.addAndGet(getV().getVX().get());
            this.y.addAndGet(getV().getVY().get());
            getV().dragEffect();
            getV().gravityEffect();
        }

        public boolean reachTarget(TargetZone targetZone) {
            return this.x.get() >= targetZone.getMinX() && this.x.get() <= targetZone.getMaxX()
                    && this.y.get() >= targetZone.getMinY() && this.y.get() <= targetZone.getMaxY();
        }

    }

    @Getter
    @RequiredArgsConstructor
    private static class Velocity {

        private final AtomicInteger vX;
        private final AtomicInteger vY;

        private void dragEffect() {
            if (getVX().get() < 0) {
                this.vX.incrementAndGet();
            }
            if (getVX().get() > 0) {
                this.vX.decrementAndGet();
            }
        }

        private void gravityEffect() {
            this.vY.decrementAndGet();
        }

    }

}
