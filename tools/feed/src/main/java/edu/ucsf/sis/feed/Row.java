package edu.ucsf.sis.feed;

import java.util.HashMap;
import java.util.Set;

public class Row<K,V> extends HashMap
{

    public Set<K> keySet(){
	return super.keySet();
    }

}
