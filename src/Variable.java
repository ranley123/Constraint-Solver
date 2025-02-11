import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Variable {
    private int id;
    private Integer value = null;
    private Set<Integer> domain;

    public Variable(int id, Set<Integer> domain) {
        this.id = id;
        this.domain = domain;
    }

    public int getId(){
        return id;
    }

    public int getValue(){
        return value;
    }

    public void assign(int value){
        this.value = value;
    }

    public void unassign(){
        value = null;
    }

    public void removeFromDomain(int value){
        domain.remove(value);
    }

    public void addToDomain(int value){
        domain.add(value);
    }

    public int getDomainSize(){
        return domain.size();
    }

    /**
     * @return  - a HashSet for domain
     */
    public LinkedHashSet<Integer> getDomain(){
        if(value == null){
            return new LinkedHashSet<>(domain);
        }
        else{ // if the variable is assigned then its domain is its value
            LinkedHashSet<Integer> res = new LinkedHashSet<>();
            res.add(value);
            return res;
        }
    }

    /**
     * @return  - true if the variable is consistent
     */
    public boolean isConsistent(){
        return domain.size() > 0;
    }

    /**
     * @return  - the next value
     */
    public int getNextValue(){
        return domain.iterator().next();
    }

    /**
     * Given a set of values, find values inside the domain that are not in the given set
     * This is used for pruning variable's domain values. Given a set of supported values,
     * remove other values in the domain
     * @param supportedValues
     * @return
     */
    public LinkedHashSet<Integer> remainValues(LinkedHashSet<Integer> supportedValues){
        LinkedHashSet<Integer> removed = new LinkedHashSet<>();
        Iterator<Integer> iterator = domain.iterator();
        while(iterator.hasNext()){
            int value = iterator.next();
            if(!supportedValues.contains(value)){
                iterator.remove();
                removed.add(value); // update the domain
            }
        }
        return removed;
    }

    @Override
    public int hashCode(){
        return id;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Variable)){
            return false;
        }
        Variable other = (Variable)o;
        return this.id == other.id;
    }

    @Override
    public String toString(){
        return "Var " + getId() + ": " + domain;
    }

}
