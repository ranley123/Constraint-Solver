import java.util.LinkedHashSet;

public class MaintainingArcConsistency extends Solver{
    public MaintainingArcConsistency(BinaryCSP csp) {
        super(csp);
    }

    @Override
    protected boolean reviseFutureArcs(Variable variable, LinkedHashSet<Pruning> prunings) {
        LinkedHashSet<Arc> queue = new LinkedHashSet<>();
        for(Variable futureVar: future){
            if(csp.hasConstraint(futureVar, variable)){
                queue.add(new Arc(futureVar, variable));
            }
        }

        while(!queue.isEmpty()){
            Arc arc = getNextArc(queue);
            increaseRevisionTimes();
            Pruning pruning = arc.prune(csp);
            prunings.add(pruning);

            Variable dependent = arc.getDependent();
            if(!dependent.isConsistent()){
                return false;
            }

            if(pruning.isChanged()){
                Variable supporter = arc.getSupporter();
                for(Variable futureVar: future){
                    if(! futureVar.equals(supporter)){
                        if(csp.hasConstraint(futureVar, dependent)){
                            queue.add(new Arc(futureVar, dependent));
                        }
                    }
                }
            }
        }
        return true;
    }

    private Arc getNextArc(LinkedHashSet<Arc> queue){
        Arc arc = queue.iterator().next();
        queue.remove(arc);
        return arc;
    }
}