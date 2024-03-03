package edu.java.bot.configuration;

import edu.java.bot.model.LinkUpdate;
import edu.java.models.dtos.LinkUpdateRequest;
import java.net.URI;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        TypeMap<LinkUpdateRequest, LinkUpdate> fromLinkUpdateDto
            = mapper.createTypeMap(LinkUpdateRequest.class, LinkUpdate.class);
        Converter<String, URI> stringToURI = c -> URI.create(c.getSource());
        fromLinkUpdateDto
            .addMappings(im -> im.using(stringToURI).map(LinkUpdateRequest::getUrl, LinkUpdate::setUrl));
        return mapper;
    }
}
