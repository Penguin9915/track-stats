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
package hr.penguin9915.desktop.trackstats.ui;

import java.awt.Component;

public class Animation {
    private double percentage = 0L;
    private long 
        duration, 
        interval,
        current = 0;
    private Component c;

    public Animation(long duration, long interval, Component c, long offset){
        this.duration = duration;
        this.interval = interval;
        this.c = c;

        if(offset != 0){
            try{
                Thread.sleep(offset);
                startAnimation();
            }catch(InterruptedException e){
                e.printStackTrace();
                System.err.println("Animation interrupted. Quitting");
            }
        }
    }

    public Animation(long duration, long interval, Component c){
        this(duration, interval, c, 0);
    }

    private void startAnimation(){
        Runnable r = () -> {
            try{
                while(current < duration){
                    Thread.sleep(interval);
                    current += interval;
                    percentage = (double)current / duration;
                    c.invalidate();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
                System.err.println("Animation interrupted. Exiting");
            }

            percentage = 100;
            c.invalidate();
        };

        Thread t = new Thread(r);
        t.start();
    }

    public double getPercentage() {
        return percentage;
    }
}
