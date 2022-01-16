package logic.mcts;

import logic.player.AIPlayer;
import logic.player.MiniMaxPlayer;
import org.jfree.data.xy.XYSeries;

import java.util.LinkedList;
import java.util.List;

import static logic.enums.Side.BLACK;
import static logic.enums.Side.WHITE;

public class SimulationFactory {

    static long inSeconds = (long) 1e9;

    AIPlayer white;
    AIPlayer black;

    double numSimulations;
    double whiteWinTotal = 0;
    double blackWinTotal = 0;
    int numDraws = 0;
    int minTreeSize = Integer.MAX_VALUE;
    int maxTreeSize = Integer.MIN_VALUE;
    int sumTreeSize = 0;
    double avgTreeSize;
    double minTime = Double.MAX_VALUE;
    double maxTime = Double.MIN_VALUE;
    double sumTime = 0;
    double averageTime = 0;


    XYSeries winTotal;
    XYSeries winRate;
    XYSeries timeNeeded;
    XYSeries timeNeededNormalized;
    XYSeries treeSize;
    XYSeries gameResult;
    XYSeries uct;

    List<XYSeries> plots;

    public SimulationFactory(AIPlayer white, AIPlayer black, int numSimulations) {
        this.numSimulations = numSimulations;
        this.white = white;
        this.black = black;
        plots = new LinkedList<>();
    }

    public static SimulationFactory create(AIPlayer white, AIPlayer black, int numSimulations) {
        return new SimulationFactory(white, black, numSimulations);
    }

    public void start() {
        double c = 0.7;
        XYSeries exploitationWeight = new XYSeries("C");
        XYSeries localCwinRate = new XYSeries("C WR");
        double localWR = 0;
        double localI = 0;
        //timeNeededNormalized = new XYSeries("Time Needed x Tree Size");
        for (int i = 1; i <= numSimulations; i++) {
            if (i % 10 == 0) {
                localWR = 0;
                localI = 1;
                c += 0.1;
            }
            localI++;
            c = 1.4;
            exploitationWeight.add(i, c);
            white = new MCTSAgent(WHITE, 2000);
            black = new MiniMaxPlayer(7, BLACK);
            GameSimulator sim = GameSimulator.StateSimulationFactory(white, black)
//                        .trackExpectedValue();
//                        .trackDepth()
                    .tuneC(c);
            ;
            sim.start();

            if (sim.victor == WHITE) {
                whiteWinTotal++;
                localWR++;
            } else if (sim.victor == BLACK) blackWinTotal++;
            else numDraws++;

            //int gameTreeSize = ((MCTSAgent) sim.white).tree.getTreeNodes().size();
            //minTreeSize = Math.min(gameTreeSize, minTreeSize);
            //maxTreeSize = Math.max(gameTreeSize, maxTreeSize);
            //sumTreeSize += gameTreeSize;
            //avgTreeSize = sumTreeSize / (double) i;
            minTime = Math.min(minTime, white.getTimeNeeded() / (double) inSeconds);
            maxTime = Math.max(maxTime, white.getTimeNeeded() / (double) inSeconds);
            sumTime += (((double) white.getTimeNeeded()) / (double) inSeconds);
            averageTime = sumTime / (double) i;
//
            //winTotal.add(i, whiteWinTotal);
            winRate.add(i, whiteWinTotal / i);
            localCwinRate.add(i, localWR / localI);
            timeNeeded.add(i, (((double) white.getTimeNeeded()) / inSeconds));
//
            //timeNeededNormalized.add(i, (((double) white.getTimeNeeded()) / (long) 1e5));
            //treeSize.add(i, gameTreeSize);
            gameResult.add(i, (sim.victor == WHITE) ? 1 : -1);
            //uct.add(i, sim.actionValue);
//
            String progress = "Game " + i + " winner: " + sim.victor.asChar() + " --- WHITE (wins: " + (int) whiteWinTotal + ", WR: " + Math.floor(whiteWinTotal * 100 / i) / 100 + ") - BLACK (wins: " + (int) blackWinTotal + ", WR: " + Math.floor(blackWinTotal * 100 / i) / 100 + ") - " + (int) (i * 100 / numSimulations) + "% complete ";
            System.out.print("\r" + progress);

            //sim.plot();

            if (sim.debug) {
                System.out.println("--------------");
                System.out.println(white.getNameAi() + " - White win average: " + whiteWinTotal / ((double) i));
                System.out.println(black.getNameAi() + " - Black win average: " + blackWinTotal / ((double) i));
                System.out.println("Draws: " + numDraws);
                System.out.println("--------------");
            }
        }

        //plot();
//        List<XYSeries> toPlot = new LinkedList<>();
//        toPlot.add(gameResult);
//        toPlot.add(exploitationWeight);
//        toPlot.add(winRate);
//        toPlot.add(timeNeeded);
        //plot(toPlot);
        plots.add(exploitationWeight);
        plots.add(localCwinRate);
        plot(plots, true);
        //plot(timeNeeded, winRate, gameResult);
        //plot(timeNeeded, winRate, gameResult);
        //plot(timeNeededNormalized, treeSize, winRate);
    }

