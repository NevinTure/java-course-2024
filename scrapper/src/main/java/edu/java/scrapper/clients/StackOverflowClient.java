package edu.java.scrapper.clients;

import edu.java.scrapper.dtos.StackOverflowResponse;

public interface StackOverflowClient {

    StackOverflowResponse getUpdateInfo(String uri);
}
