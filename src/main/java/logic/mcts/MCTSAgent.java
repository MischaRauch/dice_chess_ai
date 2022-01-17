package logic.mcts;

import logic.Move;
import logic.State;
import logic.board.Board0x88;
import logic.enums.Side;
import logic.player.AIPlayer;

import java.util.*;

import static logic.mcts.Action.ActionType.WIN;
import static logic.mcts.MCTSAgent.Strategy.CHANCE_PENALTY;
import static logic.mcts.MCTSAgent.Strategy.NEURAL_NETWORK;
import static logic.mcts.Node.NodeType.*;

public class MCTSAgent extends AIPlayer {

    enum Strategy {DEFAULT, CHANCE_PENALTY, ALTERNATING, ALTERNATING_CHANCE_PENALTY, ALTERNATING_DEFAULT, NEURAL_NETWORK}

    State currentState;
    GameTree tree = new GameTree();
    final Side player;
    Node root;
    int numIterations;
    public static Random random;
    double c;
    public static double C = 0.8;
    long timeNeeded;
    boolean debug = false;
    boolean pruneGameTree = false;
    boolean uctExpected = false;
    public List<Node> actions;
    Node child;
    Strategy strategy;
    boolean logMoves = false;

    public static MCTSAgent create(Side player, int numIterations) {
        return new MCTSAgent(player, numIterations);
    }

