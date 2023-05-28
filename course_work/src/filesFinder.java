import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public class filesFinder {
    public static void main(String[] args) {
        File root = new File("/Users/dmitryz/Desktop/java_ex/course_work/csv_files");

        try {
            String[] extensions = {"csv"};
            boolean recursive = true;

            Collection<File> files = FileUtils.listFiles(root, extensions, recursive);

            for (File o : files) {
                System.out.println(o.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
