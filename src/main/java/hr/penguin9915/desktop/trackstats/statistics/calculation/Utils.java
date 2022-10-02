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

import hr.penguin9915.desktop.trackstats.dbacces.models.TrackPoint;

public class Utils {
    public static final double R = 6371e3;

    public static record Coordinates(
        double longitude,
        double latitude,
        double altitude
    ) {
    }

    public static record CartesianCoordinates(
        double x,
        double y,
        double z
    ){}

    /**
     * This method is modified version of javascript provided
     * on page https://www.movable-type.co.uk/scripts/latlong.html
     * Under MIT license (http://opensource.org/licenses/MIT)
     * @return distance in meters
     */
    public static double haversineFormulaForDistance(Coordinates c1, Coordinates c2){
        double phi1 = c1.latitude * Math.PI / 180; //delta, lambda in radians
        double phi2 = c2.latitude * Math.PI / 180;
        double deltaPhi = (c2.latitude - c1.latitude) * Math.PI / 180;
        double deltaLambda = (c2.longitude - c1.longitude) * Math.PI / 180;

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
            Math.cos(phi1) * Math.cos(phi2) *
            Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = R * c; // in meters
        return d;
    }

    public static double toRadian(double degrees){
        return degrees * Math.PI / 180;
    }

    public static CartesianCoordinates toCartesian(Coordinates coordinates){
        double x, y, z;

        x = (R + coordinates.altitude) * Math.sin(toRadian(coordinates.longitude)) * Math.cos(toRadian(coordinates.latitude));
        y = (R + coordinates.altitude) * (-1) * Math.cos(toRadian(coordinates.longitude)) * Math.cos(toRadian(coordinates.latitude));
        z = (R + coordinates.altitude) * Math.sin(toRadian(coordinates.latitude));

        return new CartesianCoordinates(x, y, z);
    }

    public static double euclideanDistance(Coordinates c1, Coordinates c2){
        CartesianCoordinates cc1 = toCartesian(c1);
        CartesianCoordinates cc2 = toCartesian(c2);

        return Math.sqrt(
            Math.pow((cc2.x - cc1.x), 2) +
            Math.pow((cc2.y - cc1.y), 2) +
            Math.pow((cc2.z - cc1.z), 2)
        );
    }

    public static Coordinates pointToCoordinate(TrackPoint point){
        double lat = point.getLatitude() == null ? 0 : point.getLatitude();
        double lon = point.getLongitude() == null ? 0 : point.getLongitude();
        double alt = point.getAltitude() == null ? 0 : point.getAltitude();

        return new Coordinates(lon, lat, alt);
    }

    /**
     * @return spreed in m/s
     */
    public static double speedBetweenPoints(TrackPoint first, TrackPoint second){
        double distance = haversineFormulaForDistance(
            pointToCoordinate(first), 
            pointToCoordinate(second)
        );
        Duration time = Duration.between(first.getTime().toLocalDateTime(), second.getTime().toLocalDateTime());

        return distance / time.toSeconds();
    }
}
