import java.util.LinkedHashSet;

/**
 * This class is to store pruned variables and its pruned values from its original domain.
 * It is responsible for undoing pruning.
 */
public class Pruning {
    private Variable variable; // the pruned variable
    private LinkedHashSet<Integer> removedValues; // the pruned values

    /**
     * The constructor for Pruning
     * @param variable          - the pruned variable
     * @param removedValues     - the removed values
     */
    public Pruning(Variable variable, LinkedHashSet<Integer> removedValues){
        this.variable = variable;
        this.removedValues = removedValues;
    }

    /**
     * @return  - the pruned variable
     */
    public Variable getVariable(){return variable;}

    /**
     * For each removed value, add it back to the variable's domain.
     */
    public void undo(){
        for(Integer value: removedValues){
            variable.addToDomain(value);
        }
    }

    /**
     * @return  - true if the variable's domain is changed
     */
    public boolean isChanged(){
        return removedValues.size() > 0;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Pruning))
            return false;
        Pruning other = (Pruning)o;
        return this.variable.equals(other.getVariable()) && this.removedValues.equals(other.removedValues);
    }

    @Override
    public String toString(){
        return "Pruning var " + variable + " values " + removedValues;
    }
}
