package se.kth.eagerRB;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * Created by Amir on 2017-05-18.
 */
public class EagerDeliver implements KompicsEvent {

    public KompicsEvent msg;
    public KAddress address;


    public EagerDeliver(KompicsEvent msg, KAddress kAddress){
        this.address = kAddress;
        this.msg = msg;
    }


}
