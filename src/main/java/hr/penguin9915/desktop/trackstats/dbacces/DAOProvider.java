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
package hr.penguin9915.desktop.trackstats.dbacces;

import java.util.Objects;

public class DAOProvider{

    private static DAO dao;
    
    public static DAO getDao(){
        if(dao == null) throw new NullPointerException("DAO isn't available");
        return dao;
    }

    public static void setDao(DAO dao){
        Objects.requireNonNull(dao);
        DAOProvider.dao = dao;
    }

    
}
