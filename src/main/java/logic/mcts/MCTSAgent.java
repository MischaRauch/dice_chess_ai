package logic.mcts;

import logic.Move;
import logic.State;
import logic.board.Board0x88;
import logic.enums.Side;
import logic.player.AIPlayer;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import static logic.mcts.Action.ActionType.WIN;
import static logic.mcts.Node.NodeType.*;

public class MCTSAgent extends AIPlayer {

    State currentState;
    GameTree tree;
    final Side player;
    Node root;
    int numIterations;
    public static Random random;
    static final double c = 1.4;
    long timeNeeded;
    boolean debug = false;
    boolean prune = false;
    boolean uctExpected = false;
    boolean sameStateRollout = false;

    public MCTSAgent(Side player, int numIterations) {
        super(player);
        this.player = player;
        this.numIterations = numIterations;
        random = new Random();
    }

    @Override
    public Move chooseMove(State state) {
        //this is where we create the GameTree and start the MCTS process
        long start = System.nanoTime();
        currentState = state;
        root = new Node(new TreeState(new Board0x88(currentState.board.getBoard()), player, 0), currentState.diceRoll);

        //if we can win, take that action immediately
        for (Action a : root.validActions) {
            if (a.type == WIN) {
                return new Move(a.piece, a.origin, a.destination, state.diceRoll, player);
            }
        }

        tree = new GameTree(root, player);
        Action action = expectiMCTS(tree);
        Move move = new Move(action.piece, action.origin, action.destination, state.diceRoll, player);
        long end = System.nanoTime();
        timeNeeded = end - start;

        if (debug) {
            System.out.println("ACTION TAKEN: [" + action + "] ACTION TYPE: " + action.type);
            System.out.println(root.toString());
            System.out.println("TREE SIZE: " + tree.getTreeNodes().size());
            root.state.board.printBoard();
            System.out.println("--------------------------------------------------------------------");
        }

        return move;
    }

    //int i;

    int numMostVisitedChosen = 0;
    int numBestExpectedChosen = 0;
    int numBestLocalChosen = 0;

    public Action expectiMCTS(GameTree tree) {
        for (int i = 0; i < numIterations; i++) {
            Node leaf = select(tree.getRoot());
            if (leaf.state.terminal) {
//                System.out.println(leaf.type);
//                System.out.println("leaf player to move: " + leaf.state.playerToMove);
//                System.out.println("backup: winner: " + leaf.state.winner);
//                System.out.println("backup: player: " + player);
//                System.out.println("reward: " + leaf.state.reward(player));
                backup(leaf, leaf.state.reward(player));
            } else {
                double delta = simulate(leaf.state);
                backup(leaf, delta);
            }
        }

        double expectedValue = 0;
        double maxVisits = 0;
        Node bestExpected = null;
        Node mostVisited = null;
        for (Node n : root.children) {
            if (n.getExpectedValue() > expectedValue) {
                expectedValue = n.getExpectedValue();
                bestExpected = n;
            }
            if (n.N > maxVisits) {
                mostVisited = n;
                maxVisits = n.N;
            }
        }

        //Node best = bestChild(root);
        Action h = chooseAction(root, mostVisited);
        if (h == mostVisited.action) {
            numMostVisitedChosen++;
//            System.out.println("Chose most visited: " + h.type);
            //System.out.println("Chose most visited: " + h);
        } else if (h == bestExpected.action) {
            numBestExpectedChosen++;
//            System.out.println("Chose best expected: " + h.type);
            //System.out.println("Chose best expected: " + h);
        } else {
            numBestLocalChosen++;
//            System.out.println("Chose action type: " + h.type);
            //System.out.println("Chose action: " + h);
        }
        return h;
        //return mostVisited.action;
    }

    public Action chooseAction(Node root, Node mostVisited) {
        Action best = mostVisited.action;
        double value = mostVisited.getExpectedValue() * best.score;
        for (Node n : root.children) {
            Action a = n.action;
            switch (a.type) {
                case WIN:
                    return a;
                case CAPTURE, QUIET, PROMOTE:
                    if (n.getExpectedValue() * a.score > value) {
                        best = a;
                        value = n.getExpectedValue() * a.score;
                    }
            }
        }
        return best;
    }

    public double UCT(Node v, double C) {
        //System.out.println("CALCULATING UCT");
        //System.out.println("    - value: " + v.getExpectedValue());
        //System.out.println("    - exploration: " + (C * Math.sqrt(Math.log(v.parent.N) / v.N)));
        //TODO: added expected value to UCT term
        double value = (v.N < 1) ? 1 : v.Q / v.N; //v.getExpectedValue();
        double uct = value + (C * Math.sqrt(Math.log(v.parent.N) / v.N));

        return uct;
    }

    public Node select(Node node) {
        //System.out.println("SELECTING...");
        if (node.state.terminal) {
            //System.out.println("SELECTED A TERMINAL NODE");
            return node;
        }

        if (!node.fullyExpanded()) {
            if (node.type == TERMINAL) {
                System.out.println("whoops");
            }
            //System.out.println("SELECTED NODE NOT FULLY EXPANDED");
            Node q = expand(node);
            if (q.type == CHANCE) {
                return expand(q);
            } else {
                return q;
            }
        } else {
            return select(bestChild(node));
        }
    }

