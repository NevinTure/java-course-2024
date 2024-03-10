package edu.java.scrapper.clients;

import edu.java.scrapper.dtos.StackOverflowResponse;
import java.util.List;

public interface StackOverflowClient {

    StackOverflowResponse getUpdateInfo(List<String> urns);
}
