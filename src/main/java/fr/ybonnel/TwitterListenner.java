/*
 * Copyright 2013- Yan Bonnel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ybonnel;

import fr.ybonnel.modele.TweetPhoto;
import fr.ybonnel.simpleweb4j.handlers.eventsource.EndOfStreamException;
import fr.ybonnel.simpleweb4j.handlers.eventsource.ReactiveHandler;
import org.eclipse.jetty.util.ConcurrentHashSet;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TwitterListenner extends StatusAdapter {

    private String filter;

    public TwitterListenner(String filter) {
        this.filter = filter;
    }

    private Set<ReactiveHandler<TweetPhoto>> handlers = new ConcurrentHashSet<>();

    private TweetPhoto lastTweet = null;

    public void removeHandler(ReactiveHandler<TweetPhoto> handler) {
        handlers.remove(handler);
    }

    public void addHandler(ReactiveHandler<TweetPhoto> handler) {
        handlers.add(handler);
        if (lastTweet != null) {
            sendTweetToHandler(lastTweet, handler);
        }
    }

    public void startConsumeTwitter() {
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(this);
        twitterStream.filter(new FilterQuery().track(new String[]{filter}));
    }

    @Override
    public void onStatus(Status status) {
        TweetPhoto.fromStatus(status).forEach(tweet -> new HashSet<>(handlers).stream().forEach(handler -> sendTweetToHandler(tweet, handler)));
    }

    private void sendTweetToHandler(TweetPhoto tweet, ReactiveHandler<TweetPhoto> handler) {
        try {
            handler.next(tweet);
        }
        catch (EndOfStreamException e) {
            removeHandler(handler);
        }
        lastTweet = tweet;
    }
}
