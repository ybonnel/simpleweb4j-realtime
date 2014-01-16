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
package fr.ybonnel.modele;

import twitter4j.MediaEntity;
import twitter4j.Status;

import java.util.Arrays;
import java.util.stream.Stream;

public class TweetPhoto {


    private final String user;
    private final String avatar;
    private final String photo;
    private final String text;

    public TweetPhoto(String user, String avatar, String photo, String text) {
        this.user = user;
        this.avatar = avatar;
        this.photo = photo;
        this.text = text;
    }

    public static Stream<TweetPhoto> fromStatus(Status status) {
        String user = status.getUser().getScreenName();
        String avatar = status.getUser().getBiggerProfileImageURL();
        String text = status.getText();
        return Arrays.stream(status.getMediaEntities())
                .map(MediaEntity::getMediaURL)
                .map(url -> new TweetPhoto(user, avatar, url, text));
    }

}
