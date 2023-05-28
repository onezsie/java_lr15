import com.yandex.disk.rest.*;
import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.json.Resource;
import java.io.File;
import java.io.IOException;



public class downloadFiles {
    public static void main(String[] args) throws IOException, ServerException {
          RestClient restClient = new RestClient
                    (new Credentials("onezsie", "y0_AgAAAAA4DjB5AADLWwAAAADkFDfDFE1q11rfRFa4it7U_1xyPd0swr4"));
          ResourcesArgs resourcesArgs = new ResourcesArgs.Builder().setLimit(10)
                  .setOffset(0)
                  .setFields("item.path, item.size")
                  .setMediaType("")
                  .build();
          var list = restClient.getLastUploadedResources(resourcesArgs);
          Resource s = list.getItems().get(0);
          File file = new File("/Users/dmitryz/Desktop/java_ex/course_work/csv_files", "newFile.csv");
          restClient.downloadFile(s.getPath().toString(), file, null);
          System.out.println("Done");
          }
    }

