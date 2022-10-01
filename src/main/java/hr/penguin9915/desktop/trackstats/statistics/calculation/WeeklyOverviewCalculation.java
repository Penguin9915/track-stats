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

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import hr.penguin9915.desktop.trackstats.dbacces.implementation.JPAEMProvider;
import hr.penguin9915.desktop.trackstats.dbacces.models.Track;
import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class WeeklyOverviewCalculation {

    public static CategoryDataset calculateWeeklyStatistics(LangProvider p){

        Date monday = getMondayMorning();

        EntityManager em = JPAEMProvider.getEntityManager();
        TypedQuery<Track> trackQuery = em.createNamedQuery("Get tracks after date", Track.class);
        trackQuery.setParameter("cDate", monday);

        List<Track> tracks = trackQuery.getResultList();

        long[] activity = new long[7];

        for(Track t : tracks){
            LocalDate tDate = t.getTrackDate().toLocalDate();
            Duration active = TodayOverviewCalculations.timeOfTracksInPoints(t.getPoints());
            activity[tDate.getDayOfWeek().ordinal()] += active.toMinutes();
        }

        JPAEMProvider.close(em);

        DefaultCategoryDataset data = new DefaultCategoryDataset();
        String row = new String("activity");

        data.addValue((int)activity[DayOfWeek.MONDAY.ordinal()], row, p.getStringForKey("main.week.monday"));
        data.addValue((int)activity[DayOfWeek.TUESDAY.ordinal()], row, p.getStringForKey("main.week.tuesday"));
        data.addValue((int)activity[DayOfWeek.WEDNESDAY.ordinal()], row, p.getStringForKey("main.week.wednesday"));
        data.addValue((int)activity[DayOfWeek.THURSDAY.ordinal()], row, p.getStringForKey("main.week.thursday"));
        data.addValue((int)activity[DayOfWeek.FRIDAY.ordinal()], row, p.getStringForKey("main.week.friday"));
        data.addValue((int)activity[DayOfWeek.SATURDAY.ordinal()], row, p.getStringForKey("main.week.saturday"));
        data.addValue((int)activity[DayOfWeek.SUNDAY.ordinal()], row, p.getStringForKey("main.week.sunday"));

        return data;
    }


    private static Date getMondayMorning(){
        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(ChronoField.DAY_OF_WEEK, 1);
        return Date.valueOf(monday);
    }
}
