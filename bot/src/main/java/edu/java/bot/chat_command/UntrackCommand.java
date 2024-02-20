package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;
import edu.java.bot.service.ChatService;
import edu.java.bot.utils.UrlUtils;
import jakarta.websocket.OnClose;
import lombok.extern.java.Log;
import java.util.Objects;

@Log
public class UntrackCommand implements ChatCommand {

    private String message;

    @Override
    public boolean handle(String text, Person sender) {
        if (sender.isWaitingUntrack()) {
            if (Objects.equals(text, "/cancel")) {
                message = "Отмена.";
                sender.setWaitingUntrack(false);
            } else if (UrlUtils.isValid(text)) {
                sender.setWaitingUntrack(false);
                sender.getLinkList().remove(text);
                message = "Отслеживание ссылки прекращено.";
            } else {
                message = "Некорректная ссылка.";
            }
            return true;
        } else if (Objects.equals(text, "/untrack")) {
            sender.setWaitingUntrack(true);
            message = """
                Введите ссылку, которую хотите перестать отслеживать.
                Введите /cancel чтобы отменить действие.
                """;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SendMessage getMessage(long receiverId) {
        return new SendMessage(receiverId, message);
    }

    @Override
    public String getDescription() {
        return "/untrack -- прекратить отслеживание ссылки";
    }
}
