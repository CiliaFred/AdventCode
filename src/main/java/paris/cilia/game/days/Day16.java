
package paris.cilia.game.days;

import lombok.Getter;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day16 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 16;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Long processFirstStar() {
        String binary = hexToBin(inputs.get(0));
        AtomicInteger cursor = new AtomicInteger(0);
        long score = 0;
        do {
            long version = extractVersion(cursor, binary);
            long type = extractType(cursor, binary);
            if (type != 4) {
                if (isCustomLength(cursor, binary)) {
                    cursor.addAndGet(15);
                } else {
                    cursor.addAndGet(11);
                }
            } else {
                String data = "";
                do {
                    data = binary.substring(cursor.get(), cursor.addAndGet(5));
                } while (data.startsWith("1"));
            }
            score += version;
        } while (cursor.get() < binary.length() && !endWithZero(cursor, binary));
        return score;
    }

    protected Long processSecondStar() {
        String binary = hexToBin(inputs.get(0));
        AtomicInteger cursor = new AtomicInteger(0);

        Packet packet = new Packet(cursor, binary);

        return packet.getValue();
    }

    private boolean endWithZero(AtomicInteger index, String binary) {
        return !binary.substring(index.get()).contains("1");
    }

    private boolean isCustomLength(AtomicInteger index, String binary) {
        return "0".equals(binary.substring(index.get(), index.incrementAndGet()));
    }

    private long extractVersion(AtomicInteger index, String binary) {
        return Long.parseLong(binary.substring(index.get(), index.addAndGet(3)), 2);
    }

    private long extractType(AtomicInteger index, String binary) {
        return Long.parseLong(binary.substring(index.get(), index.addAndGet(3)), 2);
    }

    private static String hexToBin(String hexa) {
        int i = 0;
        int init = Character.getNumericValue(hexa.charAt(i));
        String prefix = "";
        while (init == 0) {
            prefix += "0000";
            i++;
            init = Character.getNumericValue(hexa.charAt(i));
        }
        if (init < 8) {
            prefix += "0";
        }
        if (init < 4) {
            prefix += "0";
        }
        if (init < 2) {
            prefix += "0";
        }
        return prefix + new BigInteger(hexa, 16).toString(2);
    }

    @Getter
    private class Packet {

        private final long version;
        private final long type;

        private final List<Packet> subs = new ArrayList<>();
        private final long value;

        public Packet(AtomicInteger cursor, String binary) {
            this.version = extractVersion(cursor, binary);
            this.type = extractType(cursor, binary);
            if (this.type == 4) {
                String bloc = "";
                StringBuilder val = new StringBuilder();
                do {
                    bloc = binary.substring(cursor.get(), cursor.addAndGet(5));
                    val.append(bloc.substring(1));
                } while (bloc.startsWith("1"));
                this.value = Long.parseLong(val.toString(), 2);
            } else {
                if (isCustomLength(cursor, binary)) {
                    long length = Long.parseLong(binary.substring(cursor.get(), cursor.addAndGet(15)), 2);
                    int pos = cursor.get();
                    while (cursor.get() < pos + length) {
                        this.subs.add(new Packet(cursor, binary));
                    }
                } else {
                    long nbSubs = Long.parseLong(binary.substring(cursor.get(), cursor.addAndGet(11)), 2);
                    while (this.subs.size() < nbSubs) {
                        this.subs.add(new Packet(cursor, binary));
                    }
                }
                if (this.type == 0) {
                    this.value = this.subs.stream().mapToLong(Packet::getValue).sum();
                } else if (this.type == 1) {
                    long score = this.subs.get(0).value;
                    for (int i = 1; i < this.subs.size(); i++) {
                        score *= this.subs.get(i).value;
                    }
                    this.value = score;
                } else if (this.type == 2) {
                    this.value = this.subs.stream().mapToLong(Packet::getValue).min().getAsLong();
                } else if (this.type == 3) {
                    this.value = this.subs.stream().mapToLong(Packet::getValue).max().getAsLong();
                } else if (this.type == 5) {
                    this.value = this.subs.get(0).value > this.subs.get(1).value ? 1 : 0;
                } else if (this.type == 6) {
                    this.value = this.subs.get(0).value < this.subs.get(1).value ? 1 : 0;
                } else if (this.type == 7) {
                    this.value = this.subs.get(0).value == this.subs.get(1).value ? 1 : 0;
                } else {
                    this.value = 0;
                }
            }
        }

    }

}
