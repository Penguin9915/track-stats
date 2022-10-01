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
package hr.penguin9915.desktop.trackstats.ui.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.penguin9915.desktop.trackstats.dbacces.implementation.JPAEMProvider;
import hr.penguin9915.desktop.trackstats.dbacces.models.Track;
import hr.penguin9915.desktop.trackstats.dbacces.models.TrackPoint;
import hr.penguin9915.desktop.trackstats.lang.ActionLocalizer;
import hr.penguin9915.desktop.trackstats.utils.CSVReader;
import jakarta.persistence.EntityManager;

public class ImportAction extends AbstractAction implements LocalizableAction {
    private static final String ACTION_PREFIX = "menu.import";
    private static final FileNameExtensionFilter CSV_FILE = new FileNameExtensionFilter("CSV data", "csv");
    private static final String[] defaultNames = "time,trackpoint_type,latitude,longitude,altitude,accuracy_horizontal,accuracy_vertical,speed,altitude_gain,altitude_loss,sensor_distance,heartrate,cadence,power".split(",");

    private JFrame parent;

    public ImportAction(ActionLocalizer l, JFrame parent){
        l.setAction(this);
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Import action launched");
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileFilter(CSV_FILE);
        int result = jFileChooser.showOpenDialog(parent);

        if(result == JFileChooser.APPROVE_OPTION){
            File data = jFileChooser.getSelectedFile();

            importFile(data);
        }
        
    }

    @Override
    public String getActionKeyPrefix() {
        return ACTION_PREFIX;
    }

    private void importFile(File data){
        Track track = new Track();
        try(BufferedReader br = Files.newBufferedReader(data.toPath(), StandardCharsets.UTF_8)){
            CSVReader reader = new CSVReader(br);

            List<String> nameList = reader.getFieldNames();
            if(nameList == null){
                System.err.println("Setting default name list");
                reader.setFieldNames(Arrays.asList(defaultNames));
            }

            do{
                String type = reader.getStringByKey("trackpoint_type");
                if(! type.equals("TRACKPOINT")) continue;

                TrackPoint point = new TrackPoint();
                point.setTime(
                    Timestamp.from(reader.getDateByKey("time"))
                );
                point.setLatitude(
                    reader.getNumericalByKey("latitude")
                );
                point.setLongitude(
                    reader.getNumericalByKey("longitude")
                );
                point.setAltitude(
                    reader.getNumericalByKey("altitude")
                );
                point.setAccuracy_horizontal(
                    reader.getNumericalByKey("accuracy_horizontal")
                );
                point.setAccuracy_vertical(
                    reader.getNumericalByKey("accuracy_vertical")
                );

                if(track.getTrackDate() == null){
                    track.setTrackDate(
                        Date.valueOf(point.getTime().toLocalDateTime().toLocalDate())
                    );
                }

                track.addPoint(point);
                point.setTrack(track);
            }while(reader.nextRow());


            EntityManager em = JPAEMProvider.getEntityManager();
            em.persist(track);
            JPAEMProvider.close(em);

        }catch(IOException e){
            JOptionPane.showMessageDialog(
                null, 
                "Exception happened while reading file"
            );

            e.printStackTrace();
        }
    }
    
}
