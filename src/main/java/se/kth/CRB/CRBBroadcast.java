package se.kth.CRB;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.List;

/**
 * Created by Amir on 2017-05-17.
 */
public class CRBBroadcast implements KompicsEvent {

    public KompicsEvent msg;
    public KAddress address;


    public CRBBroadcast(KompicsEvent msg, KAddress kAddress){
        this.address = kAddress;
        this.msg = msg;
    }


}
