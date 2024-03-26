package edu.java.scrapper.clients;

import edu.java.scrapper.dtos.GitHubResponse;
import java.util.List;

public interface GitHubClient {

    List<GitHubResponse> getUpdateInfo(String uri);
}
