package no.law;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class LawToHtml {

    public static void main(String[] args) throws IOException {
        Law law = LawRepository.getLaw("LOV-2006-05-19-16");
        String offentleglova = law.toString();
        System.out.println(offentleglova);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("./offentleglova.txt"), StandardCharsets.UTF_8))) {
            writer.write(law.toString());
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("./offentleglova.html"), StandardCharsets.UTF_8))) {
            writer.write(
                    "<head>\n"
                            + "    <meta charset=\"UTF-8\">\n"
                            + "    <link rel=\"stylesheet\" href=\"law.css\">\n"
                            + "</head>\n\n"
                            + law.toHtml()
            );
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("./offentleglova.json"), StandardCharsets.UTF_8))) {
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

            writer.write(gsonBuilder.create().toJson(law));
        }
    }
}
