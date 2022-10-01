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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

/**
 * Class used for loading and saving settings
 */
public class ConfigurationProvider {

    private static Properties properties = null;

    public static Properties getConfigurationProperties(){
        if(properties == null){
            loadProperties();
        }

        return properties;
    }

    private static void loadProperties(){
        properties = new Properties();
        try(InputStream is = ConfigurationProvider.class.getResourceAsStream("configuration.properties")){
            properties.load(is);
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("Unable to load configuration");
            throw new ConfigurationLoadingException();
        }
    }

    private static class ConfigurationLoadingException extends IllegalStateException{
        public ConfigurationLoadingException(){
            super("Couldn't find configuration.properties");
        }
    }

    public static class ConfigurationParsingException extends IllegalArgumentException{
        public ConfigurationParsingException(String message){
            super(message);
        }
    }

    public static void saveProperties(Properties p){
        Objects.requireNonNull(p);

        var config = ConfigurationProvider.class.getResource("configuration.properties");
        OutputStream os = null;
        try{
            File file = new File(config.toURI());
            os = Files.newOutputStream(file.toPath());

            p.store(os, null);
            properties = p;

            System.out.println("Properties written");
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("IO exception happened");
        }catch(URISyntaxException e){
            e.printStackTrace();
            System.err.println("URI syntax exception happened");
        }finally{
            try{
                os.close();
            }catch(IOException e){}
        }
    }
    
}
