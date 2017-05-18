package se.kth.CRB;

import se.sics.kompics.KompicsEvent;
import se.sics.ktoolbox.util.network.KAddress;

/**
 * Created by Amir on 2017-05-18.
 */
public class CRBDeliver implements KompicsEvent {

   public KompicsEvent msg;
   public KAddress address;


    public CRBDeliver(KompicsEvent msg, KAddress kAddress){
        this.address = kAddress;
        this.msg = msg;
    }


}
