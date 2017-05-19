package se.kth.eagerRB;

import se.kth.GBEB.GBEBBroadcast;
import se.kth.GBEB.GBEBDeliver;
import se.kth.GBEB.GBEBPort;
import se.kth.app.AppComp;
import se.sics.kompics.*;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by araxi on 2017-05-16.
 */
public class EagerComp extends ComponentDefinition {

    KAddress selfAdr;
    Set<KompicsEvent> delivered;
    Negative<EagerPort> eagerPort = provides(EagerPort.class);
    Positive<GBEBPort> gbebPort = requires(GBEBPort.class);



    public EagerComp(Init init){
        selfAdr = init.selfAdr;
        delivered = new HashSet<>();


        subscribe(handleBroadcast, eagerPort);
        subscribe(handleDeliver, gbebPort);

    }


    public Handler<EagerBroadcast> handleBroadcast = new Handler<EagerBroadcast>() {
        @Override
        public void handle(EagerBroadcast eagerBroadcast) {
            GBEBBroadcast gbebBroadcast = new GBEBBroadcast(eagerBroadcast, selfAdr);
            trigger(gbebBroadcast, gbebPort);
        }
    };

    public Handler<GBEBDeliver> handleDeliver = new Handler<GBEBDeliver>() {
        @Override
        public void handle(GBEBDeliver gbebDeliver) {

            if(!delivered.contains(gbebDeliver.msg)){
                delivered.add(gbebDeliver.msg);
                EagerDeliver eagerDeliver = new EagerDeliver(gbebDeliver.msg, gbebDeliver.address);
                trigger(eagerDeliver, eagerPort);
                GBEBBroadcast gbebBroadcast = new GBEBBroadcast(gbebDeliver.msg, gbebDeliver.address);
                trigger(gbebBroadcast, gbebPort);
            }
        }
    };




    public static class Init extends se.sics.kompics.Init<EagerComp>{

        KAddress selfAdr;
        public Init (KAddress selfAdr){

            this.selfAdr = selfAdr;
        }
    }

}
