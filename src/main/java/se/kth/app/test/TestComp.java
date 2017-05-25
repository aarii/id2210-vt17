package se.kth.app.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.sim.ScenarioSetup;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;


/**
 * Created by araxi on 2017-05-19.
 */
public class TestComp extends ComponentDefinition {
    private static final Logger LOG = LoggerFactory.getLogger(TestComp.class);
    private final int op;

    Positive<Network> networkPort = requires(Network.class);
    Positive<Timer> timerPort = requires(Timer.class);
    KAddress selfAdr;

    private String logPrefix = " ";
    public static final int appPort = 12345;


    public TestComp(Init init) {
        selfAdr = init.selfAdr;
        op = init.op;
        logPrefix = "<nid:" + selfAdr.getId() + ">";
        //    LOG.info("{}initiating...", logPrefix);

        subscribe(handleStart, control);

    }

    Handler handleStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            //Simulation 2pSet
            if(op == 0) {
                KHeader header = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg = new BasicContentMsg(header, new Operation("ADD", 1337, "2pset"));
                trigger(msg, networkPort);
            }

            if(op == 1){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD", 1338, "2pset"));
                trigger(msg1, networkPort);
            }
            //      LOG.debug("Sent msg to network to " + ScenarioSetup.getNodeAdr("193.0.0.1", 1) + " i am " + selfAdr);
            if(op == 2) {
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD", 1337, "2pset"));
                trigger(msg1, networkPort);
            }

            if(op == 3) {
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("RM", 1337, "2pset"));
                trigger(msg1, networkPort);
            }

            if(op == 4){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("RM", 1339, "2pset"));
                trigger(msg1, networkPort);
            }

            if(op == 5){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD", 1339, "2pset"));
                trigger(msg1, networkPort);
            }


            if(op == 6){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("RM", 1339, "2pset"));
                trigger(msg1, networkPort);
            }

            if(op == 7){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD", 12, "orset"));
                trigger(msg1, networkPort);
            }

            if(op == 8){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD", 13, "orset"));
                trigger(msg1, networkPort);
            }

            if(op == 9){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD", 12, "orset"));
                trigger(msg1, networkPort);
            }

            if(op == 10){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("RM", 12, "orset"));
                trigger(msg1, networkPort);
            }

            if(op == 11){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD_V", 15, "tptpgraph"));
                trigger(msg1, networkPort);
            }

            if(op == 12){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD_V", 13, "tptpgraph"));
                trigger(msg1, networkPort);
            }

            if(op == 13){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD_V", 17, "tptpgraph"));
                trigger(msg1, networkPort);
            }

            if(op == 14){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD_E", 1, 13, 15, "tptpgraph"));
                trigger(msg1, networkPort);
            }

            if(op == 15){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("ADD_E", 2, 17, 15, "tptpgraph"));
                trigger(msg1, networkPort);
            }

            if(op == 16){
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("RM_V", 15, "tptpgraph"));
                trigger(msg1, networkPort);
            }



        }
    };




    public static class Init extends se.sics.kompics.Init<TestComp> {

        public final KAddress selfAdr;
        private final int op;


        public Init(KAddress selfAdr, int op) {
            this.selfAdr = selfAdr;
            this.op = op;

        }
    }
}
