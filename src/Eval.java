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
        Eval eval = new Eval();
        File output = new File("eval.csv");
        FileWriter writer;
        HashSet<String> files = new HashSet<>();

        try{
            files = eval.listFiles("./examples");
            writer = new FileWriter(output);


            writer.flush();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public HashSet<String> listFiles(String dir) throws IOException {
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


