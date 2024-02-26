package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.model.State;
import edu.java.bot.model.TgChat;
import edu.java.bot.utils.UrlUtils;
import java.net.URI;
import java.util.Objects;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Log
@Component
public class TrackCommand implements ChatCommand {

    private String message;

    @Override
    public boolean checkState(State state) {
        return state.equals(State.DEFAULT) || state.equals(State.WAITING_TRACK);
    }

    @Override
    public boolean handle(String text, TgChat sender) {
        if (sender.getState().equals(State.WAITING_TRACK)) {
            checkUrl(text, sender);
            return true;
        } else {
            return checkTrackCommand(text, sender);
        }
    }

    @SuppressWarnings("ReturnCount")
    private void checkUrl(String url, TgChat sender) {
        if (Objects.equals(url, "/cancel")) {
            sender.setState(State.DEFAULT);
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
            sender.setState(State.WAITING_TRACK);
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
        Link link = new Link(0, URI.create(url));
        if (sender.getLinkList().contains(link)) {
            message = "Вы уже отслеживаете эту ссылку.";
        } else {
            sender.setState(State.WAITING_TRACK);
            sender.getLinkList().add(link);
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
