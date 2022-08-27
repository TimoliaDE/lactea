package de.timolia.lactea.bukkitloader.schedule;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

/**
 * @author David (_Esel)
 */
public enum TickUnit implements TemporalUnit {
    GAME(20),
    REDSTONE(10);


    private final long ticksPerSecond;
    private final long millis;
    private final Duration duration;

    TickUnit(long ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        millis = 1000 / ticksPerSecond;
        duration = Duration.ofMillis(millis);
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean isDurationEstimated() {
        return false;
    }

    @Override
    public boolean isDateBased() {
        return false;
    }

    @Override
    public boolean isTimeBased() {
        return true;
    }

    @Override
    public boolean isSupportedBy(Temporal temporal) {
        // should we check if we have an iso date? or blacklist ChronoLocalDate
        return temporal.isSupported(ChronoUnit.MILLIS);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Temporal> R addTo(R temporal, long amount) {
        long addedMillis = Math.multiplyExact(millis, amount);
        return (R) temporal.plus(addedMillis, ChronoUnit.MILLIS);
    }

    @Override
    public long between(Temporal temporal1Inclusive, Temporal temporal2Exclusive) {
        long millisBetween = temporal1Inclusive.until(temporal2Exclusive, ChronoUnit.MILLIS);
        return millisBetween / millis;
    }

    @Override
    public String toString() {
        return name();
    }

    public long ticksPerSecond() {
        return ticksPerSecond;
    }
}
