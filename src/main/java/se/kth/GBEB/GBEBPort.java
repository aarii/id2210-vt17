package se.kth.GBEB;

import se.kth.eagerRB.EagerDeliver;
import se.sics.kompics.PortType;
import se.sics.ktoolbox.croupier.event.CroupierSample;

/**
 * Created by Amir on 2017-05-16.
 */
public class GBEBPort extends PortType {


    {
        indication(GBEBDeliver.class);
        request(GBEBBroadcast.class);
    }
}
