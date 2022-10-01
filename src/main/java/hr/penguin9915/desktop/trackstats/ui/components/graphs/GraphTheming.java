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
package hr.penguin9915.desktop.trackstats.ui.components.graphs;

import java.awt.SystemColor;

import org.jfree.chart.JFreeChart;

public class GraphTheming {
    
    public static void changeGraphColors(JFreeChart graph){
        graph.getPlot().setBackgroundPaint(SystemColor.darkGray);
        graph.getCategoryPlot().getRenderer().setSeriesPaint(0, SystemColor.text);
        graph.setBackgroundPaint(SystemColor.darkGray);
        graph.getTitle().setPaint(SystemColor.text);

        var dAxis = graph.getCategoryPlot().getDomainAxis();
        dAxis.setLabelPaint(SystemColor.text);
        dAxis.setTickLabelPaint(SystemColor.text);
        var rAxis = graph.getCategoryPlot().getRangeAxis();
        rAxis.setLabelPaint(SystemColor.text);
        rAxis.setTickLabelPaint(SystemColor.text);

        graph.getPlot().setOutlinePaint(SystemColor.text);
    }
}
