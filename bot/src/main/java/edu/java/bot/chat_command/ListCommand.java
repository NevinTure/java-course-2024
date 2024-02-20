package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;
import edu.java.bot.service.ChatService;
import java.util.List;
import java.util.Objects;
import lombok.extern.java.Log;

@Log
public class ListCommand implements ChatCommand {

    private List<String> linkList;

    @Override
    public boolean handle(String text, Person sender) {
        if (Objects.equals(text, "/list")) {
            linkList = sender.getLinkList();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SendMessage getMessage(long receiverId) {
        if (linkList.isEmpty()) {
            return new SendMessage(receiverId,
                "Вы пока не отслеживаете ни одной ссылки.");
        } else {
            return new SendMessage(receiverId,
                "Отслеживаемые ссылки:\n" + String.join("\n", linkList));
        }
    }

    @Override
    public String getDescription() {
        return "/list -- показать список отслеживаемых ссылок";
    }
}
