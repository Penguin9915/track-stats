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
package hr.penguin9915.desktop.trackstats;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import hr.penguin9915.desktop.trackstats.dbacces.implementation.JPAEMFProvider;
import hr.penguin9915.desktop.trackstats.lang.implementations.ImplLangProvider;
import hr.penguin9915.desktop.trackstats.ui.components.main.ExerciseList;
import hr.penguin9915.desktop.trackstats.ui.components.main.TodayOverview;
import hr.penguin9915.desktop.trackstats.ui.components.main.WeeklyStatistics;
import hr.penguin9915.desktop.trackstats.ui.menu.MainWindowMenuBar;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MainWindow extends JFrame {

    private class MainWindowListener extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e) {
            EntityManagerFactory emf = JPAEMFProvider.getEmf();
            JPAEMFProvider.setEmf(null);
            emf.close();
        }
    }
    
    public MainWindow(){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Track stats");
        setSize(900,600);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("track.database");
        JPAEMFProvider.setEmf(emf);

        addWindowListener(new MainWindowListener());
        
        initGUI();
    }

    private void initGUI(){
        System.out.println("Initializing GUI");
        ImplLangProvider p = new ImplLangProvider();


        Container rootContainer = new Container();
        rootContainer.setLayout(new GridLayout(0, 1, 5, 5));

        var menuBar = new MainWindowMenuBar(p, this);
        setJMenuBar(menuBar);

        var todayOverview = new TodayOverview(p);
        rootContainer.add(todayOverview);

        var exerciseList = new ExerciseList(p, this);
        rootContainer.add(exerciseList);

        var weeklyStatistics = new WeeklyStatistics(p);
        rootContainer.add(weeklyStatistics);

        var scrollPane = new JScrollPane(rootContainer);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        getContentPane().add(scrollPane);
    }
}
