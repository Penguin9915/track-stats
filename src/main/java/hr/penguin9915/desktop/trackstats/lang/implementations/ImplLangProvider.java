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
package hr.penguin9915.desktop.trackstats.lang.implementations;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.utils.ConfigurationProvider;

public class ImplLangProvider implements LangProvider{
    private ResourceBundle bundle;

    public ImplLangProvider(){
        setLanguage("en");

        setLanguage(ConfigurationProvider.getConfigurationProperties().getProperty("defaults.lang"));
    }


    @Override
    public String getStringForKey(String key) {
        String value = null;
        try{
            value = bundle.getString(key);
        }catch(MissingResourceException e){
            System.err.printf("Key \"%s\" not found\n", key);
        }
        return value; 
    }

    public void setLanguage(String tag){
        try{
            bundle = ResourceBundle.getBundle("lang.language", Locale.forLanguageTag(tag));
        }catch(NullPointerException | MissingResourceException e){
            System.err.println("Couldn't set new language");
        }
    }
    
}
