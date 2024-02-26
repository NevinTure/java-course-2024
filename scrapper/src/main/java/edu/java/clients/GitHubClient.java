package edu.java.clients;

import edu.java.dtos.GitHubResponse;

public interface GitHubClient {

    GitHubResponse getUpdateInfo(String uri);
}
