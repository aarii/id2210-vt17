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
            LOG.debug("CRBBROadcast: " + crbBroadcast);
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
               // LOG.debug("EagerBroadcast: " + eagerBroadcast);
               // LOG.debug("CRBBroadcast: " + crbBroadcast);
                LOG.debug("Operation: " + operation.op);
               // LOG.debug("Delivered innan " + delivered);
                if (!delivered.contains(eagerBroadcast)) {
                    LOG.debug("Operation1: " + operation.op);

                    for (KompicsEvent m : eagerBroadcast.past) {
                      //  System.out.println("M är: " + m);
                        if (!delivered.contains(m)) {
                            CRBDeliver crbDeliver = new CRBDeliver(operation, selfAdr);

                            trigger(crbDeliver, crbPort);
                            delivered.add(m);
                        }

                        if (!past.contains(m)) {
                            past.add(m);
                        }


                    }
                 //   LOG.debug("Delivered mellan if: " + delivered);
                    if(!delivered.contains(((EagerBroadcast) eagerDeliver.msg))) {
                     //   LOG.debug("i den andra ifen past " + past);
                        trigger(new CRBDeliver(((EagerBroadcast) eagerDeliver.msg).msg, eagerDeliver.address), crbPort);
                        delivered.add((eagerDeliver.msg));
                        if (!past.contains((eagerDeliver.msg))) {
                            past.add((eagerDeliver.msg));
                        }
                    }
                 //   LOG.debug("Delivered efter ifsen: " + delivered);
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
