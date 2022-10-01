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
package hr.penguin9915.desktop.trackstats.dbacces.models;

import java.sql.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Entity representing track.
 * Track is contained of multiple points.
 */
@Entity
@Table(name = "track")
@NamedQueries({
    @NamedQuery(
        name = "Get n last tracks",
        query = "SELECT t FROM Track t ORDER BY trackDate DESC LIMIT :n"
    ),
    @NamedQuery(
        name = "Get tracks after date",
        query = "SELECT t FROM Track t WHERE t.trackDate >= :cDate"
    ),
    @NamedQuery(
        name = "Select all tracks",
        query = "SELECT t FROM Track t"
    )
})
public class Track implements Iterable<TrackPoint>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "track")
    private List<TrackPoint> points = new LinkedList<>();

    @Column(nullable = false, name = "trackDate")
    private Date trackDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean addPoint(TrackPoint point){
        return points.add(point);
    }

    public List<TrackPoint> getPoints() {
        return points;
    }

    public void setPoints(List<TrackPoint> points) {
        this.points = points;
    }

    public Date getTrackDate() {
        return trackDate;
    }

    public void setTrackDate(Date trackDate) {
        this.trackDate = trackDate;
    }

    @Override
    public Iterator<TrackPoint> iterator() {
        points.iterator();
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Track t){
            return id.equals(t.id);
        }
        return false;
    }
    
}
