package se.kth.GBEB;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;

/**
 * Created by Amir on 2017-05-16.
 */
public class GBEBDeliver implements KompicsEvent, Serializable {

    public KAddress address;
    public KompicsEvent msg;


    public GBEBDeliver(KompicsEvent msg, KAddress address){
        this.address = address;
        this.msg = msg;
    }

}
