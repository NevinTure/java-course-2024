package edu.java.bot.chat_command;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Person;
import edu.java.bot.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

@Slf4j
public class StartCommand implements ChatCommand {

    private String message;

    @Override
    public boolean handle(String text, Person sender) {
        if (Objects.equals(text, "/start")) {
            if (sender == null) {
                message = "Регистрация прошла успешно.";
            } else {
                message = "Вы уже зарегистрированы.";
            }
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
        return "/start -- зарегистрировать пользователя";
    }
}
