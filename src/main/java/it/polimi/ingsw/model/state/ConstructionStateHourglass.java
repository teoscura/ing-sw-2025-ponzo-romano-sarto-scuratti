package it.polimi.ingsw.model.state;

import java.time.Duration;
import java.time.Instant;

import it.polimi.ingsw.model.adventure_cards.exceptions.ForbiddenCallException;

public class ConstructionStateHourglass {
    
    private final Duration period;
    private Instant toggled = null;
    private boolean can_toggle = false;
    private int times;
    
    public ConstructionStateHourglass(int seconds, int times){
        if(seconds<=0 || times<0) throw new IllegalArgumentException();
        this.times = times;
        this.period = Duration.ofSeconds(seconds);
    }

    public boolean started(){
        return toggled!=null;
    }

    public void enable(){
        this.can_toggle = true;
    }

    public void toggle() throws ForbiddenCallException{
        if(!can_toggle||times<=0) throw new ForbiddenCallException();
        this.toggled = Instant.now();
    }

    public boolean running(){
        if(toggled==null) return true;
        return Duration.between(Instant.now(), toggled).compareTo(period) < 0;
    }

    public Instant getInstant(){
        return this.toggled;
    }

}
