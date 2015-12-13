package org.aap.kafka.spark.example.p2;

import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;


public class KaProcessor2 {
	
	public static void main(String[] args) {
		
		// Init spark
		JavaStreamingContext jssc = KaHelpers.initStreams("KaProcessor2");
		
		// Receive messages from Kafka
		JavaPairInputDStream<String, String> messages = KaHelpers.readKafkaMessages(jssc, "spipe2");

		/////////////////////////
		// Processing Pipeline
		//
	    // Store or send to next app (placeholder)
	    messages.foreachRDD(rdd -> {
	        rdd.foreachPartition(partitionOfRecords -> {
		        while (partitionOfRecords.hasNext()) {
		        	Tuple2<String, String> record = partitionOfRecords.next();
	  	  	    	System.out.println("-- saved " + record._1 + " " + record._2);
		        }
	    	});
	        return null;
	      });
	    
		// Start the computation
		KaHelpers.waitStreams (jssc);
	}
}
