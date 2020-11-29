import java.util.LinkedHashSet;

public class MaintainingArcConsistency extends Solver{
    public MaintainingArcConsistency(BinaryCSP csp) {
        super(csp);
    }

    /**
     *
     * @param variable  - variable newly updated
     * @param prunings  - a HashSet to hold all prunings
     * @return
     */
    @Override
    protected boolean reviseFutureArcs(Variable variable, LinkedHashSet<Pruning> prunings) {
        // maintain a queue to hold all Arcs to be checked
        LinkedHashSet<Arc> queue = new LinkedHashSet<>();

        // initialise the queue by add all Arcs connecting to the current variable
        // since all affected variables all only because of this variable
        for(Variable futureVar: future){
            // if there is a constraint for these 2 variables, then add it
            if(csp.hasConstraint(futureVar, variable)){
                queue.add(new Arc(futureVar, variable));
            }
        }

        // process until there is no updated Arc to be processed
        while(!queue.isEmpty()){
            // get the next Arc in the queue
            Arc arc = queue.iterator().next();
            queue.remove(arc);
            increaseRevisionTimes();

            // prune the Arc and record pruning information for further undoing
            Pruning pruning = arc.prune(csp);
            prunings.add(pruning);

            // dependent is the updated future variable here
            Variable dependent = arc.getDependent();

            // if the future variable / dependent variable is not consistent
            if(!dependent.isConsistent()){
                return false;
            }

            // if the future variable is changed
            if(pruning.isChanged()){
                Variable supporter = arc.getSupporter();

                // add all Arcs connecting to this changed future variables to the queue
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
}