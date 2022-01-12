package org.sid.springcloudstreamkafka.entities;

import lombok.*;
import java.util.Date;


@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class PageEvent
{
    private String pageName;
    private String userName;
    private Date dateVisitePage;
    private long  duration;
}
