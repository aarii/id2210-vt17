package se.kth.GBEB;

import com.google.common.collect.Sets;
import se.kth.app.AppComp;
import se.kth.croupier.util.CroupierHelper;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.network.test.Message;
import se.sics.ktoolbox.croupier.event.CroupierSample;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Amir on 2017-05-16.
 */
public class GBEBComp extends ComponentDefinition {
    Positive<Network> networkPort = requires(Network.class);
    Negative<RBPort> rbPort = provides(RBPort.class);


    KAddress selfAdr;
    Set<Msg> past;
    public GBEBComp(Init init){
        this.selfAdr = init.selfAdr;
        past = new HashSet<>();

    }



    protected final Handler<Msg> broadcastHandler = new Handler<Msg>() {
        @Override
        public void handle(Msg msg) {
            past.add(msg);
        }
    };


   protected final Handler handleCroupierSample = new Handler<CroupierSample>() {
        @Override
        public void handle(CroupierSample croupierSample) {
            if (croupierSample.publicSample.isEmpty()) {
                return;
            }
            List<KAddress> sample = CroupierHelper.getSample(croupierSample);
            for (KAddress peer : sample) {
                KHeader header = new BasicHeader(selfAdr, peer, Transport.UDP);
                KContentMsg msg = new BasicContentMsg(header, new HistoryRequest());
                trigger(msg, networkPort);
            }
        }
    };


   protected final ClassMatchedHandler handleDeliverHistoryRequest = new ClassMatchedHandler<HistoryRequest, KContentMsg<?,?,HistoryRequest>>() {
       @Override
       public void handle(HistoryRequest content, KContentMsg<?,?, HistoryRequest> context) {

           trigger(context.answer(new HistoryResponse(past)), networkPort);

       }
   };


   protected final ClassMatchedHandler handleDeliverHistoryResponse = new ClassMatchedHandler<HistoryResponse, KContentMsg<?,?,HistoryResponse>>(){
       @Override
       public void handle(HistoryResponse content, KContentMsg<?,?,HistoryResponse> context) {

           Set<Msg> unseen = Sets.symmetricDifference(content.past, past);

           for(Msg msg : unseen){
               trigger(context.answer(new Msg(msg.address, msg.msg)), rbPort);
               past.add(msg);
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
