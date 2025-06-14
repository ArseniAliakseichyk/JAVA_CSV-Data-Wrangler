package util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Klasa narzędziowa do operacji na plikach CSV.
 */
public class CSVUtils {
    /**
     * Odczytuje plik CSV i zwraca jego dane jako listę tablic ciągów znaków.
     *
     * @param file      plik CSV do odczytania
     * @param delimiter separator użyty w pliku CSV
     * @return dane pliku CSV
     * @throws IOException jeśli wystąpi błąd I/O
     */
    public static List<String[]> readCSV(File file, String delimiter) throws IOException {
        List<String[]> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                String[] row = parseCSVLine(line, delimiter);
                data.add(row);
            }
        }
        br.close();
        return data;
    }

    /**
     * Parsuje linię CSV na tablicę ciągów znaków.
     *
     * @param line      linia CSV do sparsowania
     * @param delimiter separator użyty w linii
     * @return tablica ciągów znaków reprezentująca pola CSV
     */
    private static String[] parseCSVLine(String line, String delimiter) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean inQuotes = false;
        char delim = delimiter.charAt(0);

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == delim && !inQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }

    /**
     * Wykrywa separator użyty w pliku CSV.
     *
     * @param file plik CSV do analizy
     * @return wykryty separator
     * @throws IOException jeśli wystąpi błąd I/O
     */
    public static String detectDelimiter(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        br.close();
        if (line == null) return ",";
        if (line.contains(";")) return ";";
        if (line.contains("\t")) return "\t";
        if (line.contains("|")) return "|";
        return ",";
    }

    /**
     * Zapisuje dane do pliku CSV.
     *
     * @param file      plik CSV do zapisu
     * @param data      dane do zapisu
     * @param delimiter separator do użycia w pliku CSV
     * @throws IOException jeśli wystąpi błąd I/O
     */
    public static void writeCSV(File file, List<String[]> data, String delimiter) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        for (String[] row : data) {
            List<String> fields = new ArrayList<>();
            for (String f : row) {
                if (f == null) f = "";
                if (f.contains(delimiter) || f.contains("\"") || f.contains("\n")) {
                    f = "\"" + f.replace("\"", "\"\"") + "\"";
                }
                fields.add(f);
            }
            bw.write(String.join(delimiter, fields));
            bw.newLine();
        }
        if (data.isEmpty()) {
            bw.write("");
        }
        bw.close();
    }
}