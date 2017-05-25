package se.kth.app.sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.AppComp;
import se.kth.app.test.Operation;
import se.kth.graph.Edge;
import se.kth.graph.Vertex;
import se.sics.ktoolbox.util.identifiable.Identifier;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 2017-05-22.
 */
public class TwoPSet {
    private static final Logger LOG = LoggerFactory.getLogger(TwoPSet.class);

    public GSet gset;
    public GSet tombstoneset;

    public TwoPSet() {
        gset = new GSet(new ArrayList<Object>());
        tombstoneset = new GSet(new ArrayList<Object>());
    }

    public void removeElement(Object obj, Identifier id){

        if(gset.contains(obj)){
            System.out.println();
            tombstoneset.addElementTombstone(obj, id);
            //LOG.debug("Node {} removed the object {}", id, obj);
        }else{
            LOG.info("Object " + obj + " does not exist");
        }
    }

    public void removeVertex(Vertex v, Identifier id){

        if(gset.containsVertex(v)){
            System.out.println();
            tombstoneset.addElementTombstone(v, id);
            //LOG.debug("Node {} removed the object {}", id, obj);
        }else{
            LOG.info("Object " + v + " does not exist");
        }
    }



    public void addElement(Object obj, Identifier id){
        gset.addElement(obj, id);


    }



    public void printSetContent(GSet set){

    }

    public void removeEdge(Edge e, Identifier id) {


        if(gset.containsEdge(e)){
            System.out.println();
            tombstoneset.addElementTombstone(e, id);
            //LOG.debug("Node {} removed the object {}", id, obj);
        }else{
            LOG.info("Object " + e + " does not exist");
        }
    }
}
