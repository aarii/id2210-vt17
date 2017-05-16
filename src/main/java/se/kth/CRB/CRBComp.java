package se.kth.CRB;

import se.kth.GBEB.Msg;
import se.kth.app.AppComp;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.ktoolbox.util.network.KAddress;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Amir on 2017-05-16.
 */
public class CRBComp extends ComponentDefinition {

    Set<Msg> delivered;
    Set<Msg> past;
    KAddress selfAdr;

    public CRBComp(Init init){

        delivered = new HashSet<>();
        past = new HashSet<>();

        this.selfAdr = init.selfAdr;
    }


   protected final Handler<Msg> handleDeliver = new Handler<Msg>() {
       @Override
       public void handle(Msg msg) {

       }
   };







    public static class Init extends se.sics.kompics.Init<AppComp>{

        KAddress selfAdr;
        public Init (KAddress selfAdr){

            this.selfAdr = selfAdr;
        }
    }
}
