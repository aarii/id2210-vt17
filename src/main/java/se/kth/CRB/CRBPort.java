package se.kth.CRB;

import se.kth.eagerRB.EagerBroadcast;
import se.kth.eagerRB.EagerDeliver;
import se.sics.kompics.PortType;

/**
 * Created by Amir on 2017-05-18.
 */
public class CRBPort extends PortType {


    {
        request(EagerDeliver.class);
        request(CRBBroadcast.class);
        indication(EagerBroadcast.class);
        indication(CRBDeliver.class);

    }
}
