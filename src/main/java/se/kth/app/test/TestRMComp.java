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
 * Created by Amir on 2017-05-23.
 */
public class TestRMComp extends ComponentDefinition {

    private static final Logger LOG = LoggerFactory.getLogger(TestComp.class);
    public int op;

    Positive<Network> networkPort = requires(Network.class);
    Positive<Timer> timerPort = requires(Timer.class);
    KAddress selfAdr;

    private String logPrefix = " ";
    public static final int appPort = 12345;


    public TestRMComp(TestRMComp.Init init) {
        selfAdr = init.selfAdr;
        op = init.op;
        logPrefix = "<nid:" + selfAdr.getId() + ">";
        //    LOG.info("{}initiating...", logPrefix);

        subscribe(handleStart, control);

    }

    Handler handleStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            //       LOG.info("{}starting...", logPrefix);
            LOG.info("op är " + op);

            //      LOG.debug("Sent msg to network to " + ScenarioSetup.getNodeAdr("193.0.0.1", 1) + " i am " + selfAdr);
            if(op == 0) {
                KHeader header1 = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.1", 1), Transport.UDP);
                KContentMsg msg1 = new BasicContentMsg(header1, new Operation("RM", 1337));
                trigger(msg1, networkPort);
            }

        }
    };




    public static class Init extends se.sics.kompics.Init<TestComp> {

        public final KAddress selfAdr;
        public final int op;


        public Init(KAddress selfAdr, int op) {
            this.selfAdr = selfAdr;
            this.op = op;

        }
    }


}
