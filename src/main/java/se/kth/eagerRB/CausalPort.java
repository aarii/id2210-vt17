package se.kth.eagerRB;

import se.kth.GBEB.Msg;
import se.sics.kompics.PortType;

/**
 * Created by Amir on 2017-05-16.
 */
public class CausalPort extends PortType {

    {
        indication(Msg.class);
    }
}