    public void plot(XYSeries s1, XYSeries s2, XYSeries s3) {
        List<XYSeries> dataSet = new LinkedList<>();
        dataSet.add(s1);
        dataSet.add(s2);
        dataSet.add(s3);
        SimulationPlotter multiPlot = new SimulationPlotter("Title: " + white.getNameAi() + " v " + black.getNameAi(), dataSet, "idk");
        multiPlot.pack();
        multiPlot.setVisible(true);
    }

    public void plot(List<XYSeries> dataSet, boolean includesResult) {
        SimulationPlotter multiPlot = new SimulationPlotter("Title: " + white.getNameAi() + " v " + black.getNameAi(), dataSet, "idk", includesResult);
        multiPlot.pack();
        multiPlot.setVisible(true);
    }

    public void plot() {

//            for (XYSeries data : plots) {
//                final SimulationPlotter dataPlot = new SimulationPlotter(data.getDescription() +": "+ white.getNameAi() + " vs " + black.getNameAi(), data, data.getDescription());
//                dataPlot.pack();
//                //RefineryUtilities.centerFrameOnScreen(demo);
//                dataPlot.setVisible(true);
//            }

        boolean multi = true;
        if (multi) {
            SimulationPlotter multiPlot = new SimulationPlotter("Tree & Time & Result: " + white.getNameAi() + " v " + black.getNameAi(), plots, "idk");
            multiPlot.pack();
            multiPlot.setVisible(true);
        } else {
            final SimulationPlotter winTotalPlot = new SimulationPlotter("Win Rate: " + white.getNameAi() + " vs " + black.getNameAi(), winRate, "Win Rate");
            winTotalPlot.pack();
            winTotalPlot.setVisible(true);

            final SimulationPlotter winRatePlot = new SimulationPlotter("Win Total: " + white.getNameAi() + " vs " + black.getNameAi(), winTotal, "Win Total");
            winRatePlot.pack();
            winRatePlot.setVisible(true);

            final SimulationPlotter timeNeededPlot = new SimulationPlotter("Time Needed: " + white.getNameAi() + " vs " + black.getNameAi(), timeNeeded, "Time Needed");
            timeNeededPlot.pack();
            timeNeededPlot.setVisible(true);

            final SimulationPlotter treeSizePlot = new SimulationPlotter("Tree Size: " + white.getNameAi() + " vs " + black.getNameAi(), treeSize, "Tree Size");
            treeSizePlot.pack();
            treeSizePlot.setVisible(true);

            final SimulationPlotter gamesResultPlot = new SimulationPlotter("Game Results: " + white.getNameAi() + " vs " + black.getNameAi(), gameResult, "Win or Loss");
            gamesResultPlot.pack();
            gamesResultPlot.setVisible(true);
        }
    }

    public SimulationFactory trackWinRate() {
        winRate = new XYSeries("Win Rate");
        plots.add(winRate);
        return this;
    }

    public SimulationFactory trackWinTotal() {
        winTotal = new XYSeries("Win Total");
        plots.add(winTotal);
        return this;
    }

    public SimulationFactory trackTimeNeed() {
        timeNeeded = new XYSeries("Time Needed");
        plots.add(timeNeeded);
        return this;
    }

    public SimulationFactory trackTreeSize() {
        treeSize = new XYSeries("Tree Size");
        plots.add(treeSize);
        return this;
    }

    public SimulationFactory trackWinner() {
        gameResult = new XYSeries("Game Result");
        plots.add(gameResult);
        return this;
    }

    public SimulationFactory trackUCT() {
        uct = new XYSeries("UCT");
        plots.add(uct);
        return this;
    }
}
