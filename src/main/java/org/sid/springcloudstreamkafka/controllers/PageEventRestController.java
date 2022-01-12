package org.sid.springcloudstreamkafka.controllers;

import org.sid.springcloudstreamkafka.entities.PageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;


@RestController
public class PageEventRestController
{
    @Autowired
    private StreamBridge streamBridge;

    // Message Producer

    @GetMapping(path = "/publish/{topic}/{pageName}")
    public PageEvent publish(@PathVariable String topic, @PathVariable String pageName)
    {
        PageEvent pageEvent = new PageEvent(
                pageName,Math.random()>0.5?"USER1":"USER2", new Date(),new Random().nextInt(9000));

        streamBridge.send(topic, pageEvent);

        return pageEvent;
    }
}
