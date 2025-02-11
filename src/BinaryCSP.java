import java.util.* ;

public final class BinaryCSP {
  private int[][] domainBounds ;
  private ArrayList<BinaryConstraint> constraints ;
  // mapping from variable 1 and variable 2 to corresponding constraints
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

  /**
   * Return the constraint which has var1 and var2
   * @param var1Id  - the first variable it contains
   * @param var2Id  - the second variable it contains
   * @return        - the constraint
   */
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

  /**
   * Based on domainBounds, construct a set of variables
   * @return
   */
  public HashSet<Variable> getVariables(){
    HashSet<Variable> vars = new HashSet<>();
    for(int i = 0; i < domainBounds.length; i++){
      Variable var = new Variable(i, new HashSet<Integer>());
      int lowerBound = getLB(i);
      int upperBound = getUB(i);

      // add domain
      for(int j = lowerBound; j <= upperBound; j++){
        var.addToDomain(j);
      }
      vars.add(var);
    }
    return vars;
  }

  /**
   * check if there is a constraint
   * @param var1
   * @param var2
   * @return
   */
  public boolean hasConstraint(Variable var1, Variable var2){
    int id1 = var1.getId();
    int id2 = var2.getId();
    BinaryConstraint one = getConstraint(id1, id2);
    BinaryConstraint two = getConstraint(id2, id1);
    return one != null || two != null;
  }
}
