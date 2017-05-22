package se.kth.eagerRB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.GBEB.GBEBBroadcast;
import se.kth.GBEB.GBEBDeliver;
import se.kth.GBEB.GBEBPort;
import se.kth.app.AppComp;
import se.kth.app.test.TestComp;
import se.sics.kompics.*;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by araxi on 2017-05-16.
 */
public class EagerComp extends ComponentDefinition {
    private static final Logger LOG = LoggerFactory.getLogger(EagerComp.class);

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
            System.out.println("GBEBBroadcast är " + gbebBroadcast +" med msg " + gbebBroadcast.msg);

            trigger(gbebBroadcast, gbebPort);
        }
    };

    public Handler<GBEBDeliver> handleDeliver = new Handler<GBEBDeliver>() {
        @Override
        public void handle(GBEBDeliver gbebDeliver) {


            EagerBroadcast eagerBroadcast = (EagerBroadcast) gbebDeliver.msg;
            LOG.debug("mypast in eagercomp ÄR " + eagerBroadcast.past);

            if(!delivered.contains(eagerBroadcast)){
                delivered.add(eagerBroadcast);

                EagerDeliver eagerDeliver = new EagerDeliver(eagerBroadcast, gbebDeliver.address);

                trigger(eagerDeliver, eagerPort);
                GBEBBroadcast gbebBroadcast = new GBEBBroadcast(eagerBroadcast, gbebDeliver.address);

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
