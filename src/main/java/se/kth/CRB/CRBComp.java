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
           // LOG.debug("CRBBROadcast: " + crbBroadcast);
            Set pastCpy = new HashSet();
            pastCpy.addAll(past);
            EagerBroadcast eagerBroadcast = new EagerBroadcast(crbBroadcast, pastCpy);
           // System.out.println("EagerBroadcast är " + eagerBroadcast +" med msg " + eagerBroadcast.msg);

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
              //  LOG.debug("Node " + selfAdr.getId() +"Operation: " + operation.op);
              //  LOG.debug("Node "+ selfAdr.getId()+ " Delivered innan " + delivered);
                if (!delivered.contains(eagerBroadcast)) {
                     // LOG.debug("Node " + selfAdr.getId() +"Operation1: " + operation.op);

                    for (KompicsEvent m : eagerBroadcast.past) {
                        EagerBroadcast eagerBroadcast1 = (EagerBroadcast) m;
                        CRBBroadcast crbBroadcast1 = (CRBBroadcast) eagerBroadcast1.msg;
                        Operation operation1 = (Operation) crbBroadcast1.msg;
                       // System.out.println("Node "+ selfAdr.getId()+  " M är: " + m);
                        if (!delivered.contains(m)) {
                         //  System.out.println("Node " + selfAdr.getId() +"!delivered.contains(m)");

                            CRBDeliver crbDeliver = new CRBDeliver(operation1, selfAdr);

                            trigger(crbDeliver, crbPort);
                          //  System.out.println("Har skickat triggern NU");
                            delivered.add(m);
                        }

                        if (!past.contains(m)) {
                       //     System.out.println("!past.contains(m)");
                            past.add(m);
                        }

                    }

                   // LOG.debug("Node " + selfAdr.getId() +"Delivered mellan if: " + delivered);
                    if(!delivered.contains(((EagerBroadcast) eagerDeliver.msg))) {
                       // LOG.debug("Node " + selfAdr.getId() + " i den andra ifen eagerbroadcast är är " + eagerBroadcast);

                        trigger(new CRBDeliver(operation, eagerDeliver.address), crbPort);
                        delivered.add((eagerDeliver.msg));
                        if (!past.contains((eagerDeliver.msg))) {
                            past.add((eagerDeliver.msg));
                        }
                    }
                   // LOG.debug( "Node "+ selfAdr.getId()+ "Delivered efter ifsen: " + delivered);
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
