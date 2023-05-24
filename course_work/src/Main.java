import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import com.opencsv.CSVReader;

public class Main {
    public static List<String[]> readAll (Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = new ArrayList<>();
        return list = csvReader.readAll();
    }
    public static void main(String[] args) throws Exception {
        String path = "/Users/dmitryz/Desktop/java_ex/course_work/74040110_230429070000.csv";
        String secondPath = "/Users/dmitryz/Desktop/java_ex/course_work/pdk_normatives.csv";
        HashMap<String, String> hashMap = ReadCSV(path);
        HashMap<String, Double> normativeMap = GetNormativeMap(secondPath);
        System.out.println(hashMap.get("SO2"));
        System.out.println(normativeMap.get("SO2"));
    }

    public static HashMap<String, String> ReadCSV (String path) {
        BufferedReader br = null;
        String cvsSplitBy = ";";
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            br = new BufferedReader(new FileReader(path));
            String[] keys = br.readLine().split(cvsSplitBy);
            String[] values = br.readLine().split(cvsSplitBy);
            for (int i = 0; i < values.length; i++) {
                hashMap.put(keys[i], values[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return hashMap;
    }

    public static HashMap<String, Double> GetNormativeMap (String path) {
        String csvSplitBy = ",";
        String line = "";
        String[] arrValues;
        HashMap<String, Double> hashMap = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                arrValues = line.split(csvSplitBy);
                if (arrValues[0].equals("id")) continue;
                hashMap.put(arrValues[1], Double.parseDouble(arrValues[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }
}
