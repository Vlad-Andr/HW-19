package com.cursor.hw19.controller;

import com.cursor.hw19.service.TwitterService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import twitter4j.Status;
import twitter4j.TwitterException;

@RestController
@RequestMapping("twitter")
public class TwitterController {

    @GetMapping(name = "/feed",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> feed() {
        ConnectableFlux<Status> flux = TwitterService.getTwitterStream();
        return flux.map(Status::getText);
    }

    @GetMapping("getTimeline")
    public  Flux<String> gettingTimeline() throws TwitterException {
        ConnectableFlux all = TwitterService.gettingTimeline();
        return all;
    }
}
