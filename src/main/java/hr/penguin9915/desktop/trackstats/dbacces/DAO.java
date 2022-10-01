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

import java.util.List;
import java.util.Optional;

import hr.penguin9915.desktop.trackstats.dbacces.models.Track;

public interface DAO{

    Optional<Track> getTrackById(Long trackId);

    boolean deleteTrack(Track track);

    boolean persistTrack(Track track);

    /**
     * Gets last n tracks.
     * @param n number of tracks to get
     * @return list of tracks, with track at index 0 being the newest.
     */
    List<Track> getLastTracks(int n);

    /**
     * Tracks are ordered from newest to oldest.
     * @param pageNum number of page
     * @param pageSize size of page
     * @return list of tracks in selected page
     */
    List<Track> getTrackPage(int pageNum, int pageSize);
}
