package se.kth.eagerRB;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.Set;

/**
 * Created by Amir on 2017-05-18.
 */
public class EagerBroadcast implements KompicsEvent {

   public KompicsEvent msg;
   public KAddress address;
   public Set<KompicsEvent> past;


    public EagerBroadcast(KompicsEvent msg, Set<KompicsEvent> past){
        this.msg = msg;
        this.past = past;

    }


}
