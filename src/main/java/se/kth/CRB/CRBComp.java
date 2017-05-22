package se.kth.CRB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.GBEB.GBEBBroadcast;
import se.kth.app.test.Operation;
import se.kth.eagerRB.EagerBroadcast;
import se.kth.eagerRB.EagerDeliver;
import se.kth.eagerRB.EagerPort;
import se.sics.kompics.*;
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


    public CRBComp(Init init){
        delivered = new HashSet<>();
        past = new HashSet<>();
        this.selfAdr = init.selfAdr;

        subscribe(handleDeliver, eagerPort);
        subscribe(handleBroadcast, crbPort);

    }

      Handler<CRBBroadcast> handleBroadcast = new Handler<CRBBroadcast>() {
        @Override
        public void handle(CRBBroadcast crbBroadcast) {
            EagerBroadcast eagerBroadcast = new EagerBroadcast(crbBroadcast, past);
            System.out.println("EagerBroadcast är " + eagerBroadcast +" med msg " + eagerBroadcast.msg);

            trigger(eagerBroadcast, eagerPort);
            past.add(eagerBroadcast);
        }
    };

     Handler<EagerDeliver> handleDeliver = new Handler<EagerDeliver>() {
       @Override
       public void handle(EagerDeliver eagerDeliver) {

            if(eagerDeliver.msg instanceof EagerBroadcast) {

                EagerBroadcast eagerBroadcast =  (EagerBroadcast) eagerDeliver.msg;
                CRBBroadcast  crbBroadcast = (CRBBroadcast) eagerBroadcast.msg;
                Operation operation = (Operation) crbBroadcast.msg;
                    LOG.debug("Past i eagerdeliver är " + eagerBroadcast.past);
                LOG.debug("vår past " + past);

                if (!delivered.contains(eagerBroadcast)) {

                    for (KompicsEvent m : eagerBroadcast.past) {
                      //  System.out.println("M är: " + m);
                        if (!delivered.contains(m)) {
                            System.out.println("operation i crbcomp är" + operation.op);
                            CRBDeliver crbDeliver = new CRBDeliver(operation, eagerBroadcast.address);
                            trigger(crbDeliver, crbPort);
                            delivered.add(m);
                        }

                        if (!past.contains(m)) {
                            past.add(m);
                        }


                    }
                    if(!delivered.contains(((EagerBroadcast) eagerDeliver.msg).msg)) {
                        LOG.debug("i den andra ifen past " + past);
                        trigger(new CRBDeliver(((EagerBroadcast) eagerDeliver.msg).msg, eagerDeliver.address), crbPort);
                        delivered.add(((EagerBroadcast) eagerDeliver.msg).msg);
                        if (!past.contains(((EagerBroadcast) eagerDeliver.msg).msg)) {
                            past.add(((EagerBroadcast) eagerDeliver.msg).msg);
                        }
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
