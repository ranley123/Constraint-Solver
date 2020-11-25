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

    public Pruning prune(BinaryCSP csp){
        LinkedHashSet<Integer> supporterDomain = supporter.getDomain();
        LinkedHashSet<Integer> supported;
        LinkedHashSet<Integer> removed;

        BinaryConstraint one = csp.getConstraint(dependent.getId(), supporter.getId());
        BinaryConstraint two = csp.getConstraint(supporter.getId(), dependent.getId());

        if(one != null){
            supported = one.getFirstVarSupported(supporterDomain);
        }
        else{
//            System.out.println(dependent.getId());
//            System.out.println(supporter.getId());

            supported = two.getSecondVarSupported(supporterDomain);
        }
//        System.out.println("before " + dependent);
        removed = dependent.remainValues(supported);
//        System.out.println("after " + dependent);

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
