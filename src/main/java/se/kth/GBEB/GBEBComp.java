package se.kth.GBEB;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.CRB.CRBBroadcast;
import se.kth.app.AppComp;
import se.kth.app.test.Msg;
import se.kth.app.test.TestComp;
import se.kth.croupier.util.CroupierHelper;
import se.kth.eagerRB.EagerBroadcast;
import se.kth.eagerRB.EagerDeliver;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.ktoolbox.croupier.CroupierPort;
import se.sics.ktoolbox.croupier.event.CroupierSample;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Amir on 2017-05-16.
 */
public class GBEBComp extends ComponentDefinition {
    private static final Logger LOG = LoggerFactory.getLogger(GBEBComp.class);

    Positive<Network> networkPort = requires(Network.class);
    Negative<GBEBPort> gbebPort = provides(GBEBPort.class);
    Positive<CroupierPort> croupierPort = requires(CroupierPort.class);



    List<KompicsEvent> eventlist;
    KAddress selfAdr;
     Set<KompicsEvent> past;
    public GBEBComp(Init init){
        this.selfAdr = init.selfAdr;
        past = new HashSet<>();
        eventlist = new ArrayList<>();

        subscribe(broadcastHandler, gbebPort);
        subscribe(handleCroupierSample, croupierPort);
        subscribe(handleDeliverHistoryRequest, networkPort);
        subscribe(handleDeliverHistoryResponse, networkPort);

    }

    public final Handler<GBEBBroadcast> broadcastHandler = new Handler<GBEBBroadcast>() {
        @Override
        public void handle(GBEBBroadcast gbebBroadcast) {
            past.add(gbebBroadcast);
           // LOG.debug("added to my past in GBEBBroadcast");

        }
    };


    public final Handler handleCroupierSample = new Handler<CroupierSample>() {
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
              //  LOG.debug("Received CroupierSample" + sample);
            }
        }
    };


    public final ClassMatchedHandler handleDeliverHistoryRequest = new ClassMatchedHandler<HistoryRequest, KContentMsg<?,?,HistoryRequest>>() {
       @Override
       public void handle(HistoryRequest content, KContentMsg<?,?, HistoryRequest> context) {

           trigger(context.answer(new HistoryResponse(past)), networkPort);

       }
   };


    public final ClassMatchedHandler handleDeliverHistoryResponse = new ClassMatchedHandler<HistoryResponse, KContentMsg<?,?,HistoryResponse>>(){
       @Override
       public void handle(HistoryResponse content, KContentMsg<?,?,HistoryResponse> context) {

           Set<KompicsEvent> unseen = Sets.symmetricDifference(content.past, past);
           List<KompicsEvent> unseenList = new ArrayList<>(unseen);

           for(KompicsEvent ke : unseenList){
               GBEBDeliver gbebDeliver = new GBEBDeliver(ke, context.getHeader().getSource());
               trigger(gbebDeliver, gbebPort);
               past.add(ke);
              //s eventlist.add(ke);
           }



       }
   };



    public static class Init extends se.sics.kompics.Init<GBEBComp>{

        KAddress selfAdr;
        public Init (KAddress selfAdr){

            this.selfAdr = selfAdr;
        }
    }
}
