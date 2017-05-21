package com.minitwit;

import static j2html.TagCreator.article;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.minitwit.config.WebConfig;
import com.minitwit.model.User;
import com.minitwit.service.impl.MiniTwitService;
import com.minitwit.util.GravatarUtil;
import static j2html.TagCreator.div;
import static j2html.TagCreator.img;

@Configuration
@ComponentScan({"com.minitwit"})
public class App {

    private static final String GRAVATAR_DEFAULT_IMAGE_TYPE = "monsterid";
    private static final int GRAVATAR_SIZE = 34;
    public static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    public static int nextUserNumber = 1;
    //Assign to username for next connecting user
    private static MiniTwitService service;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(App.class);
        service = ctx.getBean(MiniTwitService.class);
        new WebConfig(service);
        ctx.registerShutdownHook();
    }

    //Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender, message))
                        .put("userlist", userUsernameMap.values())
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        User existingUser = service.getUserbyUsername(sender);
        System.out.print(existingUser.getEmail());
        existingUser.setGravatar(GravatarUtil.gravatarURL(existingUser.getEmail(), GRAVATAR_DEFAULT_IMAGE_TYPE, GRAVATAR_SIZE));
        return article().with(
                img().attr("src",
                        existingUser.getGravatar().isEmpty()
                        ? "ttp://www.gravatar.com/avatar/4e89474a21f58e025cffd9214751b761?d=monsterid&s=48" : existingUser.getGravatar()).attr("class", "circle"),
                b("  "+sender + " dice:"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }
}
