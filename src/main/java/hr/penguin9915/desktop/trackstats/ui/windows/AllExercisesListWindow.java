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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;

import hr.penguin9915.desktop.trackstats.dbacces.implementation.JPAEMProvider;
import hr.penguin9915.desktop.trackstats.dbacces.models.Track;
import hr.penguin9915.desktop.trackstats.lang.LangProvider;
import hr.penguin9915.desktop.trackstats.ui.components.main.ExerciseList.ExerciseDisplay;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class AllExercisesListWindow extends JDialog {
    
    private EntityManager em;
    private LangProvider p;
    public AllExercisesListWindow(LangProvider p){
        em = JPAEMProvider.getEntityManager();
        this.p = p;
        initGUI();
        setSize(600,400);
        addWindowListener(new WindowCloseListener());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private class WindowCloseListener extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e) {
            JPAEMProvider.close(em);
        }
    }

    private class ListData extends AbstractListModel<ExerciseDisplay>{
        private List<Track> tracks;

        public ListData(){
            TypedQuery<Track> trackQuery = em.createNamedQuery("Select all tracks", Track.class);
            tracks = trackQuery.getResultList();
        }

        @Override
        public int getSize() {
            return tracks.size();
        }

        @Override
        public ExerciseDisplay getElementAt(int index) {
            return new ExerciseDisplay(tracks.get(index));
        }

        

    }

    private ListSelectionListener onExerciseClick = e -> {
        Object source = e.getSource();

        if(source instanceof JList jLst){
            source = jLst.getSelectedValue();
        }

        if(source instanceof ExerciseDisplay ed && ! e.getValueIsAdjusting()){
            var o = new ExerciseDetailsWindow(null, ed.getTrackId(), p);
            o.setVisible(true);
        }
    };

    private void initGUI(){
        JList<ExerciseDisplay> list = new JList<>(new ListData());
        list.addListSelectionListener(onExerciseClick);

        getContentPane().add(list);
    }
}
