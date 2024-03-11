package edu.java.bot.services;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.LinkUpdate;
import edu.java.models.dtos.LinkUpdateRequest;
import java.net.URI;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class
LinkUpdateServiceImpl implements LinkUpdateService {

    private final ModelMapper mapper;
    private final Bot bot;

    public LinkUpdateServiceImpl(ModelMapper mapper, Bot bot) {
        this.mapper = mapper;
        this.bot = bot;
    }

    @Override
    public void update(LinkUpdateRequest updateDto) {
        LinkUpdate update = mapper.map(updateDto, LinkUpdate.class);
        for (long id : update.getTgChatIds()) {
            sendUpdate(id, update.getUrl(), update.getDescription());
        }
    }

    private void sendUpdate(long chatId, URI url, String description) {
        String text = String.format("Новое обновление (%s)!: %s", description, url);
        SendMessage request = new SendMessage(chatId, text);
        bot.execute(request);
    }
}
