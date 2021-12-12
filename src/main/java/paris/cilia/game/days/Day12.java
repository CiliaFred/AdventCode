
package paris.cilia.game.days;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day12 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 12;
    private static final String START = "start";
    private static final String END = "end";

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
        Map<String, Cave> allCaves = generateCaveList(inputs);

        List<List<Cave>> allPaths = new ArrayList<>();
        Cave start = allCaves.get(START);
        for (Cave target : start.getLinksTo()) {
            List<Cave> path = new ArrayList<>();
            path.add(start);
            allPaths.add(path);
            buildPath(allPaths, path, target);
        }
        List<String> result =
                allPaths.stream()
                        .map(caves -> caves.stream().map(Cave::getName).toList())
                        .map(caveNames -> String.join(",", caveNames))
                        .filter(path -> path.endsWith(END))
                        .distinct()
                        .toList();

        return result.size();
    }

    protected Integer processSecondStar() {
        Map<String, Cave> allCaves = generateCaveList(inputs);

        List<List<Cave>> allPaths = new ArrayList<>();
        Cave start = allCaves.get(START);
        for (Cave target : start.getLinksTo()) {
            List<Cave> path = new ArrayList<>();
            path.add(start);
            buildPath(allPaths, path, target);
            allPaths.add(path);
        }
        List<String> result =
                allPaths.stream()
                        .map(caves -> caves.stream().map(Cave::getName).toList())
                        .map(caveNames -> String.join(",", caveNames))
                        .filter(path -> path.endsWith(END))
                        .distinct()
                        .toList();

        return result.size();
    }

    private void buildPath(List<List<Cave>> allPaths, List<Cave> path, Cave next) {
        if (next.canBeVisit(path)) {
            path.add(next);
            if (!END.equalsIgnoreCase(next.getName())) {
                for (int i = 0; i < next.getLinksTo().size(); i++) {
                    if (i < next.getLinksTo().size()) {
                        List<Cave> newPath = new ArrayList<>(path);
                        buildPath(allPaths, newPath, next.getLinksTo().get(i));
                        allPaths.add(newPath);
                    } else {
                        buildPath(allPaths, path, next.getLinksTo().get(i));
                    }
                }
            }
        }
    }

    private Map<String, Cave> generateCaveList(List<String> inputs) {
        Map<String, Cave> allCaves = new HashMap<>();
        inputs.forEach(input -> {
            buildCave(input, allCaves);
        });
        return allCaves;
    }

    private void buildCave(String line, Map<String, Cave> allCaves) {
        String first = line.split("-")[0];
        String second = line.split("-")[1];
        Cave firstCave = allCaves.containsKey(first) ? allCaves.get(first) : new Cave(first);
        Cave secondCave = allCaves.containsKey(second) ? allCaves.get(second) : new Cave(second);
        if (!START.equalsIgnoreCase(secondCave.getName()) && !firstCave.getLinksTo().contains(secondCave)) {
            firstCave.getLinksTo().add(secondCave);
        }
        if (!START.equalsIgnoreCase(firstCave.getName()) && !secondCave.getLinksTo().contains(firstCave)) {
            secondCave.getLinksTo().add(firstCave);
        }
        allCaves.put(first, firstCave);
        allCaves.put(second, secondCave);
    }

    @EqualsAndHashCode(exclude = {"linksTo"})
    @Getter
    @RequiredArgsConstructor
    private static class Cave {

        private final String name;
        private final Size size;

        private final List<Cave> linksTo = new ArrayList<>();

        public Cave(String name) {
            this.name = name;
            if (END.equalsIgnoreCase(name)) {
                this.size = Size.SPECIAL;
            } else {
                if (Character.isLowerCase(name.charAt(0))) {
                    this.size = Size.SMALL;
                } else {
                    this.size = Size.BIG;
                }
            }
        }

        public boolean canBeVisit(List<Cave> path) {
            return size == Size.BIG
                    || (size == Size.SMALL && canVisitSmallCave(path))
                    || (size == Size.SPECIAL && path.stream()
                    .noneMatch(cave -> END.equalsIgnoreCase(cave.getName())));
        }

        private boolean canVisitSmallCave(List<Cave> path) {
            List<Cave> filtered = path.stream().filter(cave -> cave.getSize() == Size.SMALL).toList();
            List<String> deduplicated = filtered.stream().map(Cave::getName).distinct().toList();
            return filtered.size() == deduplicated.size()
                    || (filtered.size() - 1 == deduplicated.size() && !deduplicated.contains(name));
        }

        public String toString() {
            return this.name;
        }

    }

    private enum Size {
        SMALL, BIG, SPECIAL;
    }

}
