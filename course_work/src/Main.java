import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = "/Users/dmitryz/Desktop/java_ex/course_work/csv_files/74040110_230429070000.csv";
        String secondPath = "/Users/dmitryz/Desktop/java_ex/course_work/csv_files/pdk_normatives.csv";

        LinkedHashMap<String, String> currentFile = readCSV(path);
        LinkedHashMap<String, Double> normativeMap = getNormativeMap(secondPath);
        LinkedHashMap<String, String> normativeNames = getNormativeList(secondPath);
        List<String> troubleList = checkValues(currentFile, normativeMap, normativeNames);

    }
    
    public static LinkedHashMap<String, String> readCSV (String path) {
        BufferedReader br = null;
        String cvsSplitBy = ";";
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        try {
            br = new BufferedReader(new FileReader(path));
            String[] keys = br.readLine().split(cvsSplitBy);
            String[] values = br.readLine().split(cvsSplitBy);
            for (int i = 0; i < keys.length; i++) {
                if (i >= values.length) {
                    linkedHashMap.put(keys[i], "");
                }
                else {
                    linkedHashMap.put(keys[i], values[i]);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка: нет файла/неверно указан путь");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return linkedHashMap;
    }

    public static LinkedHashMap<String, Double> getNormativeMap (String path) {
        String csvSplitBy = ",";
        String line = "";
        String[] arrValues;
        LinkedHashMap<String, Double> linkedHashMap = new LinkedHashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                arrValues = line.split(csvSplitBy);
                if (arrValues[0].equals("id")) continue;
                linkedHashMap.put(arrValues[1], Double.parseDouble(arrValues[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linkedHashMap;
    }

    public static LinkedHashMap<String, String> getNormativeList (String path) {
        String csvSplitBy = ",";
        String line = "";
        String[] arrValues;
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while ((line = br.readLine()) != null) {
                arrValues = line.split(csvSplitBy);
                if (arrValues[0].equals("id")) continue;
                linkedHashMap.put(arrValues[1], arrValues[4]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linkedHashMap;
    }

    public static List<String> checkValues (LinkedHashMap<String, String> file,
                                            LinkedHashMap<String, Double> normatives,
                                            LinkedHashMap<String, String> normativeNames) {
        List<String> troubleList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : normatives.entrySet()) {
            if (entry.getKey().equals("T")) break;
            String current = file.get(entry.getKey());
            if (current == null || current.equals("")) continue;
            if (Double.parseDouble(current) > entry.getValue()) {
                String tmp = "Содержание " + entry.getKey() + " (" + normativeNames.get(entry.getKey()) + ") " +
                        "на " + ((Double.parseDouble(current) / entry.getValue() * 100) - 100) + "% выше нормы";
                troubleList.add(tmp);
            }
        }
        return troubleList;
    }

    public static void makeFile (List<String> troubleList) {
        
    }

}