    public MCTSAgent useStrategy(Strategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public MCTSAgent logData() {
        logMoves = true;
        return this;
    }

    public MCTSAgent setUCTConstant(double c) {
        this.c = c;
        return this;
    }

    public MCTSAgent(Side player, int numIterations) {
        super(player);
        this.player = player;
        this.numIterations = numIterations;
        random = new Random();
        actions = new LinkedList<>();
        c = C;
        strategy = CHANCE_PENALTY;
    }

    public void log() {
        InputParser.csvWriter(InputParser.parseState(currentState.board, player, currentState.diceRoll, root.Q / root.N));
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

        //tree = new GameTree(root, player);
        Action action = expectiMCTS(tree);
        Move move = new Move(action.piece, action.origin, action.destination, state.diceRoll, player);
        long end = System.nanoTime();
        timeNeeded = end - start;

        if (debug) {
            System.out.println("ACTION TAKEN: [" + action + "] ACTION TYPE: " + action.type);
            System.out.println(root.toString());
            //System.out.println("TREE SIZE: " + tree.getTreeNodes().size());
            root.state.board.printBoard();
            System.out.println("--------------------------------------------------------------------");
        }

        if (logMoves)
            log();

        return move;
    }

    //    int numMostVisitedChosen = 0;
//    int numBestExpectedChosen = 0;
//    int numBestLocalChosen = 0;
    public int maxDepth = 0;
    public Action expectiMCTS(GameTree tree) {
        for (int i = 0; i < numIterations; i++) {
            Node leaf = select2(root);

            if (leaf.state.terminal) {
                backup(leaf, leaf.state.reward(player), leaf.state.winner);
            } else {
                simulate(leaf);
            }
        }

        //double expectedValue = 0;
        double maxVisits = 0;
        //Node bestExpected = null;
        Node mostVisited = root.children.get(0);
        for (Node n : root.children) {
//            if (n.getExpectedValue() > expectedValue) {
//                expectedValue = n.getExpectedValue();
//                bestExpected = n;
//            }
            if (n.N > maxVisits) {
                mostVisited = n;
                maxVisits = n.N;
            }
        }
        //System.out.println(mostVisited);

        //Node best = bestChild(root);

        return chooseAction(root, mostVisited);
    }

    public Action chooseAction(Node root, Node mostVisited) {
        child = mostVisited;
        Action best = mostVisited.action;
        //System.out.println("\na* = "+best);
//        double value = (mostVisited.Q / mostVisited.N) + mostVisited.getExpectedValue() * best.score;
        double value = mostVisited.N + (best.score / mostVisited.Q);// - (Math.log(mostVisited.state.score) / mostVisited.numValidRolls);
        for (Node n : root.children) {
            Action a = n.action;
            switch (a.type) {
                case WIN:
                    return a;
                case CAPTURE, QUIET, PROMOTE:
                    double val = mostVisited.N + (best.score / mostVisited.Q);// - (Math.log(mostVisited.state.score) / mostVisited.numValidRolls);
                    if (val > value) {
                        best = a;
                        value = val;

                        child = n;
                    }
            }
        }
        actions.add(child);
        //System.out.println("Action chose: " + best);
        return best;
    }

    public double UCT(Node v, double c) {
        //double chance = (v.type == CHANCE) ? 1.0 / v.validRolls.size() : 0;
        double chance = v.getExpectedValue();
        //double heuristic = Math.sqrt(Math.abs(v.state.score)) / v.numValidRolls;
        //System.out.println("\r"+heuristic);
        double value = (v.N < 1) ? 1 : v.Q / v.N; //if node has not been visit set its value to be the maximum
        return value + chance + (C * Math.sqrt(Math.log(v.parent.N) / v.N));
    }

    public Node select(Node node) {
        //System.out.println("SELECTING...");
        if (node.state.terminal) {
            //System.out.println("SELECTED A TERMINAL NODE");
            return node;
        }

        if (!node.fullyExpanded()) {
            //System.out.println("SELECTED NODE NOT FULLY EXPANDED");
            Node q = expand(node);
            return q.type == CHANCE ? expand(q) : q;
        } else {
            return select(bestChild(node));
        }
    }

    public Node select2(Node node) {

        while (node.fullyExpanded) {
            if (node.state.terminal) {
                return node;
            }

            node = bestChild(node);
        }

        Node q = expand(node);
        return q.type == CHANCE ? expand(q) : q;
    }

    public Node expand(Node node) {
        if (node.type == CHANCE) {
            //chance node not fully expanded -> there are more dice rolls available to explore
            Node q = new Node(node, node.state, node.action, DECISION); //must include dice roll
            node.addChild(q);
            //tree.addNode(q);
            return q;
        } else if (node.type == DECISION) {
            //decision node
            Action a = node.getNextAction();
            TreeState nextState = node.state.apply(a); //updates player as well
            Node q;
            if (!nextState.terminal) {
                q = new Node(node, nextState, a, CHANCE);
            } else {
                //Terminal state reached by applying this action. No chance nodes should be created
                q = new Node(node, nextState, a, TERMINAL);

                //if the Decision PLayer who applied Action 'a' to get Terminal State q was the winner in q -> Always choose this action
                q.pruneSiblings();

                q.Q = Integer.MAX_VALUE; //is this necessary? should probably just be 1
                //q.Q = 1; //is this necessary? should probably just be 1
            }
            node.addChild(q);
            //tree.addNode(q);
            return q;
        } else {
            //we are trying to expand a terminal node. not possible
            System.out.println("ATTEMPTED TO EXPAND TERMINAL NODE");
            return node;
        }
    }

    //Node.ChildComparator childComparator = new Node.ChildComparator();

    public Node bestChild(Node node) {
        //System.out.println("Getting best child...");
        if (node.type == CHANCE) {
            //System.out.println("STOCHASTIC EVENT");
            return node.children.get(random.nextInt((int) node.numValidRolls));
            //return node.children.get(node.getNextRoll());
        } else {
            //UCT
            if (node.children.isEmpty()) {
                System.out.println("!!!!!!!!!!!!!!!!!\nDecision Node Empty:\n" + node + "\nParent:\n" + node.parent);
                node.state.board.printBoard();
            }
            //node.children.sort(childComparator);
            Node best = node.children.get(0);
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

    public void simulate(Node leaf) {
        TreeState given = new TreeState(new Board0x88(leaf.state.board.getBoard()), leaf.state.playerToMove, leaf.state.depth);

        //TODO: NN
        //would have to set the strategy to NEURAL_NETWORK either hardcoded or using the MCTS.create().useStrategy(NEURAL_NETWORK)) thingy
        //or extend this class and hardcode the strategy as NEURAL_NETWORK
        if (strategy == NEURAL_NETWORK) {
            int[] inputArray = InputParser.getInputBoard(given.board, given.playerToMove, given.diceRoll);
            //TODO: pass input array to NN
            /*
            double winProbability = NeuralNetwork.getEvaluation(inputArray)
            if given.playerToMove == player && winProbability > someThreshold
                backup(leaf, 1, player)
            else
                backup(leaf, 0, given.playerToMove)
             */
            return;
        }


        for (int j = 0; j < 100; j++) {
            if (!given.terminal) {
                ArrayList<Integer> rolls = given.getRolls();
                PriorityQueue<Action> actions = given.getAvailableActions(rolls.get(random.nextInt(rolls.size()))); //TODO: reduce action space;
                //PriorityQueue<Action> actions = given.getAvailableActions(given.getRolls().get(0)); //TODO: reduce action space;
                assert !actions.isEmpty();
                Action a = actions.poll(); //TODO: add randomness
//                int best = given.apply(a).score;
//                for (Action alt : actions) {
//                    if (given.apply(alt).score > best) a = alt;
//                }
                given = given.simulate(a);
            } else break;
        }

        backup(leaf, given.winner.opponent(player) ? 0 : 1, given.winner);
    }

    public void backup(Node v, double delta, Side winner) {
        //Choose backpropagation strategy. Loops are inside each case so we don't
        //have to check the backup strategy every iteration of the loop
        switch (strategy) {
            case ALTERNATING -> {
                while (v != null) {
                    //if we are at a chance node,
                    if (v.type == CHANCE) { // that represents an action its parent took
                        if (v.state.playerToMove != winner)
                            v.Q += delta;
                    } else {
                        if (v.state.playerToMove == winner)
                            v.Q += delta;
                    }

                    v.N += 1;
                    v = v.parent;
                }
            }

            case ALTERNATING_CHANCE_PENALTY -> {
                while (v != null) {
                    if (v.type == CHANCE && v.state.playerToMove != winner)
                        v.Q += delta;

                    v.N++;
                    v = v.parent;
                }
            }

            case CHANCE_PENALTY -> {
                while (v != null) {
//                    if (v.type == CHANCE && v.state.playerToMove != player)
                    if (v.type == CHANCE)
                        v.Q += delta;

                    v.N++;
                    v = v.parent;
                }
            }

            case ALTERNATING_DEFAULT -> {
                while (v != null) {
                    if (v.type == CHANCE) {
                        if (player == winner && v.state.playerToMove != player)
                            v.Q += delta;
//                            v.Q++;
                    } else {
                        if (player == winner && v.state.playerToMove == player) {
                            v.Q += delta;
//                            v.Q++;
                        }
                    }
                    v.N++;
                    v = v.parent;
                }
            }

            default -> {
                while (v != null) {
                    v.Q += delta;
                    v.N += 1;
                    v = v.parent;
                }
            }
        }

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


    @Override
    public String getNameAi() {
        return "MCTS";
    }

    @Override
    public long getTimeNeeded() {
        return timeNeeded;
    }

}
