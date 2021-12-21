
package paris.cilia.game.days;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Day21 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 21;
    private static final int SCORE_TO_REACH = 21;
    private static final int DIE_SIZE = 3;


    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Integer processFirstStar() {
//        Player one = new Player(inputs.get(0));
//        Player two = new Player(inputs.get(1));
//        Die die = new Die();
//        int dieLaunch = 0;

//        System.out.println(dieLaunch + " => 0 : Player 1 : " + one.getPosition() + " => " + one.getScore());
//        System.out.println(dieLaunch + " => 0 : Player 2 : " + two.getPosition() + " => " + two.getScore());
//        while (!one.won() && !two.won()) {
//            int oneLaunch = launchDice(one, die);
//            dieLaunch += 3;
//            System.out.println(dieLaunch + " => " + oneLaunch + " : Player 1 : " + one.getPosition() + " => " + one
//            .getScore());
//            if (!one.won()) {
//                int twoLaunch = launchDice(two, die);
//                dieLaunch += 3;
//                System.out.println(dieLaunch + " => " + twoLaunch + " : Player 2 : " + two.getPosition() + " => " +
//                two.getScore());
//            }
//        }
//
//        return dieLaunch * (one.won() ? two.getScore().get() : one.getScore().get());
        return 0;
    }

    protected Long processSecondStar() {
        Player one = new Player();
        Player two = new Player();
        List<Game> games = new ArrayList<>();
        Game game = new Game(one, two);
        game.getPlayerOnePosition().set(Integer.parseInt(inputs.get(0).split(":")[1].trim()));
        game.getPlayerTwoPosition().set(Integer.parseInt(inputs.get(1).split(":")[1].trim()));
        games.add(game);

        while (games.size() > 0) {
            List<Game> newGameList = Collections.synchronizedList(new ArrayList<>());
            games.parallelStream().forEach(g -> {
                List<Game> result = g.turn();
                newGameList.addAll(result);
            });
            games = newGameList;
        }

        return Math.max(one.getVictory().get(), two.getVictory().get());
    }


    private Game playAllPossibleGame(Game root) {
        if (root != null) {
            for (int i = 1; i <= DIE_SIZE; i++) {
                for (int j = 1; j <= DIE_SIZE; j++) {
                    for (int k = 1; k <= DIE_SIZE; k++) {
                        if (playAllPossibleGame(root.get) != null) {
                            return getPairToSplit(root.getLeft());
                        } else {

                        }
                    }
                }
            }
            if (root.mustSplit()) {
                return root;
            }
            if (getPairToSplit(root.getRight()) != null) {
                return getPairToSplit(root.getRight());
            }
        }
        return null;
    }


//    private int launchDice(Player player, Die die) {
//        int launchResult = die.roll() + die.roll() + die.roll();
//        player.setPosition(((player.getPosition() + launchResult - 1) % 10) + 1);
//        player.getScore().addAndGet(player.getPosition());
//        return launchResult;
//    }

    @Getter
    @RequiredArgsConstructor
    private static class Game {

        private final Player playerOne;
        private final AtomicInteger playerOneScore = new AtomicInteger(0);
        private final AtomicInteger playerOnePosition = new AtomicInteger(0);
        private final Player playerTwo;
        private final AtomicInteger playerTwoScore = new AtomicInteger(0);
        private final AtomicInteger playerTwoPosition = new AtomicInteger(0);

        public Game generateGameForPlayerOne(int i, int j, int k, Game root) {
            int launchResult = i + j + k;
            int newPosition = ((root.getPlayerOnePosition().get() + launchResult - 1) % 10) + 1;
            int newScore = root.getPlayerOneScore().addAndGet(newPosition);
            if (newScore >= SCORE_TO_REACH) {
                root.getPlayerOne().getVictory().incrementAndGet();
                return null;
            } else {
                Game newGame = new Game(root.getPlayerOne(), root.getPlayerTwo());
                root.getPlayerOnePosition().set(newPosition);
                root.getPlayerOneScore().set(newScore);
                return newGame;
            }
        }

        public Game generateGameForPlayerTwo(int i, int j, int k, Game root) {
            int launchResult = i + j + k;
            int newPosition = ((root.getPlayerTwoPosition().get() + launchResult - 1) % 10) + 1;
            int newScore = root.getPlayerTwoScore().addAndGet(newPosition);
            if (newScore >= SCORE_TO_REACH) {
                root.getPlayerTwo().getVictory().incrementAndGet();
                return null;
            } else {
                Game newGame = new Game(root.getPlayerOne(), root.getPlayerTwo());
                root.getPlayerTwoPosition().set(newPosition);
                root.getPlayerTwoScore().set(newScore);
                return newGame;
            }
        }

        public List<Game> turn() {
            List<Game> games = new ArrayList<>();
            for (int i = 1; i <= DIE_SIZE; i++) {
                for (int j = 1; j <= DIE_SIZE; j++) {
                    for (int k = 1; k <= DIE_SIZE; k++) {
                        int launchResult = i + j + k;
                        Game newGame = new Game(playerOne, playerTwo);
                        newGame.getPlayerOnePosition().set(((playerOnePosition.get() + launchResult - 1) % 10) + 1);
                        newGame.getPlayerOneScore().addAndGet(newGame.getPlayerOnePosition().get());
                        if (newGame.getPlayerOneScore().get() >= SCORE_TO_REACH) {
                            playerOne.getVictory().incrementAndGet();
                        } else {
                            games.add(newGame);
                            for (int i2 = 1; i2 <= DIE_SIZE; i2++) {
                                for (int j2 = 1; j2 <= DIE_SIZE; j2++) {
                                    for (int k2 = 1; k2 <= DIE_SIZE; k2++) {
                                        int launchResult2 = i2 + j2 + k2;
                                        Game newGame2 = new Game(playerOne, playerTwo);
                                        newGame2.getPlayerTwoPosition().set(((playerTwoPosition.get() + launchResult2 - 1) % 10) + 1);
                                        newGame2.getPlayerTwoScore().addAndGet(newGame2.getPlayerTwoPosition().get());
                                        if (newGame2.getPlayerTwoScore().get() >= SCORE_TO_REACH) {
                                            playerTwo.getVictory().incrementAndGet();
                                        } else {
                                            games.add(newGame2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return games;
        }

    }

    @Getter
    @NoArgsConstructor
    private static class Player {

        private final AtomicLong victory = new AtomicLong(0);

    }

    @Getter
    @NoArgsConstructor
    private static class Die {

        private final AtomicInteger rollCount = new AtomicInteger(0);
        private final AtomicInteger result = new AtomicInteger(0);

        public int roll() {
            if (result.get() >= DIE_SIZE) {
                result.set(0);
            }
            rollCount.incrementAndGet();
            return result.incrementAndGet();
        }

        public Die copy() {
            Die copy = new Die();
            copy.getRollCount().addAndGet(rollCount.get());
            return copy;
        }

    }

}
