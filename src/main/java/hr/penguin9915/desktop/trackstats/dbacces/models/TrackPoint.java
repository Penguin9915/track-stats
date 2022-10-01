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

import java.sql.Timestamp;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Table;

/**
 * Entity representing single point in track
 */
@Entity
@Table(name = "trackPoint")
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "Todays points",
        query = "SELECT p.* FROM TrackPoint p WHERE CAST(p.tTime AS DATE) = CURRENT_DATE",
        resultClass = TrackPoint.class
    )
})
public class TrackPoint implements Comparable<TrackPoint>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tTime", unique = true, nullable = false)
    private Timestamp time;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "altitude", nullable = false)
    private Double altitude;

    @Column(name = "accuracy_horizontal")
    private Double accuracy_horizontal;

    @Column(name = "accuracy_vertical")
    private Double accuracy_vertical;

    @Column(name = "heartrate")
    private Integer heartrate;

    @ManyToOne
    @JoinColumn(name = "trackid", nullable = false)
    Track track;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Optional<Double> getAccuracy_horizontal() {
        if(accuracy_horizontal == null) return Optional.empty();
        return Optional.of(accuracy_horizontal);
    }

    public void setAccuracy_horizontal(Double accuracy_horizontal) {
        this.accuracy_horizontal = accuracy_horizontal;
    }

    public Optional<Double> getAccuracy_vertical() {
        if(accuracy_vertical == null) return Optional.empty();
        return Optional.of(accuracy_vertical);
    }

    public void setAccuracy_vertical(Double accuracy_vertical) {
        this.accuracy_vertical = accuracy_vertical;
    }

    public Optional<Integer> getHeartrate() {
        if(heartrate == null) return Optional.empty();
        return Optional.of(heartrate);
    }

    public void setHeartrate(Integer heartrate) {
        this.heartrate = heartrate;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TrackPoint tp){
            return id.equals(tp.id);
        }

        return false;
    }
    

    @Override
    public int compareTo(TrackPoint o) {
        return time.compareTo(o.time);
    }
}
