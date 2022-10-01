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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;

import hr.penguin9915.desktop.trackstats.dbacces.implementation.JPAEMProvider;
import hr.penguin9915.desktop.trackstats.dbacces.models.Track;
import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.ui.components.main.subcomponents.TitleLabel;
import hr.penguin9915.desktop.trackstats.ui.layouts.CardLayout;
import hr.penguin9915.desktop.trackstats.ui.windows.AllExercisesListWindow;
import hr.penguin9915.desktop.trackstats.ui.windows.ExerciseDetailsWindow;
import hr.penguin9915.desktop.trackstats.utils.ConfigurationProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ExerciseList extends JComponent {

    public static class ExerciseDisplay{
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        private String name;
        private Long trackId;

        public ExerciseDisplay(Track track){
            this.name = String.format(
                "Track: %s", 
                dateFormat.format(track.getTrackDate())
            );
            this.trackId = track.getId();
        }

        @Override
        public String toString() {
            return name;
        }

        public Long getTrackId(){
            return trackId;
        }

    }

    private JFrame parent;
    private LangProvider p;

    private ListSelectionListener onExerciseClick = e -> {
        Object source = e.getSource();

        if(source instanceof JList jLst){
            source = jLst.getSelectedValue();
        }

        if(source instanceof ExerciseDisplay ed && ! e.getValueIsAdjusting()){
            System.out.println("Creating new window");
            var o = new ExerciseDetailsWindow(parent, ed.getTrackId(), p);
            o.setVisible(true);
        }
    };
    
    public ExerciseList(LangProvider p, JFrame parent){
        setPreferredSize(new Dimension(400,350));
        this.parent = parent;
        this.p = p;

        setLayout(new CardLayout());

        initGUI();
    }

    private void initGUI(){
        Integer itemCount = 5;
        try{
            Properties config = ConfigurationProvider.getConfigurationProperties();
            itemCount = Integer.parseInt(config.getProperty("exercise-list.item-count"));
        }catch(NullPointerException | NumberFormatException e){
            e.printStackTrace();
            System.err.println("Couldn't load configuration for exercise list");
        }

        EntityManager em = JPAEMProvider.getEntityManager();
        TypedQuery<Track> trackQuery = em.createNamedQuery("Get n last tracks", Track.class);
        trackQuery.setParameter("n", itemCount);

        List<Track> tracks = trackQuery.getResultList();
        ExerciseDisplay[] components = tracks.stream()
            .map(ExerciseDisplay::new)
            .toArray(size -> new ExerciseDisplay[size]);

        JPAEMProvider.close(em);

        JList<ExerciseDisplay> list = new JList<>(components);
        
        list.addListSelectionListener(onExerciseClick);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(list, BorderLayout.CENTER);

        JButton openAll = new JButton(p.getStringForKey("main.show-all"));
        openAll.addActionListener(a -> {
            var w = new AllExercisesListWindow(p);
            w.setVisible(true);
        });

        panel.add(openAll, BorderLayout.PAGE_END);

        add(new TitleLabel("main.last-exercises", p), CardLayout.ComponentPosition.TITLE);
        add(panel, CardLayout.ComponentPosition.BODY);
    }

    
}
