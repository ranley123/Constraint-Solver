import java.util.*;

public abstract class Solver {
    BinaryCSP csp; // the csp problem
    private long solutionTime; // the solution time to obtain all solutions
    private int numberOfNodes = 0; // the number of nodes to be recorded
    private int revisionTimes = 0; // the revision taken
    protected HashSet<Variable> past = new HashSet<>(); // the past variables already assigned
    protected HashSet<Variable> future = new HashSet<>(); // the future variables to be assigned
    public ArrayList<LinkedHashMap<Integer, Integer>> solutions = new ArrayList<>(); // solutions
    long start;

    /**
     * @return  - solution time
     */
    public long getSolutionTime(){
        return solutionTime;
    }

    /**
     * @return  - number of nodes
     */
    public int getNumberOfNodes(){
        return numberOfNodes;
    }

    /**
     * @return  - revision times
     */
    public int getRevisionTimes(){
        return revisionTimes;
    }

    /**
     * Add the current number of nodes
     */
    public void increaseNumberOfNodes(){
        numberOfNodes++;
    }

    /**
     * Add the current revision times
     */
    public void increaseRevisionTimes(){
        revisionTimes++;
    }

    /**
     * @return  - csp problem
     */
    public BinaryCSP getCsp(){
        return csp;
    }

    /**
     * Since future variables are stored in a HashSet and its hashcode() is overwritten,
     * it directly stores variables in an ascending way
     * @return  - the next future variable to be assigned
     */
    public Variable getNextVariable(){
        return future.iterator().next();
    }

    /**
     * Entry point to solve a problem
     */
    public void run(){
        numberOfNodes = 0;
        revisionTimes = 0;

        start = System.currentTimeMillis();
        solve(); // solve problem
        long end = System.currentTimeMillis();
        solutionTime = end - start;
        printStats();

        for(LinkedHashMap<Integer, Integer> solution: solutions){
//            printSolution(solution);
        }
    }

    /**
     * solve the problem in 2-branch
     */
    public void solve() {
        // for each solve(), increases the number of nodes
        increaseNumberOfNodes();
        // if assignment completes, then a solution is found
        if(completeAssignment()){
            LinkedHashMap<Integer, Integer> solution = new LinkedHashMap<>();

            for(Variable pastVar: past){
                solution.put(pastVar.getId(), pastVar.getValue());
            }
            // add the current solution
            solutions.add(solution);
            long end = System.currentTimeMillis();
            solutionTime = end - start;

            printSolution(solution);
            printStats();
            System.exit(0);
            return;
        }

        // when get the next variable:
        // 1. ascending variable order
        // 2. smallest domain value
        Variable nextVar = null;
        if(Main.variableOrder == 1){
            nextVar = getNextVariable();
        }
        else{
            nextVar = getSmallestDomainVariable();
        }

        // ascending value ordering
        int value = nextVar.getNextValue();

        branchLeft(nextVar, value); // left -> var = value
        branchRight(nextVar, value); // right -> var != value
    }

    /**
     * Assign a value to a variable
     * @param variable  - the variable
     * @param value     - the value to be assigned
     */
    protected void assign(Variable variable, int value){
        variable.assign(value);
        past.add(variable); // add the variable to the past
        future.remove(variable); // remove the variable from the future
    }

    /**
     * Unassign a value from a variable
     * @param variable  - the variable
     */
    protected void unassign(Variable variable){
        variable.unassign();
        past.remove(variable); // remove the variable from past
        future.add(variable); //  add the variable to future
    }

    /**
     * Takes the left branch
     * @param variable  - variable to be assigned values
     * @param value     - value to be assigned
     */
    protected void branchLeft(Variable variable, int value){
        assign(variable, value);
        propagate(variable);
        unassign(variable);
    }

    /**
     * Takes the right branch
     * @param variable  - the variable to be updated
     * @param value     - the value assigned
     */
    protected void branchRight(Variable variable, int value){
        variable.removeFromDomain(value); // remove value from its domain
        // check if the variable is still consistent to continue
        if(variable.isConsistent()){
            propagate(variable);
        }
        // recovering
        variable.addToDomain(value);
    }

    /**
     * @return  - true if there is no future variable
     */
    protected boolean completeAssignment(){
        return future.isEmpty();
    }

    /**
     * The smallest-domain first variable ordering
     * @return  - the next variable to be assigned
     */
    protected Variable getSmallestDomainVariable(){
        Variable nextVar = null;
        int min = Integer.MAX_VALUE;

        // for each future variable, find one with smallest domain
        for(Variable variable: future){
            if(variable.getDomainSize() < min){
                min = variable.getDomainSize();
                nextVar = variable;
                // cannot be better than 1, so directly return
                if(min == 1){
                    break;
                }
            }
        }
        return nextVar;
    }

    /**
     * To propogate the change of variable
     * @param variable  - the variable newly assigned with a value
     */
    protected void propagate(Variable variable){
        // a HashSet to record all future variables which are updated
        LinkedHashSet<Pruning> prunings = new LinkedHashSet<>();

        // if all future variables are still consistent, then continue to solve
        if(reviseFutureArcs(variable, prunings)){
            solve();
        }
        else{
//            System.out.println("domain inconsistent!");
        }

        // recovering mechanism: undo prunings
        for(Pruning pruning: prunings){
            pruning.undo();
        }
    }

    /**
     * constructor
     * @param csp   - csp problem
     */
    public Solver(BinaryCSP csp){
        this.csp = csp;
        // initialise the future variables
        future = csp.getVariables();
    }

    /**
     * Since there are different solvers (Forward Checking and MAC)
     * they would have different ways to revise future arcs
     * @param variable  - variable newly updated
     * @param prunings  - a HashSet to hold all prunings
     * @return          - true if all future variables are consistent
     */
    protected abstract boolean reviseFutureArcs(Variable variable, LinkedHashSet<Pruning> prunings);

    /**
     * Print a solution
     * @param solution  - solution to be printed
     */
    public void printSolution(LinkedHashMap<Integer, Integer> solution){
        System.out.println("------------Solution----------");
        for(Map.Entry<Integer, Integer> entry: solution.entrySet()){
            System.out.println("Var " + entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * Print statistics, such as solution time, etc.
     */
    public void printStats(){
        System.out.println("Solution Time (ms): " + solutionTime);
        System.out.println("Nodes: " + numberOfNodes);
        System.out.println("Arc Revisions: " + revisionTimes);
    }

}
