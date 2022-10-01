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
package hr.penguin9915.desktop.trackstats.statistics.overviews.today;

import java.util.Properties;

import hr.penguin9915.desktop.trackstats.dbacces.implementation.JPAEMProvider;
import hr.penguin9915.desktop.trackstats.dbacces.models.TrackPoint;
import hr.penguin9915.desktop.trackstats.statistics.calculation.TodayOverviewCalculations;
import hr.penguin9915.desktop.trackstats.ui.components.main.TodayOverview.TodayOverviewData;
import hr.penguin9915.desktop.trackstats.utils.ConfigurationProvider;
import hr.penguin9915.desktop.trackstats.utils.ConfigurationProvider.ConfigurationParsingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.Duration;
import java.util.List;

public class TodayStats {

    public static TodayOverviewData generateTodayOverviewData(){
        long dCurrent = 0L;
        long tCurrent = 0L;

        Properties properties = ConfigurationProvider.getConfigurationProperties();

        long dGoal, tGoal;
        try{
            dGoal = Long.parseLong(
                properties.getProperty("goal.today.distance")
            );
            tGoal = Long.parseLong(
                properties.getProperty("goal.today.time")
            );
        }catch(NumberFormatException e){
            throw new ConfigurationParsingException("Couldn't read values of goals");
        }

        EntityManager em = JPAEMProvider.getEntityManager();
        
        TypedQuery<TrackPoint> query = em.createNamedQuery("Todays points", TrackPoint.class);

        List<TrackPoint> todayPoints = query.getResultList();

        JPAEMProvider.close(em);

        dCurrent = Math.round(TodayOverviewCalculations.distanceBetweenPoints(todayPoints));
        Duration duration = TodayOverviewCalculations.timeOfTracksInPoints(todayPoints);
        tCurrent = duration.toMinutes();

        var overview = new TodayOverviewData(
            dCurrent, 
            dGoal, 
            tCurrent, 
            tGoal
        );

        return overview;
    }
}
