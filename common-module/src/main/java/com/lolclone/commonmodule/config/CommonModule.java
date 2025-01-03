package com.lolclone.commonmodule.config;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class CommonModule extends SimpleModule {
    private static final String MODULE_NAME = "CommonModule";

    class UUIDSerializer extends StdScalarSerializer<UUID> {
        public UUIDSerializer() {
            super(UUID.class);
        }

        @Override
        public void serialize(UUID value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(value.toString());
            }
        }
    }
    
    class UUIDDeserializer extends StdScalarDeserializer<UUID> {
        public UUIDDeserializer() {
            super(UUID.class);
        }

        @Override
        public UUID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken token = p.getCurrentToken();
            if (token == JsonToken.VALUE_STRING) {
                String str = p.getText().trim();
                return str.isEmpty() ? null : UUID.fromString(str);
            }
            return (UUID) ctxt.handleUnexpectedToken(handledType(), p.getCurrentToken(), p,
                    "Expected String value for UUID");
        }
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    public CommonModule() {
        addSerializer(UUID.class, new UUIDSerializer());
        addDeserializer(UUID.class, new UUIDDeserializer());
    }
}
