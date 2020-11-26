import java.util.LinkedHashSet;

public class Arc {
    private Variable dependent;
    private Variable supporter;

    public Arc(Variable var1, Variable var2){
        this.dependent = var1;
        this.supporter = var2;
    }

    public Variable getDependent(){
        return dependent;
    }

    public Variable getSupporter(){
        return supporter;
    }

    /**
     * An Arc can be pruned
     * @param csp
     * @return
     */
    public Pruning prune(BinaryCSP csp){
        LinkedHashSet<Integer> supporterDomain = supporter.getDomain();
        LinkedHashSet<Integer> supported; // supported values
        LinkedHashSet<Integer> removed;

        // Since in the constraintMap, the var1 id is less than var2 id
        // when dependent id < supporter id, then one
        BinaryConstraint one = csp.getConstraint(dependent.getId(), supporter.getId());
        // when dependent id > supporter id, then two
        BinaryConstraint two = csp.getConstraint(supporter.getId(), dependent.getId());

        // get the correponding supported values from domain
        if(one != null){
            supported = one.getFirstVarSupported(supporterDomain);
        }
        else{
            supported = two.getSecondVarSupported(supporterDomain);
        }

        // find the values to be removed
        removed = dependent.remainValues(supported);

        // build a new pruning to hold removed values
        Pruning pruning = new Pruning(dependent, removed);
        return pruning;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Arc)){
            return false;
        }
        Arc other = (Arc)o;
        return this.dependent.equals(other.getDependent()) && this.supporter.equals(other.getSupporter());
    }

    @Override
    public String toString(){
        return "(" + dependent + ", " + supporter + ")";
    }

}
