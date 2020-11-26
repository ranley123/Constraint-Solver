import javax.annotation.processing.SupportedSourceVersion;

public class Main {
    public static String cspFilename;
    public static String solverName;
    public static int variableOrder;

    public static void main(String[] args){
        String usageMessage = "Usage: java Main [cspFilename] [solverName] [variableOrder]\n" +
                "[solverName] - fc or mac\n" +
                "[variableOrder] - 1 for ascending variable order, 2 for smallest domain";
        if(args.length != 3){
            System.out.println(usageMessage);
            System.exit(0);
        }

        // [csp filename]
        cspFilename = args[0];
        solverName = args[1];

        // check solvername
        if(solverName.compareTo("fc") != 0 && solverName.compareTo("mac") != 0){
            System.out.println("solverName must be fc or mac!");
            System.exit(0);
        }

        // check variable order
        try{
            variableOrder = Integer.parseInt(args[2]);
            if (variableOrder != 1 && variableOrder != 2){
                System.out.println("variableOrder must be 1 or 2!");
                System.exit(0);
            }
        }
        catch (Exception e){
            System.out.println("variableOrder must be an integer!");
            System.exit(0);
        }

        BinaryCSPReader reader = new BinaryCSPReader();
        BinaryCSP csp = reader.readBinaryCSP(cspFilename);
        Solver solver = getSolver(solverName, csp);
        solver.run();
//        System.out.println(csp);
    }

    /**
     *
     * @param solverName
     * @param csp
     * @return
     */
    public static Solver getSolver(String solverName, BinaryCSP csp){
        switch (solverName){
            case "fc":
                return new ForwardChecking(csp);
            case "mac":
                return new MaintainingArcConsistency(csp);
            default:
                return null;
        }
    }


}
