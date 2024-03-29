package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Link;
import edu.java.bot.model.TgChat;
import edu.java.bot.services.ChatService;
import edu.java.bot.utils.UrlUtils;
import edu.java.models.utils.State;
import java.net.URI;
import java.util.Objects;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Log
@Component
public class UntrackCommand implements ChatCommand {

    private String message;
    private final ChatService chatService;

    public UntrackCommand(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public boolean checkState(State state) {
        return state.equals(State.DEFAULT) || state.equals(State.WAITING_UNTRACK);
    }

    @Override
    public boolean handle(String text, TgChat sender) {
        if (sender.getState().equals(State.WAITING_UNTRACK)) {
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
            sender.setState(State.DEFAULT);
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
            sender.setState(State.WAITING_UNTRACK);
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
        Link link = new Link(0, URI.create(url));
        sender.setState(State.DEFAULT);
        chatService.deleteLinkByChatId(sender.getId(), link);
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
