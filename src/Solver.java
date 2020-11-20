import java.util.LinkedHashMap;

public abstract class Solver {
    BinaryCSP csp;
    public Solver(BinaryCSP csp){
        this.csp = csp;
    }

    public abstract LinkedHashMap<Integer, Integer> solve();

}
