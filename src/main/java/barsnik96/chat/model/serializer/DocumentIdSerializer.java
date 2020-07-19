package barsnik96.chat.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import barsnik96.chat.model.BaseId;

import java.io.IOException;

public class DocumentIdSerializer extends StdSerializer<BaseId> {

    public DocumentIdSerializer() {
        super(BaseId.class);
    }

    @Override
    public void serialize(BaseId value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getId());
    }
}
