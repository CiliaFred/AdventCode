package paris.cilia.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdventCodeUtils {

    public static List<String> readInputByLine(int day) throws FileNotFoundException {
        ClassLoader classLoader = AdventCodeUtils.class.getClassLoader();
        String path = String.join("/", String.valueOf(day), "input.txt");
        File file = new File(Objects.requireNonNull(classLoader.getResource(path)).getFile());
        Scanner input = new Scanner(file);
        List<String> result = new LinkedList<>();
        while (input.hasNextLine()) {
            String line = input.nextLine();
            if (line != null) {
                result.add(line.trim());
            }
        }
        input.close();
        return result;
    }

    public static <T> Stream<List<T>> sliding(List<T> list, int size) {
        if (size > list.size()) {
            return Stream.empty();
        }
        return IntStream.range(0, list.size() - size + 1)
                .mapToObj(start -> list.subList(start, start + size));
    }

}
