import java.io.File;
import java.util.ArrayList;

/**
 * Created by manshu on 10/8/14.
 */
public class ListDir {
    ArrayList<String> pom_paths;

    public static void main(String args[]) {
        String current_path = System.getProperty("user.dir");
        System.out.println("Current Path = " + current_path);
        String path = "/home/manshu/Templates/EXEs/CS527SE/Homework/hw7/temp_ekstazi/continuum";
        int max_depth = -1;
        if (args.length > 0) path = args[0];
        if (args.length > 1) max_depth = Integer.parseInt(args[1]);
        ListDir ld = new ListDir();
        ld.ListDir(path, max_depth);
        for (String p : ld.pom_paths) {
            System.out.println(p);
        }
    }

    public ArrayList<String> ListDir(String path, int maxdepth){
        pom_paths = new ArrayList<String>(5);
        ListDirRecursively(path, 1, maxdepth);
        return pom_paths;
    }

    private void ListDirRecursively(String path, int depth, int maxdepth){
        if (depth > maxdepth && maxdepth != -1)
            return;
        File folder = new File(path);
        File[] dir_files = folder.listFiles();
        int pom_found = -1;
        for (File file : dir_files){
            if (file.isDirectory())
                ListDirRecursively(file.getPath(), depth + 1, maxdepth);
            else
                //if(file.getName().startsWith("pom") && file.getName().endsWith(".xml"))
                if (file.getName().matches("pom([-]\\w+)?.xml")) {
                    if (file.getName().equalsIgnoreCase("pom.xml")){
                        if (pom_found == 1) {
                            pom_found = 0;
                            continue;
                        } else
                            pom_found = 0;
                    }
                    else if (file.getName().equalsIgnoreCase("pom-main.xml")){
                        if (pom_found == 0){
                            pom_paths.remove(pom_paths.size() - 1);
                        }
                        pom_found = 1;
//                        pom_paths.clear();
//                        pom_paths.add(file.getAbsolutePath());
//                        break;
                    }
                    else{
                        continue;
                    }
                    pom_paths.add(file.getAbsolutePath());
                }
        }
    }
}
