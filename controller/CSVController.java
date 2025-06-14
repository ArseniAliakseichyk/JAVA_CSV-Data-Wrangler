package controller;

import model.CSVModel;
import util.CSVUtils;
import view.MainFrame;
import view.TablePanel;
import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * Kontroler obsługujący operacje na plikach CSV.
 * Współpracuje z modelem i widokiem w celu zarządzania danymi CSV.
 */
public class CSVController {
    private CSVModel model;
    private TablePanel tablePanel;
    private MainFrame mainFrame;

    /**
     * Konstruktor domyślny. Inicjalizuje model CSV.
     */
    public CSVController() {
        model = new CSVModel();
    }

    /**
     * Ustawia panel tabeli dla kontrolera.
     *
     * @param panel panel tabeli do ustawienia
     */
    public void setTablePanel(TablePanel panel) {
        tablePanel = panel;
    }

    /**
     * Ustawia główną ramkę aplikacji dla kontrolera.
     *
     * @param frame główna ramka do ustawienia
     */
    public void setMainFrame(MainFrame frame) {
        mainFrame = frame;
    }

    /**
     * Tworzy nowy plik CSV z określoną liczbą kolumn.
     *
     * @param columns liczba kolumn dla nowego pliku CSV
     */
    public void createNewFile(int columns) {
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            headers.add("Kolumna " + (i + 1));
        }
        model.setHeaders(headers);
        model.setData(new ArrayList<>());
        tablePanel.updateTable(model.getData(), model.getHeaders());
        updateStatus();
    }

    /**
     * Dodaje nową kolumnę o podanej nazwie.
     *
     * @param name nazwa nowej kolumny
     */
    public void addColumn(String name) {
        model.addColumn(name);
        tablePanel.updateTable(model.getData(), model.getHeaders());
        updateStatus();
    }

    /**
     * Usuwa kolumnę o podanym indeksie.
     *
     * @param index indeks kolumny do usunięcia
     */
    public void removeColumn(int index) {
        model.removeColumn(index);
        tablePanel.updateTable(model.getData(), model.getHeaders());
        updateStatus();
    }

    /**
     * Wczytuje dane z pliku CSV.
     *
     * @param file      plik CSV do wczytania
     * @param delimiter separator użyty w pliku CSV
     * @param hasHeader określa, czy plik zawiera nagłówki
     */
    public void loadCSV(File file, String delimiter, boolean hasHeader) {
        try {
            if (file.length() == 0) throw new Exception("Plik jest pusty");
            List<String[]> data = CSVUtils.readCSV(file, delimiter);
            if (data.isEmpty()) throw new Exception("Brak danych w CSV");
            if (hasHeader) model.setHeaders(Arrays.asList(data.remove(0)));
            else model.setHeaders(new ArrayList<>());
            model.setData(data);
            tablePanel.updateTable(model.getData(), model.getHeaders());
            updateStatus();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Blad ladowania CSV: " + e.getMessage());
        }
    }

    /**
     * Zapisuje dane do pliku CSV.
     *
     * @param file         plik CSV do zapisu
     * @param delimiter    separator do użycia w pliku CSV
     * @param includeHeader określa, czy zapisać nagłówki
     */
    public void saveCSV(File file, String delimiter, boolean includeHeader) {
        try {
            CSVUtils.writeCSV(file, model.getAllDataForSaving(includeHeader), delimiter);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Blad zapisu CSV: " + e.getMessage());
        }
    }

    /**
     * Filtruje dane w tabeli na podstawie podanych kryteriów.
     *
     * @param keyword słowo kluczowe do filtrowania
     * @param index   indeks kolumny do filtrowania
     * @param type    typ filtra (np. "Zawiera", "Rozpoczyna sie")
     */
    public void filterTable(String keyword, int index, String type) {
        List<String[]> filtered = model.filter(keyword, index, type);
        tablePanel.updateTable(filtered, model.getHeaders());
        updateStatus();
    }

    /**
     * Dodaje nowy wiersz do danych.
     *
     * @param row dane wiersza do dodania
     */
    public void addRow(String[] row) {
        model.addRow(row);
        tablePanel.updateTable(model.getData(), model.getHeaders());
        updateStatus();
    }

    /**
     * Usuwa wiersz o podanym indeksie.
     *
     * @param index indeks wiersza do usunięcia
     */
    public void deleteRow(int index) {
        model.deleteRow(index);
        tablePanel.updateTable(model.getData(), model.getHeaders());
        updateStatus();
    }

    /**
     * Aktualizuje nagłówki kolumn.
     *
     * @param headers nowe nagłówki kolumn
     */
    public void updateHeaders(List<String> headers) {
        model.setHeaders(headers);
        tablePanel.updateTable(model.getData(), model.getHeaders());
        updateStatus();
    }

    /**
     * Aktualizuje wartość komórki w danych.
     *
     * @param row indeks wiersza
     * @param col indeks kolumny
     * @param val nowa wartość komórki
     */
    public void updateCell(int row, int col, String val) {
        try {
            model.updateCell(row, col, val);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Błąd aktualizacji komórki: " + e.getMessage());
        }
    }

    /**
     * Zwraca listę nagłówków kolumn.
     *
     * @return lista nagłówków
     */
    public List<String> getHeaders() {
        return model.getHeaders();
    }

    /**
     * Zwraca liczbę kolumn w danych.
     *
     * @return liczba kolumn
     */
    public int getColumnCount() {
        return model.getColumnCount();
    }

    /**
     * Zwraca liczbę wierszy w danych.
     *
     * @return liczba wierszy
     */
    public int getRowCount() {
        return model.getRowCount();
    }

    /**
     * Aktualizuje status aplikacji, np. liczbę rekordów.
     */
    private void updateStatus() {
        if (mainFrame != null) {
            SwingUtilities.invokeLater(() -> mainFrame.updateStatus());
        }
    }
}