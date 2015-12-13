package org.aap.kafka.spark.example.p1;

import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

//
// Processor 1 pipeline
//
public class KaProcessor1 {
	public static void main(String[] args) {
		
		// Init spark
		JavaStreamingContext jssc = KaHelpers.initStreams("KaProcessor1");
		
		// Receive messages from Kafka
		JavaPairInputDStream<String, String> messages = KaHelpers.readKafkaMessages(jssc, "spipe1");

		/////////////////////////
		// Processing Pipeline
		//
		
		// Convert string to objects (placeholder)
		messages.map(tuple2 -> {
			System.out.println("-- mapped " + tuple2._2());
			return new InRecord(tuple2._1(), tuple2._2());
			
		// filter task (placeholder)
		}).filter(record -> {
			if (record.field2.contains("111")) {
				System.out.println("-- filtered out " + record.field2);
				return false;
			}
			return true;
			
		// translate to other object type (placeholder)
		}).map(inrecord -> {
			System.out.println("-- transformed " + inrecord.field2);
			return new OutRecord(inrecord.field1, inrecord.field2);
			
		// send to "kitcat2" topic for processing by Processor 2
		}).foreachRDD(rdd -> {
			rdd.foreachPartition(partitionOfRecords -> {
				KaHelpers.sendToKafkaOutput (partitionOfRecords, "spipe2");

			});
			return null;
		});

		// Start the computation
		KaHelpers.waitStreams (jssc);
	}
	
}
