/*
 * 2016 Royal Institute of Technology (KTH)
 *
 * LSelector is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.kth.app;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.CRB.CRBBroadcast;
import se.kth.CRB.CRBDeliver;
import se.kth.CRB.CRBPort;
import se.kth.app.sim.ScenarioSetup;
import se.kth.app.test.Msg;
import se.kth.croupier.util.CroupierHelper;
import se.kth.app.test.Ping;
import se.kth.app.test.Pong;
import se.kth.eagerRB.EagerBroadcast;
import se.kth.eagerRB.EagerPort;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.network.Transport;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.croupier.CroupierPort;
import se.sics.ktoolbox.croupier.event.CroupierSample;
import se.sics.ktoolbox.omngr.bootstrap.BootstrapClientComp;
import se.sics.ktoolbox.util.identifiable.Identifier;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import se.sics.ktoolbox.util.network.KHeader;
import se.sics.ktoolbox.util.network.basic.BasicContentMsg;
import se.sics.ktoolbox.util.network.basic.BasicHeader;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class AppComp extends ComponentDefinition {

  private static final Logger LOG = LoggerFactory.getLogger(AppComp.class);
  private String logPrefix = " ";

  //*******************************CONNECTIONS********************************

  Positive<Timer> timer = requires(Timer.class);
  Positive<Network> networkPort = requires(Network.class);
  Positive<CRBPort> crbPort = requires(CRBPort.class);
  //**************************************************************************
  private KAddress selfAdr;

  public AppComp(Init init) {
    selfAdr = init.selfAdr;
    logPrefix = "<nid:" + selfAdr.getId() + ">";
    LOG.info("{}initiating...", logPrefix);

    subscribe(handleStart, control);
    subscribe(handleMsg, networkPort);
    subscribe(handleCRBDeliver, crbPort);
  }

  Handler handleStart = new Handler<Start>() {
    @Override
    public void handle(Start event) {
      LOG.info("{}starting...", logPrefix);
    }
  };


  ClassMatchedHandler handleMsg = new ClassMatchedHandler<Msg, KContentMsg<?, ?, Msg>>() {

    @Override
    public void handle(Msg content, KContentMsg<?, ?, Msg> container) {
      LOG.info("{}received Msg from:{}", logPrefix, container.getHeader().getSource());
      LOG.info("selfAdr is:" + selfAdr);
      trigger(new CRBBroadcast(content, selfAdr), crbPort);

    }
  };

  protected final Handler<CRBDeliver> handleCRBDeliver = new Handler<CRBDeliver>() {
    @Override
    public void handle(CRBDeliver crbDeliver) {
      LOG.info("I am node " + selfAdr);
    }
  };



  public static class Init extends se.sics.kompics.Init<AppComp> {

    public final KAddress selfAdr;
    public final Identifier gradientOId;

    public Init(KAddress selfAdr, Identifier gradientOId) {
      this.selfAdr = selfAdr;
      this.gradientOId = gradientOId;
    }
  }
}
