package se.kth.app.sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.AppComp;
import se.kth.app.test.Operation;
import se.kth.graph.Vertex;
import se.sics.ktoolbox.util.identifiable.Identifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 2017-05-22.
 */
public class GSet {
    private static final Logger LOG = LoggerFactory.getLogger(GSet.class);

    public List<Object> set;

    public GSet(ArrayList<Object> set){
        this.set = set;

    }


    public void addElement(Object obj, Identifier id){

        if (!set.contains(obj)){
            //LOG.debug("Node {} added the object {}", id, obj);
            set.add(obj);
        }else{
          // LOG.info("For Node " + id + " the object " + obj + " is already added");
        }

    }

    public void addElementTombstone(Object obj, Identifier id){

        if (!set.contains(obj)){
            set.add(obj);
        }else{
            System.out.println("Object already added");
        }

    }


    public Object getElement(int index){

        if(set.size() >= index) {
            return set.get(index);
        }

        return null;
    }

    public boolean contains(Object obj){

        if(set.contains(obj)){
            return true;
        }

        return false;
    }

    public boolean containsVertex(Vertex vertex){

        for(Object v : set){
            Vertex vtemp = (Vertex) v;
            if(vtemp.id == vertex.id){
                return true;
            }
        }
        return false;
    }
}
