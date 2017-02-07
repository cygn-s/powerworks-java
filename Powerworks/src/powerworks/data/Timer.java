package powerworks.data;

import java.util.ArrayList;

public class Timer {
    static ArrayList<Timer> timers = new ArrayList<Timer>();
    int tickTime, tickOffset, minTicks, maxTicks, timer, currentTick;
    boolean playing, stopOnFinish;

    /**
     * Instantiates a Timer object, which is a handy and easily extendible way
     * for objects to keep track of cooldowns or other things
     * 
     * @param tickTime
     *            the time it takes to go up a tick
     * @param tickOffset
     *            the number of ticks it starts at (will go back to minTicks
     *            when maxTicks is reached)
     * @param minTicks
     *            how many ticks to reset to when it reaches maxTicks
     * @param maxTicks
     *            the maximum number of ticks (it will reach this and then the
     *            next time tickTime is reached it will go to minTicks)
     */
    public Timer(int tickTime, int tickOffset, int minTicks, int maxTicks) {
	if (tickTime < 0 || tickOffset < 0 || minTicks < 0 || maxTicks < 0)
	    throw new IllegalArgumentException("Arguments cannot be less than 0");
	this.tickTime = tickTime;
	this.tickOffset = tickOffset;
	this.minTicks = minTicks;
	this.maxTicks = maxTicks;
    }

    /**
     * Resets the timer that counts 60ths of a second and the current tick (keeping all other attributes)
     */
    public void resetTimes() {
	timer = 0;
	currentTick = 0;
    }
    
    /**
     * Sets timer, current tick, playing and stop on finish to default
     */
    public void resetAll() {
	timer = 0;
	currentTick = 0;
	playing = false;
	stopOnFinish = false;
    }
    
    public void play() {
	playing = true;
    }
    
    public void stop() {
	playing = false;
    }
    
    public void setStopOnFinish(boolean stopOnFinish) {
	this.stopOnFinish = stopOnFinish;
    }

    /**
     * Sets the timer that counts 60ths of a second (keeping the current frame
     * and all other attributes). If the time is above the tickTime it will
     * simply automatically go to the next tick and reset the timer
     * 
     * @param time
     *            the time, in 60ths of a second, to set it to
     */
    public void setTimer(int time) {
	if (time < 0)
	    throw new IllegalArgumentException("Invalid time, can't be less than 0");
	if (time > tickTime) {
	    timer = 0;
	    nextTick();
	}
	timer = time;
    }

    public int getCurrentTick() {
	return currentTick;
    }

    public int getMaxTicks() {
	return maxTicks;
    }

    public int getMinTicks() {
	return minTicks;
    }

    public int getTimer() {
	return timer;
    }

    /**
     * Goes to the next tick, if it is already at maxTicks and stopOnFinish is true then it goes back to
     * minTicks and stops playing, otherwise, continues playing
     */
    public void nextTick() {
	if (currentTick == maxTicks) {
	    currentTick = minTicks;
	    if(stopOnFinish) {
		playing = false;
		stopOnFinish = false;
	    }
	} else
	    currentTick++;
    }

    public void setCurrentTick(int currentTick) {
	if (currentTick > maxTicks || currentTick < minTicks)
	    throw new IllegalArgumentException("Cannot set tick number above or below max or min");
	this.currentTick = currentTick;
    }

    public static void update() {
	for (Timer t : timers) {
	    if (t.playing) {
		t.timer++;
		if (t.timer == t.tickTime)
		    t.nextTick();
	    }
	}
    }
}
