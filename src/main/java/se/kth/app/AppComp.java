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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.CRB.CRBBroadcast;
import se.kth.CRB.CRBDeliver;
import se.kth.CRB.CRBPort;
import se.kth.GBEB.GBEBBroadcast;
import se.kth.GBEB.GBEBDeliver;
import se.kth.app.sets.TwoPSet;
import se.kth.app.test.Operation;
import se.kth.eagerRB.EagerBroadcast;
import se.kth.eagerRB.EagerDeliver;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.kompics.timer.Timer;
import se.sics.ktoolbox.util.identifiable.Identifier;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import sun.rmi.runtime.Log;

/**
 * @author Alex Ormenisan <aaor@kth.se>
 */
public class AppComp extends ComponentDefinition {

  private static final Logger LOG = LoggerFactory.getLogger(AppComp.class);
  private String logPrefix = " ";

  //*******************************CONNECTIONS********************************

  Positive<Network> networkPort = requires(Network.class);
  Positive<CRBPort> crbPort = requires(CRBPort.class);
  //**************************************************************************
  private KAddress selfAdr;
  TwoPSet twoPSet = new TwoPSet();

  public AppComp(Init init) {
    selfAdr = init.selfAdr;
    logPrefix = "<nid:" + selfAdr.getId() + ">";
    // LOG.info("{}initiating...", logPrefix);

    subscribe(handleStart, control);
    subscribe(handleMsg, networkPort);
    subscribe(handleCRBDeliver, crbPort);
  }

  Handler handleStart = new Handler<Start>() {
    @Override
    public void handle(Start event) {
      //  LOG.info("{}starting...", logPrefix);
    }
  };


  ClassMatchedHandler handleMsg = new ClassMatchedHandler<Operation, KContentMsg<?, ?, Operation>>() {

    @Override
    public void handle(Operation operation, KContentMsg<?, ?, Operation> container) {
      //  LOG.info("selfAdr is:" + selfAdr);
      LOG.info(" Node {} received a {} operation from TestComp with address: {}", selfAdr.getId(), operation.op, container.getHeader().getSource());

      if(operation.op.equalsIgnoreCase("ADD")){
       // LOG.info("Node {} added object.value {}", selfAdr.getId(), operation.value);

        twoPSet.addElement(operation.value, selfAdr.getId());

      }

      if(operation.op.equalsIgnoreCase("RM")){
       // LOG.info("Node {} removed object.value {}", selfAdr.getId(), operation.value);
        twoPSet.removeElement(operation.value, selfAdr.getId());

      }

      if(operation.op.equalsIgnoreCase("LOOKUP")){

      }
      CRBBroadcast crbBroadcast = new CRBBroadcast(operation, selfAdr);
      //LOG.debug("Node " + selfAdr.getId() + " with crbBroadcast: " + crbBroadcast);

      trigger(crbBroadcast, crbPort);

    }
  };

  protected final Handler<CRBDeliver> handleCRBDeliver = new Handler<CRBDeliver>() {
    @Override
    public void handle(CRBDeliver crbDeliver) {

      if(crbDeliver.msg instanceof Operation){

        Operation operation = ((Operation) crbDeliver.msg);
        //LOG.debug("VÅR OPERATION I CRBDELIVER ÄR: " + operation.op + " MED VALUE " + operation.value);
     //   LOG.info(" Node {} received the Operation {} ", selfAdr.getId(), operation.op);

        if(operation.op.equalsIgnoreCase("ADD")){
         // LOG.info("Node {} received an ADD operation with value {}" , selfAdr.getId(), operation.value);
          twoPSet.addElement(operation.value, selfAdr.getId());

        }

        if(operation.op.equalsIgnoreCase("RM")){
         // LOG.info("Node {} received an RM operation with value {}" , selfAdr.getId(), operation.value);
          twoPSet.removeElement(operation.value, selfAdr.getId());

        }

        if(operation.op.equalsIgnoreCase("LOOKUP")){

        }
       // System.out.println("VÅRT SET INNEHÅLLER " + twoPSet.gset);

      }
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
