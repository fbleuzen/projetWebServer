package server;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONObject;


@ServerEndpoint("/websocket/map")
public class MapWebSockets {

    private Session session;
    private static List<MapWebSockets> usersConnected = new ArrayList<MapWebSockets>();

    @OnOpen
    public void start(Session session) {
        System.out.println("Session connected");
        this.session = session;
        usersConnected.add(this);
        sendPositions();
    }

    @OnClose
    public void end() {
        usersConnected.remove(this);
        System.out.println("Session disconnected");
    }

    @OnMessage
    public void incoming(String message) {
        System.out.println("Message received : " + message);
        JSONObject obj = new JSONObject(message);
        UserController ctrl = UserController.getInstance();
        User user = ctrl.getUser(obj.getString("name"));
        user.setCoordinates(obj.getString("latitude"), obj.getString("longitude"));
        sendToUsers(message);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
    	
    }

    private void sendToUsers(String msg) {
        for (MapWebSockets client : usersConnected) {
        	if (client != this) {
	            try {
	                synchronized (client) {
	                    client.session.getBasicRemote().sendText(msg);
	                }
	            } catch (IOException e) {
	                usersConnected.remove(client);
	                try {
	                    client.session.close();
	                } catch (IOException e1) {
	                    // Ignore
	                }
	            }
            }
        }
    }
    
    private void sendPositions() {
		UserController ctrl = UserController.getInstance();
        List<User> users = ctrl.getUsers();
		for (User user : users) {
			JSONObject obj = new JSONObject();
			obj.put("name", user.name);
			obj.put("interests", user.interests);
			obj.put("latitude", user.latitude);
			obj.put("longitude", user.longitude);
			try {
				System.out.println("JsonData : " + obj.toString());
				this.session.getBasicRemote().sendText(obj.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
