package com.nicegpt.chatannotator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChatGPTApi{

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String requestChatGpt(String prompt) throws Exception {
        // 创建 URL 对象
        URL url = new URL(API_URL);

        // 打开连接并设置请求方式为 POST
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        // 设置请求头
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

        // 设置连接属性
        connection.setDoOutput(true);

        // 构建请求 JSON
        // 创建 messages 数组
        JSONArray messagesArray = new JSONArray();
        JSONObject requestBody = new JSONObject();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        messagesArray.put(message);
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messagesArray);
        System.out.println(requestBody);
        // 发送请求
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        // 读取并解析响应
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = bufferedReader.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONObject("choices").getJSONArray("text").getString(0);
        }
    }

    public static void main(String[] args) {
        try {
            String prompt = "What is the capital of France?";
            String response = requestChatGpt(prompt);
            System.out.println("ChatGPT response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
