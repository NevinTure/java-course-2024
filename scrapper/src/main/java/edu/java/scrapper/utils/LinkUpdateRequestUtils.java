package edu.java.scrapper.utils;

import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.scrapper.model.Link;
import java.util.List;

public class LinkUpdateRequestUtils {

    private LinkUpdateRequestUtils() {
    }

    public static LinkUpdateRequest createRequest(Link link, List<Long> tgChatIds, UpdateType updateType) {
        LinkUpdateRequest request = new LinkUpdateRequest();
        request.setId(link.getId());
        request.setUrl(link.getUrl().toString());
        request.setTgChatIds(tgChatIds);
        request.setDescription(getDescription(updateType));
        return request;
    }

    private static String getDescription(UpdateType updateType) {
        return switch (updateType) {
            case UPDATE -> "Обновление";
            case ANSWER -> "Новый ответ";
            case PUSH -> "Новый коммит";
            default -> "";
        };
    }
}
