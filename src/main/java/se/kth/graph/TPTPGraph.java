package se.kth.graph;

import se.kth.app.sets.TwoPSet;

/**
 * Created by Amir on 2017-05-24.
 */
public class TPTPGraph {

    public TwoPSet vertexSet = new TwoPSet();
    public TwoPSet edgeSet = new TwoPSet();



    public boolean lookUpVertex(Vertex vertex){

        if(vertexSet.gset.contains(vertex.id) && !vertexSet.tombstoneset.contains(vertex.id)){
            return true;
        }
        return false;

    }

    public boolean lookUpEdge(Edge edge){

        if(lookUpVertex(edge.v1) && lookUpVertex(edge.v2) && edgeSet.gset.contains(edge.id)
                && !edgeSet.tombstoneset.contains(edge.id)){
            return true;
        }
        return false;
    }



}
