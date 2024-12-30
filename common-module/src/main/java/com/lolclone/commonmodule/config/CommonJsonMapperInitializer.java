package com.lolclone.commonmodule.config;

import io.eventuate.common.json.mapper.JSonMapper;
import jakarta.annotation.PostConstruct;

public class CommonJsonMapperInitializer {

    @PostConstruct
    public void initialize() {
        registerCommonModule();
    }

    public static void registerCommonModule() {
        JSonMapper.objectMapper.registerModule(new CommonModule());
    }
}
