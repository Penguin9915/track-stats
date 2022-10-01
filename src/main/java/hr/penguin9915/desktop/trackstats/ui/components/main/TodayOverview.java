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
package hr.penguin9915.desktop.trackstats.ui.components.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.statistics.overviews.today.TodayStats;
import hr.penguin9915.desktop.trackstats.ui.Animation;
import hr.penguin9915.desktop.trackstats.ui.components.graphs.BarGraphHorizontal;
import hr.penguin9915.desktop.trackstats.ui.components.main.subcomponents.TitleLabel;
import hr.penguin9915.desktop.trackstats.ui.components.subcomponents.LocalizedLabel;
import hr.penguin9915.desktop.trackstats.ui.layouts.CardLayout;

public class TodayOverview extends JComponent{
    private static final String DISTANCE_UNIT = "m", TIME_UNIT = "min";

    public static record TodayOverviewData(
        long distanceCurrent,
        long distanceTotal,
        long timeCurrent,
        long timeTotal
    ) {
    }

    private LangProvider p;
    private Animation animation;
    private TodayOverviewData data;

    public TodayOverview(LangProvider p){
        setPreferredSize(new Dimension(400,150));
        this.p = p;

        //mock data
        data = TodayStats.generateTodayOverviewData();

        setLayout(new CardLayout());
        initGUI();
    }

    private void initGUI(){
        add(new TitleLabel("main.today", p), CardLayout.ComponentPosition.TITLE);

        JPanel overview = new JPanel();
        overview.setLayout(new BorderLayout());

        JPanel charts = new JPanel();
        charts.setLayout(new GridLayout(2, 1, 50, 50));

        double distP = (double) data.distanceCurrent / data.distanceTotal;
        charts.add(
            new BarGraphHorizontal(
                distP > 1 ? 1 : distP, 
                animation)
        );

        double timeP = (double) data.timeCurrent / data.timeTotal;
        charts.add(
            new BarGraphHorizontal(
                timeP > 1 ? 1 : timeP, 
                animation
            )
        );

        JPanel left = new JPanel();
        left.setLayout(new GridLayout(2, 1, 50, 50));
        left.add(new LocalizedLabel("main.today.distance", p));
        left.add(new LocalizedLabel("main.today.time", p));

        overview.add(charts, BorderLayout.CENTER);
        overview.add(left, BorderLayout.LINE_START);

        JPanel right = new JPanel();
        right.setLayout(new GridLayout(2, 1, 50, 50));
        right.add(new JLabel(String.format("%d/%d %s", data.distanceCurrent, data.distanceTotal, DISTANCE_UNIT)));
        right.add(new JLabel(String.format("%d/%d %s", data.timeCurrent, data.timeTotal, TIME_UNIT)));

        overview.add(right, BorderLayout.LINE_END);
        add(overview, CardLayout.ComponentPosition.BODY);
    }
}
