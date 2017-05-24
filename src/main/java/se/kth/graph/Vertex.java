package se.kth.graph;

import se.sics.kompics.KompicsEvent;

/**
 * Created by Amir on 2017-05-24.
 */
public class Vertex implements KompicsEvent {
    public int id;

    public Vertex(int id){
      this.id = id;
    }
}
