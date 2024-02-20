package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;
import edu.java.bot.utils.UrlUtils;
import java.util.Objects;
import lombok.extern.java.Log;

@Log
public class TrackCommand implements ChatCommand {

    private String message;

    @Override
    public boolean handle(String text, Person sender) {
        if (sender.isWaitingTrack()) {
            if (Objects.equals(text, "/cancel")) {
                sender.setWaitingTrack(false);
                message = "Отмена.";
            } else if (UrlUtils.isValid(text)) {
                if (sender.getLinkList().contains(text)) {
                    message = "Вы уже отслеживаете эту ссылку.";
                } else {
                    sender.setWaitingTrack(false);
                    sender.getLinkList().add(text);
                    message = "Ссылка добавлена для отслеживания.";
                }
            } else {
                message = "Некорректная ссылка.";
            }
            return true;
        } else if (Objects.equals(text, "/track")) {
            sender.setWaitingTrack(true);
            message = """
                Введите ссылку, которую хотите начать отслеживать.
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
        return "/track -- начать отслеживание ссылки";
    }
}
