import java.util.LinkedHashSet;

public class Pruning {
    private Variable variable;

    private LinkedHashSet<Integer> removedValues;

    public Pruning(Variable variable, LinkedHashSet<Integer> removedValues){
        this.variable = variable;
        this.removedValues = removedValues;
    }

    public Variable getVariable(){return variable;}

    public void undo(){
        for(Integer value: removedValues){
            variable.addToDomain(value);
        }
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