    //need to make sure we don't expand terminal nodes
    public Node expand(Node node) {
        //System.out.println("EXPANDING...");
        if (node.type == CHANCE) {
            //System.out.println("Expanding Chance...");
            //chance node not fully expanded -> there are more dice rolls available to explore
            Node q = new Node(node, node.state, node.action, DECISION); //must include dice roll
            node.addChild(q);
            tree.addNode(q);
            return q;
        } else if (node.type == DECISION) {
            //decision node
            //System.out.println("Expanding Decision...");
            Action a = node.getNextAction();
            TreeState nextState = node.state.apply(a); //updates player as well
            Node q;
            if (!nextState.terminal) {
                q = new Node(node, nextState, a, CHANCE);
            } else {
                //Terminal state reached by applying this action. No chance nodes should be created
                q = new Node(node, nextState, a, TERMINAL);
                //q.Q = nextState.playerToMove == player ? -1 : 1;

                //if the Decision PLayer who applied Action 'a' to get Terminal State q was the winner in q -> Always choose this action
                if (nextState.winner != node.state.playerToMove) {
                    System.out.println("HOW is this even possible?");
                }
                q.Q = (nextState.winner == node.state.playerToMove) ? Integer.MAX_VALUE : Integer.MIN_VALUE; //TODO: is it even possible for the winner not to be the player who moved?
                q.pruneSiblings();
                //q.Q = Integer.MAX_VALUE;
                //TODO: prune siblings from game tree and parent node
                //TODO: prune action except a from parent -> mark parent fully expanded
            }
            node.addChild(q);
            tree.addNode(q);
            return q;
        } else {
            //we are trying to expand a terminal node. not possible
            System.out.println("ATTEMPTED TO EXPAND TERMINAL NODE");
            return node;
        }
    }

    public Node bestChild(Node node) {
        //System.out.println("Getting best child...");
        if (node.type == CHANCE) {
            //System.out.println("STOCHASTIC EVENT");
            return node.children.get(random.nextInt(node.children.size()));
        } else {
            //UCT
            if (node.children.isEmpty()) {
                System.out.println("!!!!!!!!!!!!!!!!!\nDecision Node Empty:\n" + node + "\nParent:\n" + node.parent);
                node.state.board.printBoard();
            }
            Node best = node.children.get(0);
            //TODO: order children by heuristic score
            //System.out.println("Decision children size: " + node.children.size());
            double uct = UCT(best, c);
            for (Node n : node.children) {
                double val = UCT(n, c);
                if (val > uct) { //TODO: break ties randomly
                    best = n;
                    uct = val;
                }
            }
            //System.out.println("Best UCT: " + uct);
            return best;
        }
    }

    public double simulate(TreeState state, boolean strategy) {
        TreeState given = new TreeState(new Board0x88(state.board.getBoard()), state.playerToMove, state.depth);

        for (int j = 0; j < 100; j++) {
            if (given.terminal) {
                return given.reward(player);
            }

            if (given.getRolls().isEmpty()) {
                System.out.println("NO ROLLS");
                System.out.println("TERMINAL? " + given.terminal);
                return 0;
            }

            List<Integer> rolls = given.getRolls();
            int roll = rolls.get(random.nextInt(rolls.size()));

            PriorityQueue<Action> actions = given.getAvailableActions(roll); //TODO: reduce action space;
            assert !actions.isEmpty();
            Action a = actions.poll(); //TODO: add randomness

            given = given.simulate(a);
        }
        //no terminal state found in 200 moves
        return 0.5;
    }

    public double simulate(TreeState state) {
        //System.out.println("ROLLOUT!");
        for (int j = 0; j < 100; j++) {
            //System.out.println("-- j: " + j);
            //System.out.println(state.terminal);
            //state.board.printBoard();
            //System.out.println("player to move: " + state.playerToMove);
            //System.out.println("canonical player: " + tree.getPlayer());
            if (state.terminal) {
                //System.out.println("DELTA in j = " + j + " turn delta: " + delta);
                return state.reward(player);
            }

            if (state.getRolls().isEmpty()) {
                System.out.println("NO ROLLS");
                System.out.println("TERMINAL? " + state.terminal);
                return 0;
            }

            //TODO: simulate on same TreeState
            //playout policy
            List<Integer> rolls = state.getRolls();
            //System.out.println(rolls);
            int roll = rolls.get(random.nextInt(rolls.size()));
            //System.out.println("rolled: " + roll);
            PriorityQueue<Action> actions = state.getAvailableActions(roll); //TODO: reduce action space;
            //System.out.println(actions);
            assert !actions.isEmpty();
            //System.out.println("--- available: " + actions.size());
            Action a = actions.poll(); //TODO: add randomness
            //System.out.println("--- action: " + a);
            state = state.apply(a);
            //System.out.println("--- iteration: " + j);

            //swap player

        }
        //no terminal state found in 200 moves
        return 0.5;
    }

    public void backup(Node v, double delta) {
        //System.out.println("BACKING UP: " + delta);
        //TODO: while (v != null) -> include root node
        while (v.parent != null) {
            //ignores chance for now

            v.N += 1;
            v.Q += delta;
//            if (v.type == CHANCE) {
//                // delta = delta / ((double)v.validRolls.size());
//            }
            v = v.parent;

            //delta = -delta;
        }
        root.N += 1;
        root.Q += delta;
    }

    @Override
    public String getNameAi() {
        return "MCTS";
    }

    @Override
    public long getTimeNeeded() {
        return timeNeeded;
    }

}
