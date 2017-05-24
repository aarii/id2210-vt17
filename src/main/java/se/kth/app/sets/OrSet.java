package se.kth.app.sets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.app.AppComp;
import se.kth.app.test.Element;
import se.kth.app.test.ElementList;
import se.kth.app.test.Operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Amir on 2017-05-23.
 */
public class OrSet {

    private static final Logger LOG = LoggerFactory.getLogger(OrSet.class);


    public List<Element> payload = new ArrayList<>();

    public void addElement(Element element){

        List<UUID> tempUUID = new ArrayList<UUID>();
        for(Element e : payload){
           tempUUID.add(e.uuid);
        }
        if(!tempUUID.contains(element.uuid)) {
            payload.add(element);
        }

    }

    public List<UUID> removeElement(Element element){



        List<UUID> listOfElementsToRemove = new ArrayList<>();
        Iterator<Element> iteratorPayload = payload.iterator();
        while(iteratorPayload.hasNext()){
            Element e = iteratorPayload.next();
            if(element.value == e.value){
                listOfElementsToRemove.add(e.uuid);
                iteratorPayload.remove();
            }
        }
        return listOfElementsToRemove;
    }

    public void removeElementsDownStream(ElementList elementList){



        for(UUID uuid : elementList.uuidList){
            Iterator<Element> iteratorPayload = payload.iterator();
            while(iteratorPayload.hasNext()){
                Element e = iteratorPayload.next();
                if(uuid.compareTo(e.uuid) == 0){
                    iteratorPayload.remove();
                }
            }
        }
    }

    public boolean lookup(Element element){


        for(Element e : payload){
            if(e.value == element.value){
                return true;
            }
        }

        return false;

    }
}
