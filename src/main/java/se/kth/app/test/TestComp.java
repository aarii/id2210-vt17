package se.kth.app.test;

import javafx.geometry.Pos;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.GBEB.HistoryRequest;
import se.kth.app.AppComp;
import se.kth.app.sim.ScenarioSetup;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.omngr.bootstrap.BootstrapClientComp;
import se.sics.ktoolbox.overlaymngr.events.OMngrCroupier;
import se.sics.ktoolbox.util.identifiable.BasicBuilders;
import se.sics.ktoolbox.util.identifiable.BasicIdentifiers;
import se.sics.ktoolbox.util.identifiable.Identifier;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicAddress;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;
import se.sics.ktoolbox.util.network.nat.NatAwareAddressImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;




/**
 * Created by araxi on 2017-05-19.
 */
public class TestComp extends ComponentDefinition {
    private static final Logger LOG = LoggerFactory.getLogger(TestComp.class);

    Positive<Network> networkPort = requires(Network.class);
    Positive<Timer> timerPort = requires(Timer.class);
    KAddress selfAdr;

    private String logPrefix = " ";
    public static final int appPort = 12345;


    public TestComp(Init init) {
        selfAdr = init.selfAdr;
        logPrefix = "<nid:" + selfAdr.getId() + ">";
        LOG.info("{}initiating...", logPrefix);

        subscribe(handleStart, control);

    }

    Handler handleStart = new Handler<Start>() {
        @Override
        public void handle(Start event) {
            LOG.info("{}starting...", logPrefix);
            KHeader header = new BasicHeader(selfAdr, ScenarioSetup.getNodeAdr("193.0.0.0", 1), Transport.UDP);
            KContentMsg msg = new BasicContentMsg(header, new Msg());
            trigger(msg, networkPort);
            LOG.debug("Sent msg to network from " + selfAdr);
        }
    };




    public static class Init extends se.sics.kompics.Init<TestComp> {

        public final KAddress selfAdr;


        public Init(KAddress selfAdr) {
            this.selfAdr = selfAdr;

        }
    }
}
