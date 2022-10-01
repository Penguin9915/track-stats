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
package hr.penguin9915.desktop.trackstats.ui.windows;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.utils.ConfigurationProvider;

public class SettingsWindow extends JDialog{
    
    private LangProvider p;

    private static record PropValue(
        JLabel name,
        JTextArea value
    ){}

    private List<PropValue> uiPropDisplay = new LinkedList<>();

    public SettingsWindow(JFrame parent, LangProvider p){
        super(parent);
        this.p = p;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initGUI();

        pack();
    }

    private void initGUI(){
        Container root = getContentPane();

        root.setLayout(new GridLayout(0, 2, 15, 15));

        Properties prop = ConfigurationProvider.getConfigurationProperties();

        for(Entry<Object, Object> entry : prop.entrySet()){
            if(entry.getKey() instanceof String key &&
                entry.getValue() instanceof String value){

                    JLabel label = new JLabel(key);
                    JTextArea area = new JTextArea(value);

                    uiPropDisplay.add(
                        new PropValue(label, area)
                    );

                    root.add(label);
                    root.add(area);
            }
        }

        JButton cancel = new JButton(p.getStringForKey("settings.button.cancel"));
        cancel.addActionListener(a -> dispose());

        root.add(cancel);

        JButton save = new JButton(p.getStringForKey("settings.button.save"));
        save.addActionListener(a -> {
            Properties toSave = new Properties();
            for(PropValue ui : uiPropDisplay){
                toSave.put(
                    ui.name.getText(), 
                    ui.value.getText()
                );
            }

            ConfigurationProvider.saveProperties(toSave);
            dispose();
        });

        root.add(save);

    }
}
