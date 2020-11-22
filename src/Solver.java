import java.util.LinkedHashMap;

public abstract class Solver {
    BinaryCSP csp;
    private long solutionTime;
    private int numberOfNodes = 0;
    private int revisionTimes = 0;

    public long getSolutionTime(){
        return solutionTime;
    }

    public int getNumberOfNodes(){
        return numberOfNodes;
    }

    public int getRevisionTimes(){
        return revisionTimes;
    }

    public void increaseNumberOfNodes(){
        numberOfNodes++;
    }

    public void increaseRevisionTimes(){
        revisionTimes++;
    }

    public BinaryCSP getCsp(){
        return csp;
    }

    public LinkedHashMap<Integer, Integer> run(){
        long start = System.currentTimeMillis();

        LinkedHashMap<Integer, Integer> solution = solve();

        long end = System.currentTimeMillis();
        solutionTime = end - start;
        return solution;
    }

    public Solver(BinaryCSP csp){
        this.csp = csp;
    }

    public abstract LinkedHashMap<Integer, Integer> solve();

}
