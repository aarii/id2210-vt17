package se.kth.CRB;

import se.kth.app.test.Msg;
import se.kth.eagerRB.EagerBroadcast;
import se.kth.eagerRB.EagerDeliver;
import se.sics.kompics.KompicsEvent;
import se.sics.kompics.PortType;

/**
 * Created by Amir on 2017-05-18.
 */
public class CRBPort extends PortType {


    {

        request(CRBBroadcast.class);
        request(EagerDeliver.class);
        indication(CRBDeliver.class);
        indication(EagerBroadcast.class);



    }
}
