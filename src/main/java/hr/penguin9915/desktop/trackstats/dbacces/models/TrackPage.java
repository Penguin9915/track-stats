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

import java.util.List;

public record TrackPage(
    int pageSize,
    int pageNumber,
    int totalPages,
    List<Track> tracks
) {

    public boolean isFirstPage(){
        return pageNumber == 0;
    }

    public boolean isLastPage(){
        return pageNumber == totalPages;
    }
}
