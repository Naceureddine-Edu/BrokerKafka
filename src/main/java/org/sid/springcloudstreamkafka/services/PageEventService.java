package org.sid.springcloudstreamkafka.services;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.sid.springcloudstreamkafka.entities.PageEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


@Service
public class PageEventService
{
    // ce code fontionne que ca soit avec kafka ou rabbitMQ ou un autre broker
    // ------------------BATCH PROCESSING-------------------------------------
    @Bean
    public Consumer<PageEvent> pageEventConsumer()
    {
        // input c'est le flux ou les messages ou les eventPage recu
        return (input) -> {
            System.out.println("-----------**********---------");
            System.out.println(input.toString());
            System.out.println("-----------**********---------");
        };
    }

    @Bean
    public Supplier<PageEvent> pageEventSupplier()
    {
        return  () -> new PageEvent(
                Math.random()>0.5?"PAGE1":"PAGE2",
                Math.random()>0.5?"USER1":"USER2",
                new Date(),
                new Random().nextInt(9000));
    }

    @Bean
    public Function<PageEvent, PageEvent> pageEventFunction()
    {
        // input c'est le flux ou les messages ou les eventPage recu

        return (input)-> {
          input.setPageName("Ma Page");
          input.setUserName("Moi Meme");
          input.setDuration(7777);

          // out ce qui va etre stocker ou envoyer vers le topic R3
          return input;
        };
    }
    // ------------------BATCH PROCESSING END-------------------------------------


    // ------------------RealTIME PROCESSING-------------------------------------
    @Bean
    public Function<KStream<String, PageEvent>, KStream<String, Long>> kStreamFunction()
    {
        return (input) -> {
            return input
                    .filter((k, v) -> v.getDuration() > 100)
                    .map((k, v) -> new KeyValue<>(v.getPageName(), 0L))
                    .groupBy((k, v) -> k, Grouped.with(Serdes.String(), Serdes.Long()))
                    .windowedBy(TimeWindows.of(Duration.ofDays(5000)))
                    // Creation d'un store pour stocker et acceder a ces données a partir du controller
                    .count(Materialized.as("page-count"))  // Fonction d'agreggation(SUM,AVG...)
                    .toStream()
                    .map((k, v) ->
                            new KeyValue<>("=> Start : " + k.window().startTime() + " Ends :" + k.window().endTime() + " Page :" + k.key(), v));
           };
        }
    }
