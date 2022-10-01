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

import java.awt.Dimension;

import javax.swing.JComponent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.statistics.calculation.WeeklyOverviewCalculation;
import hr.penguin9915.desktop.trackstats.ui.components.graphs.GraphTheming;
import hr.penguin9915.desktop.trackstats.ui.components.main.subcomponents.TitleLabel;
import hr.penguin9915.desktop.trackstats.ui.layouts.CardLayout;

public class WeeklyStatistics extends JComponent{

    private LangProvider p;
    
    public WeeklyStatistics(LangProvider p){
        this.p = p;
        setPreferredSize(new Dimension(800, 300));
        setLayout(new CardLayout());
        add(new TitleLabel("main.weekly-stats", p), CardLayout.ComponentPosition.TITLE);

        setChart();
    }

    private void setChart(){
        CategoryDataset dataset = WeeklyOverviewCalculation.calculateWeeklyStatistics(p);

        
        JFreeChart barChart = ChartFactory.createBarChart(
            p.getStringForKey("main.week.graph.title"),
            p.getStringForKey("main.week.graph.xAxis"), 
            p.getStringForKey("main.week.graph.yAxis"),
            dataset, 
            PlotOrientation.VERTICAL, 
            false, 
            false, 
            false
        );

        GraphTheming.changeGraphColors(barChart);

        ChartPanel chartPanel = new ChartPanel(barChart);        
        chartPanel.setPreferredSize(new Dimension(800, 400));
        //chartPanel.setOpaque(false);

        add(chartPanel, CardLayout.ComponentPosition.BODY);
    }
}
