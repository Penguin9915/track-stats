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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import hr.penguin9915.desktop.trackstats.dbacces.models.TrackPoint;
import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.ui.windows.ExerciseDetailsWindow.ExerciseStat;

public class ExerciseDetailsCalculation {

    public static List<ExerciseStat> calculateStats(List<TrackPoint> points, LangProvider language){
        List<ExerciseStat> stats = new LinkedList<>();

        if(points.size() < 2){
            return stats;
        }

        double maxSpeed = 0, maxAcceleration = 0;
        Duration totalTime;
        LocalDateTime min, max;
        Double minAlt, maxAlt;
        Integer maxHeartrate = 0;
        
        long heartrateSum = 0;
        int heartrateCount = 0;
        
        double lastSpeed = 0;
        double speedSum = 0;

        long distance = 0L;

        Iterator<TrackPoint> it = points.iterator();
        TrackPoint last = it.next();
        min = last.getTime().toLocalDateTime();
        max = min;
        maxAlt = last.getAltitude();
        minAlt = maxAlt;
        if(last.getHeartrate().isPresent()){
            maxHeartrate = last.getHeartrate().get();
        }

        while(it.hasNext()){
            TrackPoint p = it.next();
            LocalDateTime pTime = p.getTime().toLocalDateTime();
            
            if(p.getHeartrate().isPresent()){
                Integer heartrate = p.getHeartrate().get();
                heartrateSum += heartrate;
                heartrateCount++;

                if(maxHeartrate.compareTo(heartrate) < 0){
                    maxHeartrate = heartrate;
                }
            }

            if(maxAlt.compareTo(p.getAltitude()) < 0){
                maxAlt = p.getAltitude();
            }else if(minAlt.compareTo(p.getAltitude()) > 0){
                minAlt = p.getAltitude();
            }

            if(pTime.isBefore(min)){
                min = pTime;
            }else if(pTime.isAfter(max)){
                max = pTime;
            }

            double currentSpeed = Utils.speedBetweenPoints(last, p);

            if(currentSpeed > maxSpeed){
                maxSpeed = currentSpeed;
            }

            long seconds = Duration.between(
                last.getTime().toLocalDateTime(), 
                p.getTime().toLocalDateTime()
                ).toSeconds();
            double acceleration = (currentSpeed - lastSpeed) / seconds;

            if(acceleration > maxAcceleration){
                maxAcceleration = acceleration;
            }

            distance += Utils.haversineFormulaForDistance(
                Utils.pointToCoordinate(last), 
                Utils.pointToCoordinate(p)
            );

            speedSum += currentSpeed * seconds;

            last = p;
        }

        totalTime = Duration.between(min, max);

        stats.add(
            new ExerciseStat(language.getStringForKey("trackd.stat.total-time"), totalTime)
        );
        stats.add(
            new ExerciseStat(language.getStringForKey("trackd.stat.max-speed"), maxSpeed * 3.6)
        );
        stats.add(
            new ExerciseStat(language.getStringForKey("trackd.stat.max-accel"), maxAcceleration)
        );
        stats.add(
            new ExerciseStat(language.getStringForKey("trackd.stat.avg-speed"), 3.6 * speedSum / totalTime.toSeconds())
        );
        stats.add(
            new ExerciseStat(language.getStringForKey("trackd.stat.min-alt"), minAlt)
        );
        stats.add(
            new ExerciseStat(language.getStringForKey("trackd.stat.max-alt"), maxAlt)
        );
        stats.add(
            new ExerciseStat(language.getStringForKey("trackd.stat.dist"), distance)
        );
        if(heartrateCount != 0){
            stats.add(
            new ExerciseStat(language.getStringForKey("trackd.stat.avg-hr"), heartrateSum / heartrateCount)
            );
            stats.add(
                new ExerciseStat(language.getStringForKey("trackd.stat.max-hr"), maxHeartrate)
            );
        }

        return stats;
    }

}
