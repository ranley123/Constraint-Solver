public class Main {
    public static void main(String[] args){
        // [csp filename]
        String cspFilename = args[0];
        BinaryCSPReader reader = new BinaryCSPReader();
        BinaryCSP csp = reader.readBinaryCSP(cspFilename);
        Solver solver = getSolver("fc", csp);
        solver.solve();
//        System.out.println(csp);
    }

    public static Solver getSolver(String solverName, BinaryCSP csp){
        switch (solverName){
            case "fc":
                return new ForwardChecking(csp);
            case "ac":
                return new MaintainingArcConsistency(csp);
            default:
                return null;
        }
    }


}
