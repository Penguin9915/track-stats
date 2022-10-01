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
package hr.penguin9915.desktop.trackstats.dbacces.implementation;

import jakarta.persistence.EntityManager;

public class JPAEMProvider{
    public static EntityManager getEntityManager(){
        EntityManager em;
        em = JPAEMFProvider.getEmf().createEntityManager();
        em.getTransaction().begin();

        return em;
    }


    /**
     * Commits current transaction and closes entity manager
     * @throws RuntimeException wrapper for all exception that can be thrown in this procedure
     */
    public static void close(EntityManager em) throws RuntimeException{

        RuntimeException dex = null;

        try{
            em.getTransaction().commit();
        }catch(Exception ex){
            dex = new RuntimeException("Unable to commit transaction", ex);
        }

        try{
            em.close();
        }catch(Exception ex){
            if(dex == null){
                dex = new RuntimeException("Unable to close entity manage", ex);
            }else{
                RuntimeException old = dex;
                dex = new RuntimeException("Unable to close entitiy manager", ex);
                dex.addSuppressed(old);
            }
        }
        if(dex != null) throw dex;
    }

    


}
