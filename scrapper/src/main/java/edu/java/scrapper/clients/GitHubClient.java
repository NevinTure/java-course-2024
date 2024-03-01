package edu.java.scrapper.clients;

import edu.java.scrapper.dtos.GitHubResponse;

public interface GitHubClient {

    GitHubResponse getUpdateInfo(String uri);
}
