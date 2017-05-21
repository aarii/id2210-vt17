package se.kth.CRB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.GBEB.GBEBBroadcast;
import se.kth.GBEB.GBEBDeliver;
import se.kth.app.AppComp;
import se.kth.app.test.Msg;
import se.kth.eagerRB.EagerBroadcast;
import se.kth.eagerRB.EagerDeliver;
import se.kth.eagerRB.EagerPort;
import se.sics.kompics.*;
import se.sics.ktoolbox.omngr.bootstrap.BootstrapClientComp;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Amir on 2017-05-16.
 */
public class CRBComp extends ComponentDefinition {
    private static final Logger LOG = LoggerFactory.getLogger(CRBComp.class);

    Set<KompicsEvent> delivered;
    Set<KompicsEvent> past;
    KAddress selfAdr;
    Positive<EagerPort> eagerPort = requires(EagerPort.class);
    Negative<CRBPort> crbPort = provides(CRBPort.class);
    EagerBroadcast eagerBroadcast;


    public CRBComp(Init init){
        delivered = new HashSet<>();
        past = new HashSet<>();
        this.selfAdr = init.selfAdr;
        LOG.debug("selfadr " + selfAdr);

        subscribe(handleDeliver, eagerPort);
        subscribe(handleBroadcast, crbPort);

    }

      Handler<CRBBroadcast> handleBroadcast = new Handler<CRBBroadcast>() {
        @Override
        public void handle(CRBBroadcast crbBroadcast) {
            EagerBroadcast eagerBroadcast = new EagerBroadcast(crbBroadcast, past);
            trigger(eagerBroadcast, eagerPort);
            past.add(crbBroadcast);
        }
    };

     Handler<EagerDeliver> handleDeliver = new Handler<EagerDeliver>() {
       @Override
       public void handle(EagerDeliver eagerDeliver) {

            GBEBBroadcast gbebBroadcast = (GBEBBroadcast) eagerDeliver.msg;



            if(gbebBroadcast.msg instanceof EagerBroadcast) {
                eagerBroadcast = (EagerBroadcast) gbebBroadcast.msg;


                if (!delivered.contains(eagerDeliver.msg)) {


                    for (KompicsEvent m : eagerBroadcast.past) {
                        if (!delivered.contains(m)) {


                            CRBDeliver crbDeliver = new CRBDeliver(m, eagerBroadcast.address);
                            trigger(crbDeliver, crbPort);
                            delivered.add(m);

                        }
                        if (!past.contains(m)) {
                            past.add(m);
                        }

                    }
                    trigger(new CRBDeliver(eagerDeliver.msg, eagerDeliver.address), crbPort);

                    delivered.add(eagerDeliver.msg);
                    if (!past.contains(eagerDeliver.msg)) {
                        past.add(eagerDeliver.msg);
                    }
                }
            }
       }


   };


    public static class Init extends se.sics.kompics.Init<CRBComp>{

        KAddress selfAdr;
        public Init (KAddress selfAdr){

            this.selfAdr = selfAdr;
        }
    }
}
