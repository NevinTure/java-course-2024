package edu.java.scrapper.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.metrics.Stat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TgChat {

    private Long id;
    private State state;
    private List<GitLink> gitLinks;
    private List<StackOverFlowLink> stackOverFlowLinks;
    private List<Link> linkList;

    public TgChat(Long id) {
        this.id = id;
        gitLinks = new ArrayList<>();
        stackOverFlowLinks = new ArrayList<>();
        linkList = new ArrayList<>();
    }
}
