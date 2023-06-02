package mainPackage;

import download.DownloadNewFile;

import java.io.*;
import java.util.*;

public class Main {
    static String yandexToken = getPasswords()[0];
    static String googleToken = getPasswords()[1];
    public static void main(String[] args) throws Exception {
        // Скачиваем последний загруженный файл с Яндекс диска, переменной присваивается путь до этого файла.
        String path = DownloadNewFile.downloadFile(yandexToken);
        // Путь к файлу с нормативами
        String secondPath = "/Users/dmitryz/Desktop/java_ex/course_work/csv_files/pdk_normatives.csv";

        // Читаем скаченный файл, добавляем информацию в связанный список
        LinkedHashMap<String, String> currentFile = readCSV(path);

        // Читаем файл с нормативами, добавляем в связный список, с которым потом будем сверять скаченные данные
        LinkedHashMap<String, Double> normativeMap = getNormativeMap(secondPath);

        // Читаем файл с нормативами и создаем список, который будет использоваться для формирования отчета
        LinkedHashMap<String, String> normativeNames = getNormativeList(secondPath);

        // Создаем лист с описанием обнаруженных отклонений (их наличие проверяется в методе)
        List<String> troubleList = checkValues(currentFile, normativeMap, normativeNames);

        // Если найдены отклонения, формируется отчет с подробным описанием, который будет отправлен на почту
        // и впоседствии удален. Если отклонений не найдено, отчет формироваться не будет.
        if (!troubleList.isEmpty()) {
           String newFilePath = makeFile(troubleList, currentFile);
           mail.SendEmail.sendEmail(googleToken, newFilePath);
           File file = new File(newFilePath);
           file.delete();
           System.out.println("В полученых данных были обнаружены отклонения от нормы, отчет отправлен на почту. " +
                   "Работа программы завершена.");
        }
        else {
            System.out.println("В полученых данных отклонения от норм не обнаружены. Работа программы завершена.");
        }
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
                        "на " + ((Double.parseDouble(current) / entry.getValue() * 100) - 100) + "% выше нормы.";
                troubleList.add(tmp);
            }
        }
        return troubleList;
    }

    public static String makeFile (List<String> troubleList, LinkedHashMap<String, String> current) {
        String path = "/Users/dmitryz/Desktop/java_ex/course_work/csv_files/report.txt";
        try (FileWriter fileWriter = new FileWriter(path, true)){
            fileWriter.append("Внимание! Пост, находящийся на ").append(current.get("Название"))
                    .append(" обнаружил ").append("превышение содержания некоторых веществ воздухе.");
            int counter = 1;
            for (String s : troubleList) {
                fileWriter.append("\n").append(String.valueOf(counter)).append(": ").append(s);
                counter++;
            }
            fileWriter.append("\n").append("Дата: ").append(current.get("Время"));
            fileWriter.append("\n\n").append("Метеопараметры: ");
            fileWriter.append("\n").append("Температура (оС): ").append(current.get("TemperatureOutside"));
            fileWriter.append("\n").append("Влажность воздуха (%): ").append(current.get("HumidityOutside"));
            fileWriter.append("\n").append("Атмосферное давление (мм.рт.ст): ").append(current.get("Barometer"));
            fileWriter.append("\n").append("Скорость ветра (м,с): " ).append(current.get("WindSpeed"));
            fileWriter.append("\n").append("Напраление ветра (град.): ").append(current.get("WindDirection"));
            fileWriter.flush();
            System.out.println("Файл с отчетом создан.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String[] getPasswords () {
        String[] pass = new String[2];
        int counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/dmitryz/Desktop/passwords.txt"))){
            String readLine = "";
            while ((readLine = br.readLine()) != null) {
                pass[counter] = readLine;
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pass;
    }

}

