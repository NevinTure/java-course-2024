package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.TgChat;
import edu.java.bot.utils.UrlUtils;
import java.util.Objects;
import lombok.extern.java.Log;

@Log
public class TrackCommand implements ChatCommand {

    private String message;

    @Override
    public boolean handle(String text, TgChat sender) {
        if (sender.isWaitingTrack()) {
            checkUrl(text, sender);
            return true;
        } else {
            return checkTrackCommand(text, sender);
        }
    }

    @SuppressWarnings("ReturnCount")
    private void checkUrl(String url, TgChat sender) {
        if (Objects.equals(url, "/cancel")) {
            sender.setWaitingTrack(false);
            message = "Отмена.";
            return;
        }
        if (UrlUtils.isValid(url)) {
            addUrl(url, sender);
            return;
        }
        message = "Некорректная ссылка.";
    }

    private boolean checkTrackCommand(String commandStr, TgChat sender) {
        if (Objects.equals(commandStr, "/track")) {
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

    private void addUrl(String url, TgChat sender) {
        if (sender.getLinkList().contains(url)) {
            message = "Вы уже отслеживаете эту ссылку.";
        } else {
            sender.setWaitingTrack(false);
            sender.getLinkList().add(url);
            message = "Ссылка добавлена для отслеживания.";
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
