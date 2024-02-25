package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.TgChat;
import edu.java.bot.utils.UrlUtils;
import java.util.Objects;
import lombok.extern.java.Log;

@Log
public class UntrackCommand implements ChatCommand {

    private String message;

    @Override
    public boolean handle(String text, TgChat sender) {
        if (sender.isWaitingUntrack()) {
            checkUrl(text, sender);
            return true;
        } else {
            return checkUntrackCommand(text, sender);
        }
    }

    @SuppressWarnings("ReturnCount")
    private void checkUrl(String url, TgChat sender) {
        if (Objects.equals(url, "/cancel")) {
            message = "Отмена.";
            sender.setWaitingUntrack(false);
            return;
        }
        if (UrlUtils.isValid(url)) {
            removeUrl(url, sender);
            return;
        }
        message = "Некорректная ссылка.";
    }

    private boolean checkUntrackCommand(String commandStr, TgChat sender) {
        if (Objects.equals(commandStr, "/untrack")) {
            sender.setWaitingUntrack(true);
            message = """
                Введите ссылку, которую хотите прекратить отслеживать.
                Введите /cancel чтобы отменить действие.
                """;
            return true;
        } else {
            return false;
        }
    }

    private void removeUrl(String url, TgChat sender) {
        sender.setWaitingUntrack(false);
        sender.getLinkList().remove(url);
        message = "Отслеживание ссылки прекращено.";
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
