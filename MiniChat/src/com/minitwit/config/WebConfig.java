package com.minitwit.config;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;  

import com.minitwit.model.LoginResult;
import com.minitwit.model.Message;
import com.minitwit.model.User;
import com.minitwit.service.impl.MiniTwitService;

import spark.ModelAndView;
import spark.Request;
import spark.template.freemarker.FreeMarkerEngine;
import spark.utils.StringUtils;

public class WebConfig {
	
	private static final String USER_SESSION_ID = "user";
	private MiniTwitService service;
	 

	public WebConfig(MiniTwitService service) {
		this.service = service;
		 staticFiles.location("/public");
		 webSocket("/socket", ChatWebSocketHandler.class);
		setupRoutes();
	}
	
	private void setupRoutes() {
		
		get("/", (req, res) -> {
			User user = getAuthenticatedUser(req);
			Map<String, Object> map = new HashMap<>();
			map.put("pageTitle", "Felix_Chat");
			map.put("user", user);
			List<Message> messages = service.getUserFullTimelineMessages(user);
			map.put("messages", messages);
			return new ModelAndView(map, "timeline.ftl");
        }, new FreeMarkerEngine());
		before("/", (req, res) -> {
			User user = getAuthenticatedUser(req);
			if(user == null) {
				res.redirect("/login");
				halt();
			}
		});

		
		
		get("/public", (req, res) -> {
			User user = getAuthenticatedUser(req);
			Map<String, Object> map = new HashMap<>();
			map.put("pageTitle", "Felix_Chat");
			map.put("user", user);
			List<Message> messages = service.getPublicTimelineMessages();
			map.put("messages", messages);
			return new ModelAndView(map, "timeline.ftl");
        }, new FreeMarkerEngine());
		before("/public", (req, res) -> {
			User user = getAuthenticatedUser(req);
			if(user == null) {
				res.redirect("/login");
				halt();
			}
		});
		
		
		get("/t/:username", (req, res) -> {
			String username = req.params(":username");
			User profileUser = service.getUserbyUsername(username);
			
			User authUser = getAuthenticatedUser(req);
			boolean followed = false;
			if(authUser != null) {
				followed = service.isUserFollower(authUser, profileUser);
			}
			List<Message> messages = service.getUserTimelineMessages(profileUser);
			
			Map<String, Object> map = new HashMap<>();
			map.put("pageTitle", username + "Felix_Chat");
			map.put("user", authUser);
			map.put("profileUser", profileUser);
			map.put("followed", followed);
			map.put("messages", messages);
			return new ModelAndView(map, "timeline.ftl");
        }, new FreeMarkerEngine());
		
		before("/t/:username", (req, res) -> {
			String username = req.params(":username");
			User profileUser = service.getUserbyUsername(username);
			if(profileUser == null) {
				halt(404, "Usuario No Encontrado");
			}
		});
		
		
		
		get("/t/:username/follow", (req, res) -> {
			String username = req.params(":username");
			User profileUser = service.getUserbyUsername(username);
			User authUser = getAuthenticatedUser(req);
			
			service.followUser(authUser, profileUser);
			res.redirect("/t/" + username);
			return null;
        });
		
		before("/t/:username/follow", (req, res) -> {
			String username = req.params(":username");
			User authUser = getAuthenticatedUser(req);
			User profileUser = service.getUserbyUsername(username);
			if(authUser == null) {
				res.redirect("/login");
				halt();
			} else if(profileUser == null) {
				halt(404, "Usuario No Encontrado");
			}
		});
		
		
		
		get("/t/:username/unfollow", (req, res) -> {
			String username = req.params(":username");
			User profileUser = service.getUserbyUsername(username);
			User authUser = getAuthenticatedUser(req);
			
			service.unfollowUser(authUser, profileUser);
			res.redirect("/t/" + username);
			return null;
        });
		
		before("/t/:username/unfollow", (req, res) -> {
			String username = req.params(":username");
			User authUser = getAuthenticatedUser(req);
			User profileUser = service.getUserbyUsername(username);
			if(authUser == null) {
				res.redirect("/login");
				halt();
			} else if(profileUser == null) {
				halt(404, "Usuario No Encontrado");
			}
		});
		
		
		
		get("/login", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			if(req.queryParams("r") != null) {
				map.put("message", "Usuario reistrado correctamente, puede Iniciar Sesíon Ahora.");
			}
			return new ModelAndView(map, "login.ftl");
        }, new FreeMarkerEngine());
		
		post("/login", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			User user = new User();
			try {
				MultiMap<String> params = new MultiMap<String>();
				UrlEncoded.decodeTo(req.body(), params, "UTF-8");
				BeanUtils.populate(user, params);
			} catch (Exception e) {
				halt(501);
				return null;
			}
			LoginResult result = service.checkUser(user);
			if(result.getUser() != null) {
				addAuthenticatedUser(req, result.getUser());
				res.redirect("/");
				halt();
			} else {
				map.put("error", result.getError());
			}
			map.put("username", user.getUsername());
			return new ModelAndView(map, "login.ftl");
        }, new FreeMarkerEngine());
		
		before("/login", (req, res) -> {
			User authUser = getAuthenticatedUser(req);
			if(authUser != null) {
				res.redirect("/");
				halt();
			}
		});
		
		
		
		get("/register", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			return new ModelAndView(map, "register.ftl");
        }, new FreeMarkerEngine());
		
		post("/register", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			User user = new User();
			try {
				MultiMap<String> params = new MultiMap<String>();
				UrlEncoded.decodeTo(req.body(), params, "UTF-8");
				BeanUtils.populate(user, params);
			} catch (Exception e) {
				halt(501);
				return null;
			}
			String error = user.validate();
			if(StringUtils.isEmpty(error)) {
				User existingUser = service.getUserbyUsername(user.getUsername());
				if(existingUser == null) {
					service.registerUser(user);
					res.redirect("/login?r=1");
					halt();
				} else {
					error = "The username is already taken";
				}
			}
			map.put("error", error);
			map.put("username", user.getUsername());
			map.put("email", user.getEmail());
			return new ModelAndView(map, "register.ftl");
        }, new FreeMarkerEngine());
		
		before("/register", (req, res) -> {
			User authUser = getAuthenticatedUser(req);
			if(authUser != null) {
				res.redirect("/");
				halt();
			}
		});
		
		
		
		post("/message", (req, res) -> {
			User user = getAuthenticatedUser(req);
			MultiMap<String> params = new MultiMap<String>();
			UrlEncoded.decodeTo(req.body(), params, "UTF-8");
			Message m = new Message();
			m.setUserId(user.getId());
			m.setPubDate(new Date());
			BeanUtils.populate(m, params);
			service.addMessage(m);
			res.redirect("/");
			return null;
        });
		
		before("/message", (req, res) -> {
			User authUser = getAuthenticatedUser(req);
			if(authUser == null) {
				res.redirect("/login");
				halt();
			}
		});
		
		
		
		get("/logout", (req, res) -> {
			removeAuthenticatedUser(req);
			res.redirect("/public");
			return null;
        });
	}

	private void addAuthenticatedUser(Request request, User u) {
		request.session().attribute(USER_SESSION_ID, u);
		
	}

	private void removeAuthenticatedUser(Request request) {
		request.session().removeAttribute(USER_SESSION_ID);
		
	}

	public static User getAuthenticatedUser(Request request) {
		return request.session().attribute(USER_SESSION_ID);
	}
}
