/* This is part of Track-stats

    Copyright (C) 2022  Penguin9915

    Track-stats is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package hr.penguin9915.desktop.trackstats.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Class used for loading data from csv file.
 */
public class CSVReader implements DataLoader{

    private BufferedReader br;
    private List<String> names;
    private String[] currentLine;

    public CSVReader(BufferedReader br){
        this.br = br;

        try{
            String line = br.readLine();
            if(line != null && line.startsWith("#")){
                line = line.substring(1); //Removing #
                names = Arrays.asList(line.split(","));
            }
        }catch(IOException e){
            var ex = new IllegalStateException("Could not read file");
            ex.addSuppressed(e);
            throw ex;
        }

        if(!nextRow()){
            throw new IllegalArgumentException("File doesn't contain any lines");
        }
    }

    @Override
    public List<String> getFieldNames() {
        return names;
    }

    @Override
    public void setFieldNames(List<String> names) {
        this.names = names;
    }

    private <T> T callByIndex(String key, Function<Integer, T> delegate){
        if(names == null){
            throw new IllegalStateException("Column names not found at beginning of file");
        }

        Objects.requireNonNull(key);
        Objects.requireNonNull(delegate);

        int index = names.indexOf(key);
        if(index == -1){
            throw new IllegalArgumentException("Column with given name not found");
        }

        return delegate.apply(index);
    }

    @Override
    public Double getNumericalByKey(String key) {
        return callByIndex(key, this::getNumericalByIndex);
    }

    private String sanitizeData(int index){
        String data = currentLine[index];
        if(data.startsWith("\"")){
            data = data.substring(1, data.length() - 1);
        }
        return data;
    }

    @Override
    public Double getNumericalByIndex(int index) {
        String data = sanitizeData(index);

        Double result = null;
        try{
            result = Double.parseDouble(data);
        }catch(NumberFormatException e){
            System.err.printf("Number format exception. Couldn't read number %s\n", data);
        }

        return result;
    }

    @Override
    public String getStringByKey(String key) {
        return callByIndex(key, this::getStringByIndex);
    }

    @Override
    public String getStringByIndex(int index) {
        return sanitizeData(index);
    }

    @Override
    public Instant getDateByKey(String key) {
        return callByIndex(key, this::getDateByIndex);
    }

    @Override
    public Instant getDateByIndex(int index) {
        String data = sanitizeData(index);
        Instant instant = null;
        try{
            instant = Instant.parse(data);
        }catch(DateTimeParseException e){
            System.err.printf("Couldn't parse date %s\n",data);
        }
        return instant;
    }

    @Override
    public boolean nextRow() {
        String line = null;
        try{
            while((line = br.readLine()) != null){
                if(line.startsWith("#")){
                    continue;
                }

                currentLine = line.split(",");
                if(currentLine[1].equals("\"TRACKPOINT\"")){
                    break;
                }
            }
        }catch(IOException e){
            var ex = new IllegalStateException("Can't read file anymore");
            ex.addSuppressed(e);
            throw ex;
        }
        

        return line != null; //False if end of file is reached
    }

    
    
}
