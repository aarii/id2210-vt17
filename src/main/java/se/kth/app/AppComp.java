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
import se.kth.app.sets.OrSet;
import se.kth.app.sets.TwoPSet;
import se.kth.app.test.Element;
import se.kth.app.test.ElementList;
import se.kth.app.test.Operation;
import se.kth.graph.Edge;
import se.kth.graph.TPTPGraph;
import se.kth.graph.Vertex;
import se.sics.kompics.*;
import se.sics.kompics.network.Network;
import se.sics.ktoolbox.util.identifiable.Identifier;
import se.sics.ktoolbox.util.network.KAddress;
import se.sics.ktoolbox.util.network.KContentMsg;
import java.util.List;
import java.util.UUID;

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
  OrSet orSet = new OrSet();
  TPTPGraph tptpGraph = new TPTPGraph();
  boolean source;


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
      LOG.info("Node {}: received a {} operation from TestComp with address: {}", selfAdr.getId(), operation.op, container.getHeader().getSource());

      if(operation.set.equalsIgnoreCase("2pset")){
        simulateTwoPSet(operation);
        CRBBroadcast crbBroadcast = new CRBBroadcast(operation, selfAdr);
        trigger(crbBroadcast, crbPort);
      }

      if(operation.set.equalsIgnoreCase("orset")){
        UUID uuid = UUID.randomUUID();
        Element element = new Element(uuid, operation.value);
        simulateOrSet(element, operation.op, operation.set);

      }if(operation.set.equalsIgnoreCase("tptpgraph")){
        simulateTPTPGraph(operation);

      }
    }
  };


  public void simulateTPTPGraph(Operation operation){
    if(operation.op.equalsIgnoreCase("ADD_V")){

      Vertex vertex = new Vertex(operation.value);
      tptpGraph.vertexSet.addElement(vertex, selfAdr.getId());

      Operation op = new Operation(vertex, operation.set, operation.op);
      CRBBroadcast crbBroadcast = new CRBBroadcast(op, selfAdr);
      trigger(crbBroadcast, crbPort);

    }

/*TODO Implement RM_V so we can check if we can add vertices and remove them */

    if(operation.op.equalsIgnoreCase("ADD_E")){



    }
  }

  public void simulateOrSet(Element element, String operation, String set) {
    if(operation.equalsIgnoreCase("ADD")){
      //orSet.addElement(element);
      CRBBroadcast crbBroadcast = new CRBBroadcast(new Operation(element, set), selfAdr);
      trigger(crbBroadcast, crbPort);
    /*  LOG.info("Node {}: after adding element with value "+ element.value + " and UUID "+ element.uuid + ", orSet contains now: ", selfAdr.getId());
      for(Element e : orSet.payload){
        LOG.info("(Node "+ selfAdr.getId()+ ") Value: " + e.value +" and UUID: " + e.uuid );
      }
      System.out.println();*/
    }

    if(operation.equalsIgnoreCase("RM")){
      if(orSet.lookup(element)){
        List<UUID> elementsToBeRemoved =  orSet.removeElement(element);
        ElementList elementslist = new ElementList(elementsToBeRemoved);
        CRBBroadcast crbBroadcast = new CRBBroadcast(new Operation(elementslist,set),selfAdr);
        trigger(crbBroadcast, crbPort);
      }
    }
  }


  public void simulateTwoPSet(Operation operation){
    if(operation.op.equalsIgnoreCase("ADD")){
      twoPSet.addElement(operation.value, selfAdr.getId());
    }

    if(operation.op.equalsIgnoreCase("RM")){
      twoPSet.removeElement(operation.value, selfAdr.getId());
    }
  }


  protected final Handler<CRBDeliver> handleCRBDeliver = new Handler<CRBDeliver>() {
    @Override
    public void handle(CRBDeliver crbDeliver) {
      if(crbDeliver.msg instanceof Operation){
        Operation operation = ((Operation) crbDeliver.msg);

        if(operation.set.equalsIgnoreCase("2pset")){
          simulateTwoPSet(operation);
        }
        if(operation.set.equalsIgnoreCase("orset")){

          if(operation.elementList instanceof ElementList){
            ElementList elementList =  operation.elementList;

            System.out.println();
            LOG.info("Node {}: List of UUIDs to be removed are: ", selfAdr.getId());
            for(UUID uuid : elementList.uuidList){
              LOG.info("(Node "+ selfAdr.getId()+ ") "+ uuid.toString());
            }
            System.out.println();
            orSet.removeElementsDownStream(elementList);

            LOG.info("Node {}: Orset after remove contains: ", selfAdr.getId());
            for(Element e : orSet.payload){
              LOG.info("(Node "+ selfAdr.getId()+ ") Value: " + e.value +" and UUID: " + e.uuid );

            }
            System.out.println();

          }

          if(operation.element instanceof Element){

            Element element =  operation.element;
            orSet.addElement(element);

            LOG.info("Node {}: after adding element with value "+ element.value + " and UUID "+ element.uuid + ", orSet contains now: ", selfAdr.getId());
            for(Element e : orSet.payload){
              LOG.info("(Node "+ selfAdr.getId()+ ") Value: " + e.value +" and UUID: " + e.uuid );
            }
            System.out.println();
          }


        }

        if(operation.set.equalsIgnoreCase("tptpgraph")){

          if(operation.vertex instanceof Vertex){

            if(operation.op.equalsIgnoreCase("ADD_V")) {
              tptpGraph.vertexSet.addElement(operation.vertex, selfAdr.getId());
              LOG.info("Node {}: after adding vertex with id {} , VA contains now: ", selfAdr.getId(), operation.vertex.id);
              for (Object e : tptpGraph.vertexSet.gset.set) {
                LOG.info("(Node id  " + selfAdr.getId() + ") vertex id : " + e);
              }
              System.out.println();
            }

            if(operation.op.equalsIgnoreCase("RM_V")){

            }
          }

          if(operation.edge instanceof Edge){

          }
        }

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
