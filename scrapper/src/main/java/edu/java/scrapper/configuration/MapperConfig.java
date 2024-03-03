package edu.java.scrapper.configuration;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.TgChat;
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
        TypeMap<AddLinkRequest, Link> addReqToLink = mapper.createTypeMap(AddLinkRequest.class, Link.class);
        TypeMap<RemoveLinkRequest, Link> removeReqToLink = mapper.createTypeMap(RemoveLinkRequest.class, Link.class);
        mapper.createTypeMap(TgChat.class, ListLinksResponse.class)
            .addMappings(im -> im.map(TgChat::getLinkList, ListLinksResponse::setLinks));
        mapper.createTypeMap(Link.class, LinkResponse.class);
        Converter<String, URI> strToUrl = c -> URI.create(c.getSource());
        addReqToLink.addMappings(innerMapper -> innerMapper.using(strToUrl).map(AddLinkRequest::getUrl, Link::setUrl));
        removeReqToLink.addMappings(im -> im.using(strToUrl).map(RemoveLinkRequest::getUrl, Link::setUrl));
        return mapper;
    }
}
