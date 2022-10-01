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
package hr.penguin9915.desktop.trackstats.lang;

import javax.swing.Action;

import hr.penguin9915.desktop.trackstats.ui.actions.LocalizableAction;

public class ActionLocalizer{
    private LocalizableAction action;
    private LangProvider p;

    public ActionLocalizer(LangProvider p){
        this.p = p;
    }

    public void setAction(LocalizableAction action){
        this.action = action;
        languageChanged();
    }

    
    public void languageChanged() {
        String prefix = action.getActionKeyPrefix();

        String name = p.getStringForKey(prefix.concat(".name"));
        String description = p.getStringForKey(prefix.concat(".description"));

        if(name != null){
            action.putValue(Action.NAME, name);
        }

        if(description != null){
            action.putValue(Action.SHORT_DESCRIPTION, description);
        }
    }
    
}
