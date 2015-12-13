package org.aap.kafka.spark.example.p1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import kafka.serializer.StringDecoder;

public class KaHelpers {
	//
	// Init Spark streaming
	//
	static JavaStreamingContext initStreams (String appName) {
		
		// Init Spark config
		SparkConf sparkConf = new SparkConf().setAppName("KaProcessor1");
		sparkConf.setMaster("local[*]");
		sparkConf.setSparkHome("/Users/aapollonsky/Tools/spark-1.5.2");

		// Create context with a 2 seconds batch interval
		JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(2));
		return jssc;
	}

	//
	// Start waiting for streams
	//
	static void waitStreams (JavaStreamingContext jssc) {	
		jssc.start();
		jssc.awaitTermination();
	}

	
	//
	// Read messages from Kafka queue
	//
	static JavaPairInputDStream<String, String> readKafkaMessages (JavaStreamingContext jssc, String topics) {
		String brokers = "localhost:9092";

		HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
		HashMap<String, String> kafkaParams = new HashMap<String, String>();
		kafkaParams.put("metadata.broker.list", brokers);

		// Create direct kafka stream with brokers and topics
		return KafkaUtils.createDirectStream(jssc, String.class, String.class,
				StringDecoder.class, StringDecoder.class, kafkaParams, topicsSet);
	}
	
	//
	// Send messages to Kafka queue
	//
	static void sendToKafkaOutput (Iterator <OutRecord> partitionOfRecords, String topic) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		final KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

		while (partitionOfRecords.hasNext()) {
			OutRecord outrecord = partitionOfRecords.next();

			System.out.println("-- sent: " + outrecord);
			ProducerRecord message = new ProducerRecord<String, String>(topic, outrecord.outfield1,
					outrecord.outfield2);
			
			producer.send(message);
		}
	}
	
	
}
