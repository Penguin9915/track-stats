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
package hr.penguin9915.desktop.trackstats.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JFrame;

import hr.penguin9915.desktop.trackstats.lang.ActionLocalizer;
import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.ui.windows.SettingsWindow;

public class OpenSettingsAction extends AbstractAction implements LocalizableAction{
    private static final String ACTION_PREFIX = "menu.settings";

    private JFrame parent;
    private LangProvider p;

    public OpenSettingsAction(ActionLocalizer l, JFrame parent, LangProvider p){
        this.parent = parent;
        this.p = p;
        l.setAction(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JDialog dialog = new SettingsWindow(parent, p);
        dialog.setVisible(true);
    }

    @Override
    public String getActionKeyPrefix() {
        return ACTION_PREFIX;
    }
    
}
