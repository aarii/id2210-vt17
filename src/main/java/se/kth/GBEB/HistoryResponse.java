package se.kth.GBEB;

import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Amir on 2017-05-16.
 */
public class HistoryResponse implements KompicsEvent, Serializable {

    Set<Msg> past;

    public HistoryResponse(Set<Msg> past){
        this.past = past;
    }

}
