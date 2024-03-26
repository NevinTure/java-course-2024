package edu.java.scrapper.utils;

import org.jetbrains.annotations.NotNull;
import org.jooq.Converter;
import org.jooq.impl.EnumConverter;
import edu.java.models.utils.State;
import java.util.function.Function;

public class StateConverter implements Converter<State, edu.java.scrapper.model.jooq.enums.State> {

    @Override
    public edu.java.scrapper.model.jooq.enums.State from(State state) {
        return null;
    }

    @Override
    public State to(edu.java.scrapper.model.jooq.enums.State state) {
        return null;
    }

    @Override
    public @NotNull Class<State> fromType() {
        return null;
    }

    @Override
    public @NotNull Class<edu.java.scrapper.model.jooq.enums.State> toType() {
        return null;
    }
}
