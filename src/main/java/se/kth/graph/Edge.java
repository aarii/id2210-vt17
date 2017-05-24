package se.kth.graph;

import se.sics.kompics.KompicsEvent;

/**
 * Created by Amir on 2017-05-24.
 */
public class Edge implements KompicsEvent {

    public Vertex v1;
    public Vertex v2;
    public int id;

    public Edge(Vertex v1, Vertex v2, int id){
        this.v1 = v1;
        this.v2 = v2;
        this.id = id;

    }
}
