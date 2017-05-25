package se.kth.graph;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import se.kth.app.sets.TwoPSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amir on 2017-05-24.
 */
public class TPTPGraph {

    public TwoPSet vertexSet = new TwoPSet();
    public TwoPSet edgeSet = new TwoPSet();



    public boolean lookUpVertex(Vertex vertex){

        if(vertexSet.gset.containsVertex(vertex) && !vertexSet.tombstoneset.containsVertex(vertex)){
            return true;
        }
        return false;

    }

    public boolean lookUpEdge(Edge edge){
        Edge e = getEdgeVertices(edge);

        if(lookUpVertex(e.v1) && lookUpVertex(e.v2) && edgeSet.gset.containsEdge(e)
                && !edgeSet.tombstoneset.containsEdge(e)){
            return true;
        }
        return false;
    }


    public Edge getEdgeVertices(Edge edge){
        for(Object o : edgeSet.gset.set) {
            Edge e = (Edge) o;
            if(e.id == edge.id ){
                return e;
            }
        }
        return null;
    }



  public List<Edge> edgesToBeRemoved(Vertex v){
      List<Edge> edges = new ArrayList<>();
      for(Object o : edgeSet.gset.set){
          Edge e = (Edge) o;
          if(v.id == e.v1.id || v.id == e.v2.id){
              edges.add(e);
          }
      }
      return edges;
  }

}
