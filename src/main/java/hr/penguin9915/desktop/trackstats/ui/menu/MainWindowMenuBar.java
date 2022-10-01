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
package hr.penguin9915.desktop.trackstats.ui.menu;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import hr.penguin9915.desktop.trackstats.lang.ActionLocalizer;
import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.ui.actions.ImportAction;
import hr.penguin9915.desktop.trackstats.ui.actions.OpenSettingsAction;

public class MainWindowMenuBar extends JMenuBar {
    
    public MainWindowMenuBar(LangProvider p, JFrame mainFrame){
        var importLocalizer = new ActionLocalizer(p);
        Action importAction = new ImportAction(importLocalizer, mainFrame);
        var optionsMenu = new JMenu(p.getStringForKey("main.menu.options"));
        optionsMenu.add(new JMenuItem(importAction));
        var settingsLocalizer = new ActionLocalizer(p);
        Action settingsAction = new OpenSettingsAction(settingsLocalizer, mainFrame, p);
        optionsMenu.add(settingsAction);

        add(optionsMenu);
    }
}
