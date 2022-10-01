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
package hr.penguin9915.desktop.trackstats.ui.windows;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import hr.penguin9915.desktop.trackstats.dbacces.implementation.JPAEMProvider;
import hr.penguin9915.desktop.trackstats.dbacces.models.Track;
import hr.penguin9915.desktop.trackstats.dbacces.models.TrackPoint;
import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.statistics.calculation.ExerciseDetailsCalculation;
import hr.penguin9915.desktop.trackstats.statistics.calculation.Utils;
import hr.penguin9915.desktop.trackstats.ui.components.graphs.GraphTheming;
import hr.penguin9915.desktop.trackstats.ui.components.main.subcomponents.TitleLabel;
import jakarta.persistence.EntityManager;

public class ExerciseDetailsWindow extends JDialog{

    private static final SimpleDateFormat titleDate = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat statDate = titleDate;

    private Long trackId;
    private EntityManager em;
    private LangProvider lang;
 
    public ExerciseDetailsWindow(JFrame parent, Long trackId, LangProvider p){
        super(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.trackId = trackId;
        this.lang = p;

        em = JPAEMProvider.getEntityManager();

        initGUI();

        JPAEMProvider.close(em);
        em = null;

        setSize(800, 600);
    }

    private void initGUI(){
        Container root = new Container();
        root.setLayout(new GridLayout(0, 1));

        JScrollPane scrollPane = new JScrollPane(root);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        Track myTrack = em.find(Track.class, trackId);
        if(myTrack == null){
            root.add(new JLabel("Somehow given track couldn't be found"));
            return;
        }

        var title = new TitleLabel(String.format("Track: %s", titleDate.format(myTrack.getTrackDate())));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        root.add(title);


        initNumericalStats(myTrack, root);


        initGraphs(myTrack, root);

        getContentPane().add(scrollPane);
    }


    public static record ExerciseStat(
        String name,
        Object value
    ) {
    }

    private void initNumericalStats(Track myTrack, Container root){
        Container grid = new Container();
        grid.setLayout(new GridLayout(0, 3, 15, 5));

        List<ExerciseStat> stats = ExerciseDetailsCalculation.calculateStats(myTrack.getPoints(), lang);

        for(ExerciseStat s : stats){
            String name = s.name;
            String value = null;

            if(s.value instanceof Date d){
                value = statDate.format(d);
            }else if(s.value instanceof Duration d){
                value = String.format("%d h %d min", d.toHours(), d.toMinutes() % 60);
            }else if(s.value instanceof Double || s.value instanceof Float){
                Number num = (Number) s.value;
                value = String.format("%.4f", num);
            }else{
                value = s.value.toString();
            }

            grid.add(
                new JLabel(String.format("%s : %s", name, value), SwingConstants.CENTER)
            );
        }

        root.add(grid);
    }

    private static Dimension graphPreferred = new Dimension(800, 400);

    private void addOneGraph(DefaultCategoryDataset dataset, String nameBase, Container root){
        JFreeChart chart = ChartFactory.createLineChart(
            lang.getStringForKey(String.format("%s.title", nameBase)), 
            lang.getStringForKey(String.format("%s.xDesc", nameBase)), 
            lang.getStringForKey(String.format("%s.yDesc", nameBase)), 
            dataset,
            PlotOrientation.VERTICAL,
            false,
            false,
            false
        );
        GraphTheming.changeGraphColors(chart);
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(graphPreferred);
        root.add(panel);
    }

    private void initGraphs(Track myTrack, Container root){
        MultipleDatasets datasets = speedDataset(myTrack.getPoints());
        

        addOneGraph(datasets.speed, "trackd.graph.speed", root);

        addOneGraph(datasets.acceleration, "trackd.graph.accel", root);
        
        addOneGraph(datasets.altitude, "trackd.graph.alt", root);

        addOneGraph(datasets.heartrate, "trackd.graph.hr", root);
    }

    private static record MultipleDatasets(
        DefaultCategoryDataset speed,
        DefaultCategoryDataset heartrate,
        DefaultCategoryDataset altitude,
        DefaultCategoryDataset acceleration
    ){}

    private static MultipleDatasets speedDataset(List<TrackPoint> points){
        MultipleDatasets datasets = new MultipleDatasets(
            new DefaultCategoryDataset(), 
            new DefaultCategoryDataset(),
            new DefaultCategoryDataset(), 
            new DefaultCategoryDataset()
        );

        if(points.size() < 2){
            return datasets;
        }

        Iterator<TrackPoint> it = points.iterator();
        TrackPoint last = it.next();
        LocalDateTime firstTime = last.getTime().toLocalDateTime();
        double lastSpeed = 0;
        if(last.getHeartrate().isPresent()){
            Integer h = last.getHeartrate().get();
            datasets.heartrate.addValue(
                h, 
                "Heartrate", 
                last.getTime().toInstant()
            );
        }
        if(last.getAltitude() != null){
            Double a = last.getAltitude();
            datasets.altitude.addValue(
                a, 
                "Altitude", 
                last.getTime().toInstant()
            );
        }
        

        while(it.hasNext()){
            TrackPoint p = it.next();
            Long pointTime = Duration.between(firstTime, p.getTime().toLocalDateTime()).toMinutes();

            if(p.getHeartrate().isPresent()){
                Integer h = p.getHeartrate().get();
                datasets.heartrate.addValue(
                    h, 
                    "Heartrate", 
                    pointTime
                );
            }
            if(p.getAltitude() != null){
                Double a = p.getAltitude();
                datasets.altitude.addValue(
                    a, 
                    "Altitude", 
                    pointTime
                );
            }
            
            double currentSpeed = Utils.speedBetweenPoints(last, p);
            
            datasets.speed.addValue(
                currentSpeed * 3.6, 
                "Speed", 
                pointTime
            );

            long seconds = Duration.between(
                last.getTime().toLocalDateTime(), 
                p.getTime().toLocalDateTime()
                ).toSeconds();

            datasets.acceleration.addValue(
                (currentSpeed - lastSpeed)/seconds , 
                "Acceleration", 
                pointTime
            );

            last = p;
            lastSpeed = currentSpeed;
        }

        return datasets;
    }
}
