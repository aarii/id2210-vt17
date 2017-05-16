package se.kth.GBEB;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;

/**
 * Created by Amir on 2017-05-16.
 */
public class Msg implements KompicsEvent, Serializable {

    KAddress address;
    String msg;

    public Msg(KAddress address, String msg){
        this.address = address;
        this.msg = msg;
    }

}
