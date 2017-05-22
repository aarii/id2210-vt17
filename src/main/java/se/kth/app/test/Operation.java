package se.kth.app.test;

import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.KompicsEvent;

import java.io.Serializable;

/**
 * Created by araxi on 2017-05-19.
 */
public class Operation implements KompicsEvent, Serializable {
    public String op;
    public int value;

    public Operation(String op, int value){
    this.op = op;
    this.value = value;
    }




}
