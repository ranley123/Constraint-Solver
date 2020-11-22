import java.util.* ;

public final class BinaryCSP {
  private int[][] domainBounds ;
  private ArrayList<BinaryConstraint> constraints ;
  private LinkedHashMap<Integer, LinkedHashMap<Integer, BinaryConstraint>> constraintMap;
  
  public BinaryCSP(int[][] db, ArrayList<BinaryConstraint> c) {
    domainBounds = db ;
    constraints = c ;
    constraintMap = new LinkedHashMap<>();

    // init constraintMap
    for(BinaryConstraint constraint: constraints){
      int var1 = constraint.getFirstVar();
      int var2 = constraint.getSecondVar();

      if(!constraintMap.containsKey(var1)){
        LinkedHashMap<Integer, BinaryConstraint> innerConstraint = new LinkedHashMap<>();
        constraintMap.put(var1, innerConstraint);
      }
      LinkedHashMap<Integer, BinaryConstraint> innerConstraint = constraintMap.get(var1);
      innerConstraint.put(var2, constraint);
    }
  }

  public BinaryConstraint getConstraint(Integer var1Id, Integer var2Id){
    if(!constraintMap.containsKey(var1Id)){
      return null;
    }
    return constraintMap.get(var1Id).get(var2Id);
  }

  public LinkedHashMap<Integer, LinkedHashMap<Integer, BinaryConstraint>> getConstraintMap(){
    return constraintMap;
  }
  
  public String toString() {
    StringBuffer result = new StringBuffer() ;
    result.append("CSP:\n") ;
    for (int i = 0; i < domainBounds.length; i++)
      result.append("Var "+i+": "+domainBounds[i][0]+" .. "+domainBounds[i][1]+"\n") ;
    for (BinaryConstraint bc : constraints)
      result.append(bc+"\n") ;
    return result.toString() ;
  }
  
  public int getNoVariables() {
    return domainBounds.length ;
  }
  
  public int getLB(int varIndex) {
    return domainBounds[varIndex][0] ;
  }
  
  public int getUB(int varIndex) {
    return domainBounds[varIndex][1] ;
  }
  
  public ArrayList<BinaryConstraint> getConstraints() {
    return constraints ;
  }

  public HashSet<Variable> getVariables(){
    HashSet<Variable> vars = new HashSet<>();
    for(int i = 0; i < domainBounds.length; i++){
      Variable var = new Variable(i, new HashSet<Integer>());
      int lowerBound = getLB(i);
      int upperBound = getUB(i);

      for(int j = lowerBound; j <= upperBound; j++){
        var.addToDomain(j);
      }
      vars.add(var);
    }
    return vars;
  }
}
