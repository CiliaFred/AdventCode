
package paris.cilia.game.days;

import lombok.Getter;
import lombok.Setter;
import paris.cilia.game.AdventCodeGame;
import paris.cilia.game.AdventCodeGameImpl;
import paris.cilia.game.AdventCodeUtils;

import java.io.FileNotFoundException;
import java.util.Deque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Day18 extends AdventCodeGameImpl implements AdventCodeGame {

    private static final int DAY = 18;

    public void init() throws FileNotFoundException {
        inputs = AdventCodeUtils.readInputByLine(DAY);
    }

    protected Long processFirstStar() {
        Queue<Pair> pairs =
                inputs.stream().map(this::parsePair).collect(Collectors.toCollection(LinkedBlockingQueue::new));

        Pair sum = pairs.poll();
        while (!pairs.isEmpty()) {
            sum = sumPair(sum, pairs.poll());
            System.err.println("after sum     : " + sum);
            while (getPairToExplode(sum) != null || getPairToSplit(sum) != null) {
                if (getPairToExplode(sum) != null) {
                    explode(getPairToExplode(sum));
                    System.out.println("after explode : " + sum);
                } else if (getPairToSplit(sum) != null) {
                    split(getPairToSplit(sum));
                    System.out.println("after split   : " + sum);
                }
            }
            System.err.println("after reduce  : " + sum);
        }
        System.err.println("after all     : " + sum);
        return getScore(sum);
    }

    protected Long processSecondStar() {
        long maxScore = 0;
        for (int i = 0; i < inputs.size() - 1; i++) {
            for (int j = 1; j < inputs.size(); j++) {
                if (i != j) {
                    Pair sum = sumPair(parsePair(inputs.get(i)), parsePair(inputs.get(j)));
                    while (getPairToExplode(sum) != null || getPairToSplit(sum) != null) {
                        if (getPairToExplode(sum) != null) {
                            explode(getPairToExplode(sum));
                        } else if (getPairToSplit(sum) != null) {
                            split(getPairToSplit(sum));
                        }
                    }
                    if (maxScore < getScore(sum)) {
                        maxScore = getScore(sum);
                    }
                }
            }
        }

        return maxScore;
    }

    private Long getScore(Pair root) {
        if (root == null) {
            return null;
        } else {
            if (root.getLeftValue() != null && root.getRightValue() != null) {
                return root.getLeftValue() * 3L + root.getRightValue() * 2L;
            } else if (root.getLeftValue() != null) {
                return root.getLeftValue() * 3L + getScore(root.getRight()) * 2L;
            } else if (root.getRightValue() != null) {
                return getScore(root.getLeft()) * 3L + root.getRightValue() * 2L;
            } else {
                return getScore(root.getLeft()) * 3L + getScore(root.getRight()) * 2L;
            }
        }
    }

    private void split(Pair root) {
        if (root != null && root.getLeftValue() != null && root.getLeftValue() >= 10) {
            Pair newLeft = new Pair();
            newLeft.setParent(root);
            newLeft.setDepth(root.getDepth() + 1);
            newLeft.setLeftValue(root.getLeftValue() / 2);
            newLeft.setRightValue((root.getLeftValue() + 1) / 2);
            root.setLeftValue(null);
            root.setLeft(newLeft);
        } else if (root != null && root.getRightValue() != null && root.getRightValue() >= 10) {
            Pair newRight = new Pair();
            newRight.setParent(root);
            newRight.setDepth(root.getDepth() + 1);
            newRight.setLeftValue(root.getRightValue() / 2);
            newRight.setRightValue((root.getRightValue() + 1) / 2);
            root.setRightValue(null);
            root.setRight(newRight);
        }
    }

    private Pair getPairToSplit(Pair root) {
        if (root != null) {
            if (getPairToSplit(root.getLeft()) != null) {
                return getPairToSplit(root.getLeft());
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

    private void explode(Pair explode) {
        Integer newLeftValue = null;
        Integer newRightValue = null;
        Pair onLeft = explode.onLeft();
        Pair onRight = explode.onRight();
        if (onLeft != null && onLeft == explode.getParent()) {
            newLeftValue = explode.getParent().getLeftValue() + explode.getLeftValue();
            newRightValue = 0;
        } else if (onLeft != null) {
            if (onLeft.getRightValue() == null) {
                onLeft.setLeftValue(onLeft.getLeftValue() + explode.getLeftValue());
            } else {
                onLeft.setRightValue(onLeft.getRightValue() + explode.getLeftValue());
            }
        } else {
            newLeftValue = 0;
        }
        if (onRight != null && onRight == explode.getParent()) {
            newRightValue = explode.getParent().getRightValue() + explode.getRightValue();
            newLeftValue = 0;
        } else if (onRight != null) {
            if (onRight.getLeftValue() == null) {
                onRight.setRightValue(onRight.getRightValue() + explode.getRightValue());
            } else {
                onRight.setLeftValue(onRight.getLeftValue() + explode.getRightValue());
            }
        } else {
            newRightValue = 0;
        }
        if (newLeftValue == null && newRightValue == null) {
            if (explode.getParent().getLeft() == explode) {
                newLeftValue = 0;
            }
            if (explode.getParent().getRight() == explode) {
                newRightValue = 0;
            }
        }
        if (newLeftValue != null) {
            explode.getParent().setLeft(null);
            explode.getParent().setLeftValue(newLeftValue);
        }
        if (newRightValue != null) {
            explode.getParent().setRight(null);
            explode.getParent().setRightValue(newRightValue);
        }
    }

    private Pair sumPair(Pair left, Pair right) {
        if (right == null) {
            return left;
        }
        Pair result = new Pair();
        result.setLeft(left);
        result.setRight(right);
        result.setDepth(-1);
        left.setParent(result);
        right.setParent(result);
        incrementDepth(result);
        return result;
    }

    private void incrementDepth(Pair root) {
        if (root != null) {
            root.setDepth(root.getDepth() + 1);
            if (root.getLeft() != null) {
                incrementDepth(root.getLeft());
            }
            if (root.getRight() != null) {
                incrementDepth(root.getRight());
            }
        }
    }

    private Pair getPairToExplode(Pair root) {
        if (root == null) {
            return null;
        } else if (root.mustExplode()) {
            return root;
        }
        if (getPairToExplode(root.getLeft()) != null) {
            return getPairToExplode(root.getLeft());
        }
        if (getPairToExplode(root.getRight()) != null) {
            return getPairToExplode(root.getRight());
        }
        return null;
    }

    private Pair parsePair(String toParse) {
        Deque<Pair> pairs = new ConcurrentLinkedDeque<>();
        Pair result = null;
        for (int i = 0; i < toParse.length(); i++) {
            if (toParse.charAt(i) == '[') {
                pairs.add(new Pair());
            } else if (toParse.charAt(i) == ']') {
                Pair pair = pairs.pollLast();
                Pair parent = pairs.peekLast();
                if (parent != null) {
                    Objects.requireNonNull(pair).setDepth(pairs.size());
                    Objects.requireNonNull(pair).setParent(parent);
                    if (parent.getLeft() == null && parent.getLeftValue() == null) {
                        parent.setLeft(pair);
                    } else {
                        parent.setRight(pair);
                    }
                } else {
                    result = pair;
                }
            } else if (toParse.charAt(i) == ',') {
            } else {
                Pair pair = pairs.peekLast();
                if (pair != null) {
                    if (pair.getLeft() == null && pair.getLeftValue() == null) {
                        pair.setLeftValue(Character.getNumericValue(toParse.charAt(i)));
                    } else {
                        pair.setRightValue(Character.getNumericValue(toParse.charAt(i)));
                    }
                }
            }
        }
        return result;
    }

    @Getter
    @Setter
    private class Pair {

        private Pair left;
        private Integer leftValue;

        private Pair right;
        private Integer rightValue;

        private Pair parent;
        private Integer depth = 0;

        public boolean mustExplode() {
            return getDepth() >= 4;
        }

        public boolean mustSplit() {
            return (getLeftValue() != null && getLeftValue() >= 10) || (getRightValue() != null && getRightValue() >= 10);
        }

        public Pair onLeft() {
            Pair firstCommonParent = parent;
            Pair leftChild = this;
            while (firstCommonParent.getLeft() == leftChild) {
                leftChild = firstCommonParent;
                firstCommonParent = firstCommonParent.getParent();
                if (firstCommonParent == null) {
                    return null;
                }
            }
            if (firstCommonParent.getLeft() != null) {
                if (firstCommonParent.getLeft().getRightValue() != null) {
                    return firstCommonParent.getLeft();
                }
                Pair goRight = firstCommonParent.getLeft();
                while (goRight.getRight() != null) {
                    goRight = goRight.getRight();
                }
                return goRight;
            }
            if (firstCommonParent.getLeftValue() != null) {
                return firstCommonParent;
            }
            return null;
        }

        public Pair onRight() {
            Pair firstCommonParent = parent;
            Pair rightChild = this;
            while (firstCommonParent.getRight() == rightChild) {
                rightChild = firstCommonParent;
                firstCommonParent = firstCommonParent.getParent();
                if (firstCommonParent == null) {
                    return null;
                }
            }
            if (firstCommonParent.getRight() != null) {
                if (firstCommonParent.getRight().getLeftValue() != null) {
                    return firstCommonParent.getRight();
                }
                Pair goLeft = firstCommonParent.getRight();
                while (goLeft.getLeft() != null) {
                    goLeft = goLeft.getLeft();
                }
                return goLeft;
            }
            if (firstCommonParent.getRightValue() != null) {
                return firstCommonParent;
            }
            return null;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("[");
            if (left != null) {
                builder.append(left);
            } else {
                builder.append(leftValue);
            }
            builder.append(",");
            if (right != null) {
                builder.append(right);
            } else {
                builder.append(rightValue);
            }
            builder.append("]");
            return builder.toString();
        }

    }

}
