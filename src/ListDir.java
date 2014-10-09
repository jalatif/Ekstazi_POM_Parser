import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manshu on 10/8/14.
 */
public class ListDir {
    ArrayList<String> pom_paths;

    public ArrayList<String> ListDir(String path){
        pom_paths = new ArrayList<String>(5);
        ListDirRecursively(path);
        return pom_paths;
    }
    private void ListDirRecursively(String path){
        File folder = new File(path);
        File[] dir_files = folder.listFiles();
        for (File file : dir_files){
            if (file.isDirectory())
                ListDirRecursively(file.getPath());
            else
                if (file.getName().equalsIgnoreCase("pom.xml")){
                    pom_paths.add(file.getAbsolutePath());
                    System.out.println(file.getAbsolutePath());
                }
        }
    }
    public static void main(String args[]){
        String current_path = System.getProperty("user.dir");
        System.out.println("Current Path = " + current_path);
        String path = ".";
        if (args.length > 0)   path = args[0];

        ListDir ld = new ListDir();
        ld.ListDir(path);
    }
}
