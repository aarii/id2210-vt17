package se.kth.app.test;

import se.sics.kompics.KompicsEvent;

import java.util.List;
import java.util.UUID;

/**
 * Created by Amir on 2017-05-23.
 */
public class ElementList implements KompicsEvent {

    public List<UUID> uuidList;
    public ElementList(List<UUID> uuidList){
        this.uuidList = uuidList;
    }
}
