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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import hr.penguin9915.desktop.trackstats.dbacces.DAO;
import hr.penguin9915.desktop.trackstats.dbacces.models.Track;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TransactionRequiredException;
import jakarta.persistence.TypedQuery;

public class JPAImplementation implements DAO {

    @Override
    public Optional<Track> getTrackById(Long trackId) {
        Objects.requireNonNull(trackId);
        EntityManager em = JPAEMProvider.getEntityManager();

        Track t = em.find(Track.class, trackId);

        JPAEMProvider.close(em);

        if(t == null) return Optional.empty();
        return Optional.of(t);
    }

    @Override
    public boolean deleteTrack(Track track) {
        try(EntityManager em = JPAEMProvider.getEntityManager()){
            em.remove(track);
        }catch(IllegalArgumentException | TransactionRequiredException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean persistTrack(Track track) {

        try(EntityManager em = JPAEMProvider.getEntityManager()){
            em.persist(track);
        }catch(IllegalArgumentException | TransactionRequiredException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Track> getLastTracks(int n) {
        List<Track> trackList = new ArrayList<>();
        try(EntityManager em = JPAEMProvider.getEntityManager()){
            TypedQuery<Track> tQuery = em.createNamedQuery("getLastNTracks", Track.class);
            tQuery.setParameter("n", n);

            trackList = tQuery.getResultList();
        }

        return trackList;
    }

    @Override
    public List<Track> getTrackPage(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("This method is not yet implemented");
        //TODO: implement
    }
}
