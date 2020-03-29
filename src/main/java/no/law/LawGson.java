package no.law;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LawGson {
    private static Gson gson;
    static {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(Law.Section.class, new TypeAdapter<Law.Section>() {
            @Override
            public void write(JsonWriter out, Law.Section value) throws IOException {
                out.value(value.text);
            }

            @Override
            public Law.Section read(JsonReader in) throws IOException {
                return new Law.Section(in.nextString());
            }
        });
        gsonBuilder.registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {

            @Override
            public void write(JsonWriter out, LocalDate value) throws IOException {
                out.value(value.format(DateTimeFormatter.ISO_DATE));
            }

            @Override
            public LocalDate read(JsonReader in) throws IOException {
                return LocalDate.parse(in.nextString(), DateTimeFormatter.ISO_DATE);
            }
        });
        gson = gsonBuilder.create();
    }

    public static Gson getGson() {
        return gson;
    }
}
