package se.kth.app.test;

import se.kth.graph.Edge;
import se.kth.graph.Vertex;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;
import java.util.List;
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
    public int v1;
    public int v2;
    public Edge edge;
    public List<Edge> edges;
    public int id;



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

    public Operation(Vertex vertex, List<Edge> edges, String set, String op){
        this.vertex = vertex;
        this.set = set;
        this.op = op;
        this.edges = edges;
    }


    public Operation(Vertex vertex, String set, String op){
        this.vertex = vertex;
        this.set = set;
        this.op = op;
    }


    public Operation(Edge edge, String set, String op){
        this.edge = edge;
        this.set = set;
        this.op = op;
    }

    public Operation(String op, int id, int v1, int v2, String set){
        this.op = op;
        this.id = id;
        this.v1 = v1;
        this.v2 = v2;
        this.set = set;
    }

}
