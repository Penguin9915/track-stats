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

import java.awt.Graphics;

import javax.swing.JComponent;

import hr.penguin9915.desktop.trackstats.ui.Animation;

public class BarGraphHorizontal extends JComponent {
    private Animation a;
    private double p;

    public BarGraphHorizontal(double percentage, Animation animation){
        this.a = animation;
        this.p = percentage;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        int wf;
        if(a == null) wf = (int)(w * p);
        else wf = (int)(w * p * a.getPercentage());

        g.fillRect(
            0, 
            0, 
            wf, 
            h);
    }
}
