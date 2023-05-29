package mainPackage;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.ServerException;
import com.yandex.disk.rest.json.Resource;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class DownloadNewFile {
    public static String downloadFile(String token) throws ServerException, IOException {
        String path = "/Users/dmitryz/Desktop/java_ex/course_work/csv_files";
        Date date = new Date();
        RestClient restClient = new RestClient
                (new Credentials("onezsie", token));
        ResourcesArgs resourcesArgs = new ResourcesArgs.Builder().setLimit(10)
                .setOffset(0)
                .setFields("item.path, item.size")
                .setMediaType("")
                .build();
        var list = restClient.getLastUploadedResources(resourcesArgs);
        Resource s = list.getItems().get(0);
        File file = new File(path, date.toString() + ".csv");
        restClient.downloadFile(s.getPath().toString(), file, null);
        System.out.println("Новый файл успешно загружен.");
        return (file.getPath());
    }
}
