
package paris.cilia.game.days;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Day19 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 19;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        List<Scanner> scanners = parseInputs(inputs);
        while (scanners.size() != 1) {
            List<Scanner> mustBeMerged = new ArrayList<>(scanners);
            scanners = new ArrayList<>();
            while (mustBeMerged.size() > 1) {
                Scanner reference = mustBeMerged.get(0);
                boolean match = false;
                for (int i = 1; i < mustBeMerged.size(); i++) {
                    // DO Job
                    Scanner tested = mustBeMerged.get(i);
                    for (int rY = 0; rY < 4; rY++) {
                        for (int rZ = 0; rZ < 4; rZ++) {
                            match = testScannerDirection(reference, tested);
                            if (match) {
                                break;
                            }
                            reference.rotate(AXES.Z);
                        }
                        if (match) {
                            break;
                        }
                        reference.rotate(AXES.Y);
                    }
                    if (!match) {
                        for (int rX = 0; rX < 2; rX++) {
                            reference.rotate(AXES.X);
                            for (int rZ = 0; rZ < 4; rZ++) {
                                match = testScannerDirection(reference, tested);
                                if (match) {
                                    break;
                                }
                                reference.rotate(AXES.Z);
                            }
                            if (match) {
                                break;
                            }
                            reference.rotate(AXES.X);
                        }
                    }
                    if (match) {
                        mergeScanners(reference, tested);
//                        reference.resetDirection();
                        scanners.add(reference);
                        mustBeMerged.remove(reference);
                        mustBeMerged.remove(tested);
                        System.out.println(new Date() + "(" + i + ") => " + reference.getBeacons().size() + " -- ");
                        break;
                    } else {
//                        reference.resetDirection();
                        //System.out.println(new Date() + "(" + scanI + ") => " + reference.getBeacons().size());
                    }
                    if (i == mustBeMerged.size() - 1) {
                        scanners.add(mustBeMerged.get(0));
                        mustBeMerged.remove(0);
                    }
                }
            }
            if (mustBeMerged.size() == 1) {
                scanners.addAll(mustBeMerged);
            }
            for (int i = 0; i < scanners.size(); i++) {
                System.out.println(i + " -> " + scanners.get(i).getBeacons().size());
            }
        }

        return scanners.get(0).getBeacons().size();
    }

    protected Integer processSecondStar() {
        return 0;
    }

    private boolean testScannerDirection(Scanner reference, Scanner tested) {
        boolean match;
        for (int y = 0; y < 4; y++) {
            for (int z = 0; z < 4; z++) {
                match = testAllBeaconsPosition(reference, tested);
                if (match) {
                    return true;
                }
                tested.rotate(AXES.Z);
            }
            tested.rotate(AXES.Y);
        }
        for (int x = 0; x < 2; x++) {
            tested.rotate(AXES.X);
            for (int z = 0; z < 4; z++) {
                match = testAllBeaconsPosition(reference, tested);
                if (match) {
                    return true;
                }
                tested.rotate(AXES.Z);
            }
            tested.rotate(AXES.X);
        }
//        tested.resetDirection();
        return false;
    }

    private List<Scanner> parseInputs(List<String> inputs) {
        List<Scanner> scanners = new ArrayList<>();
        Scanner scanner = null;
        for (String line : inputs) {
            if (line.startsWith("---")) {
                if (scanner != null) {
                    scanners.add(scanner);
                }
                scanner = new Scanner();
            } else if (!"".equalsIgnoreCase(line)) {
                Objects.requireNonNull(scanner).addBeacon(line);
            }
        }
        scanners.add(scanner);
        return scanners;
    }

    private boolean testAllBeaconsPosition(Scanner reference, Scanner tested) {
        for (Position refInitBeacon : reference.getBeacons()) {
            for (Position testedInitBeacon : tested.getBeacons()) {
                // Get tested scanner position from init beacons
                tested.calculateRelativePosition(refInitBeacon, testedInitBeacon);
                if (!tested.isOutOfRange()) {
                    long count = reference.getBeacons().stream()
                            .filter(refBeacon -> tested.getBeacons().stream()
                                    .anyMatch(testedBeacon -> tested.getPosition().getX() == (refBeacon.getX() - testedBeacon.getX())
                                            && tested.getPosition().getY() == (refBeacon.getY() - testedBeacon.getY())
                                            && tested.getPosition().getZ() == (refBeacon.getZ() - testedBeacon.getZ()
                                    )))
                            .count();
                    if (count >= 12) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void mergeScanners(Scanner reference, Scanner toMerge) {
        toMerge.getBeacons().forEach(beacon -> {
            Position newBeacon = new Position(
                    beacon.getX() + toMerge.getPosition().getX(),
                    beacon.getY() + toMerge.getPosition().getY(),
                    beacon.getZ() + toMerge.getPosition().getZ()
            );
            reference.getBeacons().add(newBeacon);
        });
    }

    @Getter
    private static class Scanner {

        private static final int RANGE = 1000;

        private final Direction direction = new Direction();
        private final Set<Position> beacons = new HashSet<>();

        private Position position = new Position(0, 0, 0);

        private Set<Position> testedFirstBeacon = new HashSet<>();

        public void calculateRelativePosition(Position referenceBeacon, Position beacon) throws UnsupportedOperationException {
            int x = referenceBeacon.getX() - beacon.getX();
            int y = referenceBeacon.getY() - beacon.getY();
            int z = referenceBeacon.getZ() - beacon.getZ();
            position = new Position(x, y, z);
        }

        public boolean isOutOfRange() {
            return position.getX() > RANGE * 2 || position.getY() > RANGE * 2 || position.getZ() > RANGE * 2;
        }

        public void addBeacon(String line) {
            String[] coords = line.split(",");
            beacons.add(new Position(
                    Integer.parseInt(coords[0]),
                    Integer.parseInt(coords[1]),
                    Integer.parseInt(coords[2])));
        }

        public void rotate(AXES rotation) {
            direction.rotate(rotation);
            beacons.forEach(beacon -> beacon.rotate(rotation));
        }

        public void resetDirection() {
            testedFirstBeacon = new HashSet<>();
            while (direction.getDirX().get() != 0) {
                rotate(AXES.X);
            }
            while (direction.getDirY().get() != 0) {
                rotate(AXES.Y);
            }
            while (direction.getDirZ().get() != 0) {
                rotate(AXES.Z);
            }
        }

    }

    @Getter
    private static class Direction {

        private final AtomicInteger dirX = new AtomicInteger(0);
        private final AtomicInteger dirY = new AtomicInteger(0);
        private final AtomicInteger dirZ = new AtomicInteger(0);

        public void rotate(AXES rotation) {
            switch (rotation) {
                case X -> {
                    dirX.set(dirX.incrementAndGet() % 4);
                }
                case Y -> {
                    dirY.set(dirY.incrementAndGet() % 4);
                }
                case Z -> {
                    dirZ.set(dirZ.incrementAndGet() % 4);
                }
            }
        }

    }

    @EqualsAndHashCode
    @Getter
    @AllArgsConstructor
    private static class Position {

        private int x;
        private int y;
        private int z;

        public void rotate(AXES rotation) {
            switch (rotation) {
                case X -> {
                    int newZ = y * -1;
                    y = z;
                    z = newZ;
                }
                case Y -> {
                    int newX = z * -1;
                    z = x;
                    x = newX;
                }
                case Z -> {
                    int newY = x * -1;
                    x = y;
                    y = newY;
                }
            }
        }

        public String toString() {
            return "[" + x + "," + y + "," + z + "]";
        }

    }

    private enum AXES {
        X, Y, Z;
    }

}
