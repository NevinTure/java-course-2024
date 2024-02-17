package edu.java.clients;

import edu.java.dtos.StackOverflowResponse;

public interface StackOverflowClient {

    StackOverflowResponse getUpdateInfo(String uri);
}
