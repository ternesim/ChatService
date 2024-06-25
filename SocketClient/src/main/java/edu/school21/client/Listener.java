package edu.school21.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.net.Socket;

public class Listener extends Thread {

    private final BufferedReader in;

    volatile JSONObject lastJson = new JSONObject();

    public Listener(BufferedReader in) {
        this.in = in;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                lastJson = new JSONObject(in.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getLastJson() {
        return lastJson;
    }
}