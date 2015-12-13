package org.aap.kafka.spark.example.producer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
 
public class KaProducer {
    public static void main(String[] args) {
	    

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
  
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        
        File folder = new File("./data/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
                try {
                	System.out.println("Sending file " + file.getName());
                	
                    String fileContent = new String(Files.readAllBytes(Paths.get(file.getPath())));
                   	System.out.println(fileContent);

					ProducerRecord<String, String> message = new ProducerRecord<String, String>("spipe1", file.getName(), fileContent);
					producer.send(message);		        	
                 }
                catch (IOException ex) {
                	ex.printStackTrace();
                }
                catch (Exception ex) {
                	ex.printStackTrace();
                }
                
            }
        }
 
        producer.close();
    }
}