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

        try{
            //files = listFiles("./examples");
            files.add("FinnishSudoku.csp");
            writer = new FileWriter(output);

            for(String filename: files){
                csp = reader.readBinaryCSP("./examples/" + filename);
                writer.write(filename + " ");
                start(csp, writer, "fc", 1);
                start(csp, writer, "fc", 2);
                start(csp, writer, "mac", 1);
                start(csp, writer, "mac", 2);
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


