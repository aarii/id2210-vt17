package se.kth.app.test;

import se.kth.graph.Edge;
import se.kth.graph.Vertex;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by araxi on 2017-05-19.
 */
public class Operation implements KompicsEvent, Serializable {
    public String op;
    public int value;
    public Element element;
    public ElementList elementList;
    public String set;
    public Vertex vertex;
    public Edge edge;



    public Operation(String op, int value, String set){
    this.op = op;
    this.value = value;
    this.set = set;
    }


    public Operation(Element element, String set){
      this.element = element;
      this.set = set;
    }

    public Operation(ElementList elementList, String set){
        this.elementList = elementList;
        this.set = set;
    }

    public Operation(Vertex vertex, String set, String op){
        this.vertex = vertex;
        this.set = set;
        this.op = op;
    }

    public Operation(Edge edge, String set){
        this.edge = edge;
        this.set = set;
    }

}
