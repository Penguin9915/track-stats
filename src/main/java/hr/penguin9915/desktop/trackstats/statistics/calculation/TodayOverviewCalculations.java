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
package hr.penguin9915.desktop.trackstats.statistics.calculation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import hr.penguin9915.desktop.trackstats.dbacces.models.TrackPoint;
import hr.penguin9915.desktop.trackstats.statistics.calculation.Utils.Coordinates;

public class TodayOverviewCalculations {

    private static double calculateDistance(List<TrackPoint> points, BiFunction<Coordinates, Coordinates, Double> function){
        if(points.size() < 2){
            return 0;
        }

        Iterator<TrackPoint> it = points.iterator();
        Coordinates previous = Utils.pointToCoordinate(it.next());

        double total = 0;

        while(it.hasNext()){
            Coordinates current = Utils.pointToCoordinate(it.next());
            total += function.apply(previous, current);
            previous = current;
        }

        return total;
    }

    
    
    public static double distanceBetweenPoints(List<TrackPoint> points){

        double haversine = calculateDistance(points, Utils::haversineFormulaForDistance);

        return haversine;
    }

    public static Duration timeOfTracksInPoints(List<TrackPoint> points){
        //Local date time filed should be od size 2, 
        //and first element should be start while second should be end
        HashMap<Long, LocalDateTime[]> timesPerTrack = new HashMap<>();

        for(TrackPoint p : points){
            Long trackId = p.getTrack().getId();
            LocalDateTime trackTime = p.getTime().toLocalDateTime();
            timesPerTrack.compute(trackId, (k,v) ->{
                if(v == null){
                    v = new LocalDateTime[2];
                }

                //Second expression will not be evaluated if v[0] is null 
                if(v[0] == null || v[0].isAfter(trackTime)){
                    v[0] = trackTime;
                }

                if(v[1] == null || v[1].isBefore(trackTime)){
                    v[1] = trackTime;
                }

                return v;
            });
        }

        Duration total = Duration.ZERO;

        for(LocalDateTime[] t : timesPerTrack.values()){
            total = total.plus(Duration.between(t[0], t[1]));
        }

        return total;
    }
}
