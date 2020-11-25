import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public abstract class Solver {
    BinaryCSP csp;
    private long solutionTime;
    private int numberOfNodes = 0;
    private int revisionTimes = 0;
    protected HashSet<Variable> past = new HashSet<>();
    protected HashSet<Variable> future = new HashSet<>();
    protected LinkedHashMap<Integer, Integer> solution = new LinkedHashMap<>();

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

    public Variable getNextVariable(){
        return future.iterator().next();
    }

    public LinkedHashMap<Integer, Integer> run(){
        long start = System.currentTimeMillis();

        LinkedHashMap<Integer, Integer> solution = solve();

        long end = System.currentTimeMillis();
        solutionTime = end - start;
        return solution;
    }

    protected void assign(Variable variable, int value){
        variable.assign(value);
        past.add(variable);
        future.remove(variable);
    }

    protected boolean completeAssignment(){
        if(future.isEmpty()){
            // update solution
            solution.clear();
            for(Variable pastVar: past){
                solution.put(pastVar.getId(), pastVar.getValue());
            }

            return true;
        }
        return false;
    }


    protected void unassign(Variable variable){
        variable.unassign();
        past.remove(variable);
        future.add(variable);
    }

    protected void branchLeft(Variable variable, int value){
//        System.out.println("left branch: " + variable + " -> " + value);
        assign(variable, value);
        propagate(variable);

        unassign(variable);
    }

    protected void branchRight(Variable variable, int value){
//        System.out.println("right branch: " + variable + " -> " + value);

        variable.removeFromDomain(value);
        if(variable.isConsistent()){
            propagate(variable);
        }
        variable.addToDomain(value);
    }

    protected Variable getSmallestDomainVariable(){
        Variable nextVar = null;
        int min = Integer.MAX_VALUE;
        for(Variable variable: future){
            if(variable.getDomainSize() < min){
                min = variable.getDomainSize();
                nextVar = variable;
                if(min == 1){
                    break;
                }
            }
        }
        return nextVar;
    }

    protected void propagate(Variable variable){
        LinkedHashSet<Pruning> prunings = new LinkedHashSet<>();
        if(reviseFutureArcs(variable, prunings)){
            solve();
        }
        else{
            System.out.println("domain inconsistent!");
        }

        for(Pruning pruning: prunings){
            pruning.undo();
        }
    }

    public Solver(BinaryCSP csp){
        this.csp = csp;
        future = csp.getVariables();
    }

    public LinkedHashMap<Integer, Integer> solve() {
        if(completeAssignment()){
            printSolution();
            return solution;
        }

        // when get the next variable:
        // 1. ascending variable order
        // 2. smallest domain value
        Variable nextVar = getNextVariable();
//        Variable nextVar = getSmallestDomainVariable();

        int value = nextVar.getNextValue();
//        System.out.println("Node: " + nextVar + " -> " + value);

        branchLeft(nextVar, value);
        branchRight(nextVar, value);

        return null;
    }

    protected abstract boolean reviseFutureArcs(Variable variable, LinkedHashSet<Pruning> prunings);

    public void printSolution(){
        for(Map.Entry<Integer, Integer> entry: solution.entrySet()){
            System.out.println("Var " + entry.getKey() + ": " + entry.getValue());
        }
    }

}
