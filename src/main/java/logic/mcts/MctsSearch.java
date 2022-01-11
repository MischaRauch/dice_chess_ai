package logic.mcts;

public class MctsSearch {
    /*
    http://www.incompleteideas.net/609%20dropbox/other%20readings%20and%20resources/MCTS-survey.pdf

    Each node represents a state.
    Each edge represents an action resulting in child node state
    General MCTS approach PseudoCode:
        function MCTSearch(State0)
            create root node v0 with state0
            while withing computation budget do:
                vL = TreePolicy(v0)                 //vL is the last node reached during tree policy (Selection) phase corresponds to state sL
                delta = DefaultPolicy(s(vL))        //reward obtained from running default policy from vL (sL)
                Backup(vL, delta)
            return a(BestChild(v0))                 //return action a that leads to the best child of the root node v0 -> "best" is defined by the implantation

     UCT/UCB approach:
     The value of a child node is the expected reward approximated by the MC simulations
        -> Rewards correspond to random variables with unknown distributions
        -> unvisited children are assigned the largest possible value, to ensure that all children of a node are
            considered at least once before expanding further -> results in "powerful form of iterated local search"

     */

    /*
    page 9 - http://www.incompleteideas.net/609%20dropbox/other%20readings%20and%20resources/MCTS-survey.pdf
    UCT Algorithm:
        function UCTSearch(s0):
            create root node v0 with state s0
            while (within computational budget) do:
                vl <- TreePolicy(v0)
                delta <-  DefaultPolicy(s(vl))
                Backup(vl, delta)
            return a(BestChild(v0, 0))

        function TreePolicy(v):
            while v is non-terminal do:
                if v not full expanded then:
                    return Expand(V)
                else:
                    v <- BestChild(v, Cp)
            return v

        function Expand(v):
            choose action a from the set of untried actions from A(s(v))        //A(s(v)) legal actions from the state corresponding to node v
            add new child v' to v
                with s(v') = f(s(v), a)     //the state of the child node v' is the result of applying action a to the state of parent node v
                and a(v') = a               //nodes contain state action pairs
            return v'

        function BestChild(v, c):
            return child node v' which maximizes ( Q(v')/N(v') + c * sqrt[2*ln(N(v)) / N(v')]) //a UCT selection formula

        function DefaultPolicy(s)
            while s is non-terminal do:
                choose action a from A(s) uniformly at random
                s <- f(s, a)    //update s to be the state after applying action a from previous s
            return reward for state s

        function Backup(v, delta):


     */
}
