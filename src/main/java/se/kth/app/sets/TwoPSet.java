package se.kth.app.sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.AppComp;
import se.kth.app.test.Operation;

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

    public void removeElement(Object obj){


        if(gset.contains(obj)){
            tombstoneset.addElement(obj);
        }else{
            Operation op = (Operation) obj;
            LOG.info("Object " + op.op + " with value "+ op.value + " does not exist");
        }

    }


    public void printSetContent(GSet set){



    }
}
