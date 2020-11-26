import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;

public class Eval {
    public static void main(String[] args) {
        File output = new File("eval.txt");
        FileWriter writer;
        HashSet<String> files = new HashSet<>();
        BinaryCSPReader reader = new BinaryCSPReader();
        BinaryCSP csp;
        HashSet<String> solvers = new HashSet<>();

        solvers.add("fc");
        solvers.add("mac");


        try{
            files = listFiles("./examples");
//            files.add("4Queens.csp");
            writer = new FileWriter(output);
            writer.write("filename solver variableOrder solutionTime(ms) nodes revisions\n");

            for(String filename: files){
                System.out.println("Running: " + filename);
                csp = reader.readBinaryCSP("./examples/" + filename);
                for(String solverName: solvers){
                    writer.write(filename + " ");
                    start(csp, writer, solverName, 2);
                }
            }

            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void start(BinaryCSP csp, FileWriter writer, String solverName, int variableOrder) throws IOException {
        Solver solver = Main.getSolver(solverName, csp);
        solver.run();
        long solutionTime = solver.getSolutionTime();
        int nodes = solver.getNumberOfNodes();
        int revisions = solver.getRevisionTimes();

        writer.write(solverName + " ");
        writer.write(variableOrder + " ");
        writer.write(solutionTime + " ");
        writer.write(nodes + " ");
        writer.write(revisions + "\n");
    }

    public static HashSet<String> listFiles(String dir) throws IOException {
        HashSet<String> fileList = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.getFileName()
                            .toString());
                }
            }
        }
        return fileList;
    }

}


