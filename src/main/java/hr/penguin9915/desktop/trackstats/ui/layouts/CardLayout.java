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
package hr.penguin9915.desktop.trackstats.ui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.Objects;
import java.util.function.Function;

public class CardLayout implements LayoutManager2 {

    private Component[] components = new Component[2];

    public enum ComponentPosition{TITLE, BODY};

    @Override
    public void addLayoutComponent(String name, Component comp) {
        if(name.equals("title")){
            addLayoutComponent(comp, ComponentPosition.TITLE);
        }else if(name.equals("body")){
            addLayoutComponent(comp, ComponentPosition.BODY);
        }else{
            throw new UnsupportedOperationException("Unknown position: " + name);
        }
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        Objects.requireNonNull(comp);
        for(int i = 0; i < components.length; i++){
            if(comp.equals(components[i])){
                components[i] = null;
                break;
            }
        }
        
    }

    private Dimension calculateSize(Container parent, Function<Component, Dimension> function){
        Insets i = parent.getInsets();
        int width = 0;
        int height = 0;
        for(Component c : components){
            Dimension d = function.apply(c);
            //Width calculation
            if(width > d.width){
                width = d.width;
            }

            //Height calculation
            height += d.height;
        }

        width += i.left + i.right;
        height += i.bottom + i.top;
        return new Dimension(width, height);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return calculateSize(parent, c -> c.getPreferredSize());
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return calculateSize(parent, c -> c.getMinimumSize());
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return calculateSize(target, c -> c.getMaximumSize());
    }

    @Override
    public void layoutContainer(Container parent) {
        int tHeight = (int)(parent.getHeight() * 0.25);

        //Title
        var c = components[0];
        if(c != null) c.setBounds(
                0, 
                0,
                parent.getWidth(), 
                tHeight);

        //Body
        c = components[1];
        if(c != null) c.setBounds(
                0,
                tHeight,
                parent.getWidth(),
                parent.getHeight() - tHeight);
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if(constraints instanceof ComponentPosition pos){
            switch(pos){
                case TITLE -> components[0] = comp;
                case BODY -> components[1] = comp;
            }
        }else{
            throw new IllegalArgumentException("Object constraints should be instance of ComponentPosition");
        }
        
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) { 
    }
    
}
