package se.kth.app.test;

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



}
