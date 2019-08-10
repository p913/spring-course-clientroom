package ru.ptvi.otuscourse.clientroomapi.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ModelMapperConfig {
    private Converter<String, UUID> fromString2Uuid =
            ctx -> ctx.getSource() == null ? null : UUID.fromString(ctx.getSource());

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addConverter(fromString2Uuid, String.class, UUID.class);

        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }
}
