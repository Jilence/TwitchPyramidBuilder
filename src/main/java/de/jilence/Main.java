package de.jilence;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import io.github.bucket4j.Bandwidth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Main {

    private static TwitchClient twitchClient;

    public static void main(String[] args ) {

        twitchClient = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withEnablePubSub(true)
                .withEnableHelix(true)
                .withDefaultAuthToken(new OAuth2Credential("twitch", accessToken))
                .withChatRateLimit(Bandwidth.simple(20, Duration.ofMillis(1)))
                .withChatAccount(new OAuth2Credential("twitch", accessToken))
                .build();

        //add channels to use bot in the channels, when you don't join the bot in a channel, the bot don't work
        twitchClient.getChat().joinChannel("CHANNELS..");
        twitchClient.getChat().joinChannel("CHANNELS..");
        twitchClient.getChat().joinChannel("CHANNELS..");
        twitchClient.getChat().joinChannel("CHANNELS..");

        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> {

            if(!event.getUser().getName().equalsIgnoreCase("YOUR_TWITCH_NAME")) return;

            if(event.getMessage().split(" ")[0].equalsIgnoreCase("==pyramid")) {
                int size = Integer.parseInt(event.getMessage().split(" ")[1]);
                String emote = event.getMessage().split(" ")[2];

                StringBuilder pyramid = new StringBuilder();

                for(int i = 0; i <= size; i++) {
                    pyramid.append(emote).append(" ");
                    twitchClient.getChat().sendMessage(event.getChannel().getName(), pyramid.toString());
                }


                pyramid = new StringBuilder(pyramid.substring(0, (pyramid.length() - (emote.length() + 2))));

                for(int i = 0; i <= (size - 1); i++) {
                    twitchClient.getChat().sendMessage(event.getChannel().getName(), pyramid.toString());
                    pyramid = new StringBuilder(pyramid.substring(0, (pyramid.length() - (emote.length() + 1))));
                }
            }
        });
    }

    //you can find your twitch code on https://twitchtokengenerator.com/, and must have necessity scopes
    private static final String accessToken = "YOUR_TWITCH_CODE";

}
