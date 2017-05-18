package se.kth.eagerRB;

import se.kth.GBEB.GBEBBroadcast;
import se.kth.GBEB.GBEBDeliver;
import se.sics.kompics.PortType;

/**
 * Created by Amir on 2017-05-16.
 */
public class EagerPort extends PortType {

    {
        indication(GBEBBroadcast.class);
        indication(EagerDeliver.class);
        request(EagerBroadcast.class);
        request(GBEBDeliver.class);

    }
}
