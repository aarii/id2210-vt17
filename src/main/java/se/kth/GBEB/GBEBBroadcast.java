package se.kth.GBEB;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;

/**
 * Created by Amir on 2017-05-17.
 */
public class GBEBBroadcast implements KompicsEvent {

    public KompicsEvent msg;
    public KAddress address;


    public GBEBBroadcast(KompicsEvent msg, KAddress kAddress){
        this.address = kAddress;
        this.msg = msg;
    }

}
