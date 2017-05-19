package se.kth.CRB;

import se.kth.GBEB.GBEBDeliver;
import se.kth.app.AppComp;
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

    protected final Handler<CRBBroadcast> handleBroadcast = new Handler<CRBBroadcast>() {
        @Override
        public void handle(CRBBroadcast crbBroadcast) {

            EagerBroadcast eagerBroadcast = new EagerBroadcast(crbBroadcast.msg, past);
            trigger(eagerBroadcast, eagerPort);
            past.add(crbBroadcast.msg);
        }
    };

   protected final Handler<EagerDeliver> handleDeliver = new Handler<EagerDeliver>() {
       @Override
       public void handle(EagerDeliver eagerDeliver) {

            EagerBroadcast eagerBroadcast = (EagerBroadcast) eagerDeliver.msg;

           if(!delivered.contains(eagerDeliver.msg)){
               for(KompicsEvent m : eagerBroadcast.past){
                   if(!delivered.contains(m)){
                       CRBDeliver crbDeliver = new CRBDeliver(m, eagerBroadcast.address);
                        trigger(crbDeliver, crbPort);
                        delivered.add(m);
                   }
                   if(!past.contains(m)){
                       past.add(m);
                   }

               }
               trigger(new CRBDeliver(eagerDeliver.msg, eagerDeliver.address), crbPort);
               delivered.add(eagerDeliver.msg);
               if(!past.contains(eagerDeliver.msg)){
                   past.add(eagerDeliver.msg);
               }
           }
       }
   };


    public static class Init extends se.sics.kompics.Init<AppComp>{

        KAddress selfAdr;
        public Init (KAddress selfAdr){

            this.selfAdr = selfAdr;
        }
    }
}
