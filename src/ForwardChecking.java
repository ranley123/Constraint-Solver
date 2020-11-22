import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class ForwardChecking extends Solver{
    private HashSet<Variable> past = new HashSet<>();

    private HashSet<Variable> future = new HashSet<>();

    private LinkedHashMap<Integer, Integer> solution = new LinkedHashMap<>();

    public ForwardChecking(BinaryCSP csp) {
        super(csp);
        future = csp.getVariables();
    }

    public Variable getNextVariable(){
        return future.iterator().next();
    }


    @Override
    public LinkedHashMap<Integer, Integer> solve() {
        if(completeAssignment()){
            printSolution();
            return solution;
        }

        Variable nextVar = getNextVariable();
        int value = nextVar.getNextValue();
//        System.out.println("Node: " + nextVar + " -> " + value);


        branchLeft(nextVar, value);
        branchRight(nextVar, value);
//        System.out.println(csp.getConstraintMap());
//        System.out.println(future);

        return null;
    }

    private void assign(Variable variable, int value){
        variable.assign(value);
        past.add(variable);
        future.remove(variable);
    }

    private boolean completeAssignment(){
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


    private void unassign(Variable variable){
        variable.unassign();
        past.remove(variable);
        future.add(variable);
    }

    private void branchLeft(Variable variable, int value){
//        System.out.println("left branch: " + variable + " -> " + value);
        assign(variable, value);
        propagate(variable);

        unassign(variable);
    }

    private void branchRight(Variable variable, int value){
//        System.out.println("right branch: " + variable + " -> " + value);

        variable.removeFromDomain(value);
        if(variable.isConsistent()){
            propagate(variable);
        }
        variable.addToDomain(value);
    }

    private void propagate(Variable variable){
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

    private boolean reviseFutureArcs(Variable variable, LinkedHashSet<Pruning> prunings){
        for(Variable futureVar: future){
            if(futureVar.equals(variable))
                continue;
            Arc arc = new Arc(futureVar, variable);
            increaseRevisionTimes();
            Pruning pruning = arc.prune(csp);
            prunings.add(pruning);

            if(!futureVar.isConsistent())
                return false;
        }
        return true;
    }


    private void printSolution(){
        for(Map.Entry<Integer, Integer> entry: solution.entrySet()){
            System.out.println("Var " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
