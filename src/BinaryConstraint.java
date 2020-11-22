import java.util.* ;

public final class BinaryConstraint {
  private int firstVar, secondVar ;
  private ArrayList<BinaryTuple> tuples ;
  
  public BinaryConstraint(int fv, int sv, ArrayList<BinaryTuple> t) {
    firstVar = fv ;
    secondVar = sv ;
    tuples = t ;
  }

  public int getFirstVar(){return firstVar;}
  public int getSecondVar(){return secondVar;}

  public LinkedHashSet<Integer> getFirstVarSupported(LinkedHashSet<Integer> domain){
    LinkedHashSet<Integer> res = new LinkedHashSet<>();
    for(BinaryTuple tuple: tuples){
      if(domain.contains(tuple.getVal2())){
        res.add(tuple.getVal1());
      }
    }
    return res;
  }

  public LinkedHashSet<Integer> getSecondVarSupported(LinkedHashSet<Integer> domain){
    LinkedHashSet<Integer> res = new LinkedHashSet<>();
    for(BinaryTuple tuple: tuples){
      if(domain.contains(tuple.getVal1())){
        res.add(tuple.getVal2());
      }
    }
    return res;
  }
  
  public String toString() {
    StringBuffer result = new StringBuffer() ;
    result.append("c("+firstVar+", "+secondVar+")\n") ;
    for (BinaryTuple bt : tuples)
      result.append(bt+"\n") ;
    return result.toString() ;
  }
  
  // SUGGESTION: You will want to add methods here to reason about the constraint
}
