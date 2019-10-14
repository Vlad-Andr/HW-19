package com.cursor.hw19.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

@Service
public class TwitterService {

    private static final String CONSUMER_KEY = "";
    private static final String CONSUMER_SECRET = "";
    private static final String ACCESS_TOKEN = "";
    private static final String ACCESS_TOKEN_SECRET = "";

    private static ConnectableFlux<Status> twitterStream;

    public static synchronized ConnectableFlux getTwitterStream() {
        if (twitterStream == null) {
            initTwitterStream();
        }
        return twitterStream;
    }

    private static void initTwitterStream() {
        Flux<Status> stream = Flux.create(emmiter -> {
            StatusListener listener = new StatusListener() {

                @Override
                public void onException(Exception e) {
                    emmiter.error(e);
                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice arg) {
                }

                @Override
                public void onScrubGeo(long userId, long upToStatusId) {
                }

                @Override
                public void onStallWarning(StallWarning warning) {
                }

                @Override
                public void onStatus(Status status) {
                    emmiter.next(status);
                }

                @Override
                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                    System.out.println(numberOfLimitedStatuses);
                }
            };

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(CONSUMER_KEY)
                    .setOAuthConsumerSecret(CONSUMER_SECRET)
                    .setOAuthAccessToken(ACCESS_TOKEN)
                    .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

            TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
            twitterStream.addListener(listener);
            twitterStream.sample();

        });
        twitterStream = stream.publish();
        twitterStream.connect();
    }

    public static ConnectableFlux gettingTimeline() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        List<Status> statuses = twitter.getHomeTimeline();
        System.out.println("Showing home timeline.");
        for (Status status : statuses) {
            System.out.println(status.getUser().getName() + ":" + status.getText());
        }
        return null;
    }
}

