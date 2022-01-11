package logic.mcts;

import logic.Move;
import logic.State;
import logic.enums.Side;
import logic.player.AIPlayer;

import java.util.List;
import java.util.Random;

public class MCTSAgent extends AIPlayer {

    State currentState;
    GameTree tree;
    Side player;
    Node root;
    int numIterations;
    public static Random random;
    static final double c = 1.4;


    public MCTSAgent(Side player, int numIterations) {
        this.player = player;
        this.numIterations = numIterations;
        random = new Random();
    }

    @Override
    public Move chooseMove(State state) {
        //this is where we create the GameTree and start the MCTS process
        currentState = state;
        root = new Node(new TreeState(currentState.getBoard(), player, 0), currentState.diceRoll);
        tree = new GameTree(root, player);
        Action action = expectiMCTS(tree);
        System.out.println("ACTION TAKEN: [" + action + "] ACTION TYPE: " + action.type);
        System.out.println(root.toString());
        Move move = new Move(action.piece, action.origin, action.destination, state.diceRoll, player);
        System.out.println("TREE SIZE: " + tree.getTreeNodes().size());
        root.state.board.printBoard();
        System.out.println("--------------------------------------------------------------------");
        return move;
    }

    int i;

    public Action expectiMCTS(GameTree tree) {
        for (i = 0; i < numIterations * 2; i++) {
            Node leaf = select(tree.getRoot());
            if (leaf.state.isTerminal()) {
                backup(leaf, leaf.state.reward(tree));
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

        if (bestExpected != mostVisited) {
            System.out.println("HIGHEST EXPECTED ACTION NOT MOST VISITED");
        }

        //Node best = bestChild(root);
        Action h = chooseAction(root, mostVisited);
        return h;
        //return mostVisited.action;
    }

    public Action chooseAction(Node root, Node mostVisited) {
        Action best = mostVisited.action;
        double value = mostVisited.getExpectedValue() * best.score;
        for (Node n : root.children) {
            Action a = n.action;
            if (a.type == Action.ActionType.WIN) {
                return a;
            } else if (a.type == Action.ActionType.CAPTURE) {
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
        double value = (v.N < 1) ? 1 : v.Q / v.N; //v.getExpectedValue();
        double uct = value + ((C * Math.sqrt(Math.log(v.parent.N) / v.N)));

        return uct;
    }

    public Node select(Node node) {
        //System.out.println("SELECTING...");
        if (node.state.isTerminal()) {
            //System.out.println("SELECTED A TERMINAL NODE");
            return node;
        }

        if (!node.fullyExpanded()) {
            //System.out.println("SELECTED NODE NOT FULLY EXPANDED");
            Node q = expand(node);
            if (q.type == Node.NodeType.CHANCE) {
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
        if (node.type == Node.NodeType.CHANCE) {
            //System.out.println("Expanding Chance...");
            //chance node not fully expanded -> there are more dice rolls available to explore
            Node q = new Node(node, node.state, node.action, Node.NodeType.DECISION); //must include dice roll
            node.addChild(q);
            tree.addNode(q);
            return q;
        } else {
            //decision node
            //System.out.println("Expanding Decision...");
            Action a = node.getNextAction();
            TreeState nextState = node.state.apply(a); //updated player as well
            Node q = new Node(node, nextState, a, Node.NodeType.CHANCE);
            node.addChild(q);
            tree.addNode(q);
            return q;
        }
    }

    public Node bestChild(Node node) {
        //System.out.println("Getting best child...");
        if (node.type == Node.NodeType.CHANCE) {
            //System.out.println("STOCHASTIC EVENT");
            return node.children.get(random.nextInt(node.children.size()));
        } else {
            //UCT
            Node best = node.children.get(0);
            //System.out.println("Decision children size: " + node.children.size());
            double uct = UCT(best, c);
            for (Node n : node.children) {
                double val = UCT(n, c);
                if (val > uct) {
                    best = n;
                    uct = val;
                }
            }
            //System.out.println("Best UCT: " + uct);
            return best;
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
            if (state.isTerminal()) {
                double delta = state.reward(tree);
                //System.out.println("DELTA in j = " + j + " turn delta: " + delta);
                return delta;
            }

            if (state.getRolls().isEmpty()) {
                System.out.println("NO ROLLS");
                //System.out.println("TERMINAL? " + state.isTerminal());
                return 0;
            }

            //playout policy
            List<Integer> rolls = state.getRolls();
            //System.out.println(rolls);
            int roll = rolls.get(random.nextInt(rolls.size()));
            //System.out.println("rolled: " + roll);
            List<Action> actions = state.getAvailableActions(roll);
            //System.out.println(actions);
            assert !actions.isEmpty();
            //System.out.println("--- available: " + actions.size());
            Action a = actions.get(0);
            //System.out.println("--- action: " + a);
            state = state.apply(a);
            //System.out.println("--- iteration: " + j);

            //swap player

        }
        //no terminal state found in 200 moves
        return 0;
    }

    public void backup(Node v, double delta) {
        //System.out.println("BACKING UP: " + delta);
        while (v.parent != null) {
            //ignores chance for now

            v.N += 1;
            v.Q += delta;
            if (v.type == Node.NodeType.CHANCE) {
                // delta = delta / ((double)v.validRolls.size());
            }
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

}
