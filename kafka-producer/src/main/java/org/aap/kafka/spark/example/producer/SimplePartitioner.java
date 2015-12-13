package org.aap.kafka.spark.example.producer;

import java.io.Serializable;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;
 
public class SimplePartitioner implements Partitioner, Serializable {
	static int partition=1;
	
    public SimplePartitioner (VerifiableProperties props) {
 
    }
 
    public int partition(Object key, int a_numPartitions) {
    	if (partition < 5) {
    		partition++;
    	}
    	else {
    		partition = 1;
    	}
		return 1;
  }
 
}