import java.util.LinkedHashSet;

public class ForwardChecking extends Solver{
    public ForwardChecking(BinaryCSP csp) {
        super(csp);
    }
    @Override
    protected boolean reviseFutureArcs(Variable variable, LinkedHashSet<Pruning> prunings){
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
}
