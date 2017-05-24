package se.kth.app.test;

import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Amir on 2017-05-23.
 */
public class Element implements KompicsEvent, Serializable {

    public UUID uuid;
    public int value;
    public List<UUID> uuidList;

    public Element(UUID uuid, int value){
        this.uuid = uuid;
        this.value = value;
    }


}
