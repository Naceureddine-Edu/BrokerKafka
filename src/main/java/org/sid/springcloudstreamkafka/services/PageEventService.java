package org.sid.springcloudstreamkafka.services;

import org.sid.springcloudstreamkafka.entities.PageEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


@Service
public class PageEventService
{
    // ce code fontionne que ca soit avec kafka ou rabbitMQ ou un autre broker

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
}
