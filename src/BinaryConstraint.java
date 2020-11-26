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

  /**
   * Given a domain for the second variable, find corresponding valid first variables
   * @param domain
   * @return
   */
  public LinkedHashSet<Integer> getFirstVarSupported(LinkedHashSet<Integer> domain){
    LinkedHashSet<Integer> res = new LinkedHashSet<>();
    for(BinaryTuple tuple: tuples){
      if(domain.contains(tuple.getVal2())){
        res.add(tuple.getVal1());
      }
    }
    return res;
  }

  /**
   * Given a domain for the first variable, find corresponding valid second variables
   * @param domain
   * @return
   */
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
}
