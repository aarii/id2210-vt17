package se.kth.eagerRB;

import se.kth.GBEB.HistoryResponse;
import se.kth.GBEB.Msg;
import se.kth.GBEB.RBPort;
import se.kth.app.AppComp;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.ktoolbox.util.network.KAddress;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by araxi on 2017-05-16.
 */
public class RBComp extends ComponentDefinition {

    KAddress selfAdr;
    Set<Msg> delivered;
    Negative<RBPort> rbPort = provides(RBPort.class);
    Negative<CausalPort> causalPort = provides(CausalPort.class);



    public RBComp(Init init){
        selfAdr = init.selfAdr;
        delivered = new HashSet<>();
        subscribe(handleDeliver, rbPort);

    }

    public Handler<Msg> handleDeliver = new Handler<Msg>() {
        @Override
        public void handle(Msg msg) {

            if(!delivered.contains(msg)){
                delivered.add(msg);

                trigger(msg,causalPort);
                trigger(msg,rbPort);
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
