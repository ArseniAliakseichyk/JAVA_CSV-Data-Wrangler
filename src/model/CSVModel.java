package model;

import java.util.*;

/**
 * Model reprezentujący dane CSV.
 * Przechowuje dane i nagłówki pliku CSV.
 */
public class CSVModel {
    private List<String[]> data = new ArrayList<>();
    private List<String> headers = new ArrayList<>();
    private boolean hasHeader = false;

    /**
     * Ustawia dane modelu CSV.
     *
     * @param newData nowe dane do ustawienia
     */
    public void setData(List<String[]> newData) {
        data = newData;
    }

    /**
     * Zwraca kopię danych modelu.
     *
     * @return lista danych jako tablice ciągów znaków
     */
    public List<String[]> getData() {
        return new ArrayList<>(data);
    }

    /**
     * Zwraca wszystkie dane do zapisu, opcjonalnie z nagłówkami.
     *
     * @param includeHeader określa, czy dołączyć nagłówki
     * @return lista danych z opcjonalnymi nagłówkami
     */
    public List<String[]> getAllDataForSaving(boolean includeHeader) {
        List<String[]> all = new ArrayList<>();
        if (includeHeader && hasHeader && !headers.isEmpty()) {
            all.add(headers.toArray(new String[0]));
        }
        all.addAll(data);
        return all;
    }

    /**
     * Dodaje nowy wiersz do danych.
     *
     * @param row dane wiersza do dodania
     */
    public void addRow(String[] row) {
        data.add(row);
    }

    /**
     * Usuwa wiersz o podanym indeksie.
     *
     * @param index indeks wiersza do usunięcia
     */
    public void deleteRow(int index) {
        if (index >= 0 && index < data.size()) {
            data.remove(index);
        }
    }

    /**
     * Aktualizuje wartość komórki w danych.
     *
     * @param row indeks wiersza
     * @param col indeks kolumny
     * @param value nowa wartość komórki
     */
    public void updateCell(int row, int col, String value) {
        while (data.size() <= row) {
            data.add(new String[getColumnCount()]);
        }
        String[] r = data.get(row);
        if (r.length <= col) {
            r = Arrays.copyOf(r, col + 1);
            data.set(row, r);
        }
        r[col] = value;
    }

    /**
     * Dodaje nową kolumnę o podanej nazwie.
     *
     * @param name nazwa nowej kolumny
     */
    public void addColumn(String name) {
        headers.add(name);
        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            String[] newRow = Arrays.copyOf(row, row.length + 1);
            data.set(i, newRow);
        }
    }

    /**
     * Usuwa kolumnę o podanym indeksie.
     *
     * @param index indeks kolumny do usunięcia
     */
    public void removeColumn(int index) {
        if (index < 0 || index >= getColumnCount()) return;
        if (index < headers.size()) headers.remove(index);
        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            if (index < row.length) {
                String[] newRow = new String[row.length - 1];
                System.arraycopy(row, 0, newRow, 0, index);
                System.arraycopy(row, index + 1, newRow, index, row.length - index - 1);
                data.set(i, newRow);
            }
        }
    }

    /**
     * Ustawia nagłówki kolumn.
     *
     * @param h lista nagłówków
     */
    public void setHeaders(List<String> h) {
        headers = new ArrayList<>(h);
        hasHeader = !headers.isEmpty();
    }

    /**
     * Filtruje dane na podstawie podanych kryteriów.
     *
     * @param word  słowo kluczowe do filtrowania
     * @param index indeks kolumny do filtrowania
     * @param type  typ filtra (np. "Zawiera", "Rozpoczyna sie")
     * @return lista przefiltrowanych danych
     */
    public List<String[]> filter(String word, int index, String type) {
        if (index < 0 || word == null || word.isEmpty()) return new ArrayList<>(data);
        List<String[]> result = new ArrayList<>();
        for (String[] row : data) {
            if (index < row.length) {
                String val = row[index];
                boolean ok = false;
                if (type.equals("Zawiera")) ok = val.contains(word);
                else if (type.equals("Rozpoczyna sie")) ok = val.startsWith(word);
                else if (type.equals("Konczy sie")) ok = val.endsWith(word);
                else if (type.equals("Rowna sie")) ok = val.equals(word);
                if (ok) result.add(row);
            }
        }
        return result;
    }

    /**
     * Zwraca listę nagłówków kolumn.
     *
     * @return lista nagłówków
     */
    public List<String> getHeaders() {
        return new ArrayList<>(headers);
    }

    /**
     * Zwraca liczbę wierszy w danych.
     *
     * @return liczba wierszy
     */
    public int getRowCount() {
        return data.size();
    }

    /**
     * Zwraca liczbę kolumn w danych.
     *
     * @return liczba kolumn
     */
    public int getColumnCount() {
        if (!headers.isEmpty()) return headers.size();
        int max = 0;
        for (String[] row : data) {
            if (row.length > max) max = row.length;
        }
        return max;
    }

    /**
     * Sprawdza, czy dane mają nagłówki.
     *
     * @return true, jeśli dane mają nagłówki, false w przeciwnym razie
     */
    public boolean hasHeader() {
        return hasHeader;
    }
}