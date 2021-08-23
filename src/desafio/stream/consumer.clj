(ns desafio.stream.consumer
  (:import (java.util Properties)
           (org.apache.kafka.clients.consumer ConsumerConfig KafkaConsumer)
           (java.time Duration)))

(defn- build-properties []
  (doto (Properties.)
    (.putAll {ConsumerConfig/BOOTSTRAP_SERVERS_CONFIG,       "127.0.0.1:9092"
              ConsumerConfig/KEY_DESERIALIZER_CLASS_CONFIG,  "org.apache.kafka.common.serialization.StringDeserializer"
              ConsumerConfig/VALUE_DESERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringDeserializer"
              ConsumerConfig/GROUP_ID_CONFIG,                "EmailService"})))

(defn parse [record]
  (println "----------")
  (println "Send email")
  (println (.key record))
  (println (.value record))
  (println (.partition record))
  (println (.offset record)))

(defn consumer! []
  (with-open [consumer (KafkaConsumer. (build-properties))]
    (.subscribe consumer ["ECOMMERCE_SEND_EMAIL"])
    (while true
      (let [records (seq (.poll consumer (Duration/ofSeconds 5)))]
        (println "Encontrei" (count records) "registros")
        (println records)
        (if-let [record (first records)]
          (parse record))
        ))))

(consumer!)