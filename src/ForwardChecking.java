import java.util.LinkedHashSet;

public class ForwardChecking extends Solver{
    public ForwardChecking(BinaryCSP csp) {
        super(csp);
    }

    /**
     *
     * @param variable  - variable newly updated
     * @param prunings  - a HashSet to hold all prunings
     * @return
     */
    @Override
    protected boolean reviseFutureArcs(Variable variable, LinkedHashSet<Pruning> prunings){
        for(Variable futureVar: future){
            // omit (x,x)
            if(futureVar.equals(variable))
                continue;
            // check if there is a constraint
            if(!csp.hasConstraint(futureVar, variable)){
                continue;
            }

            // build a new Arc, futureVar is the dependent
            Arc arc = new Arc(futureVar, variable);

            // increases revision times
            increaseRevisionTimes();

            // prune the Arc and then record related information for further undoing
            Pruning pruning = arc.prune(csp);
            prunings.add(pruning);

            // check if the future variable is consistent
            if(!futureVar.isConsistent())
                return false;
        }
        return true;
    }
}
