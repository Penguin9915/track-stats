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
import javax.swing.JMenu;

import hr.penguin9915.desktop.trackstats.lang.LangProvider;

public class LocalizedMenu extends JMenu {
    
    public LocalizedMenu(LangProvider p, String nameKey, Action action){
        super(nameKey);
        if(action != null) setAction(action);
    }
}
