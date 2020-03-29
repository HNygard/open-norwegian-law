package no.law;

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
            writer.write(LawGson.getGson().toJson(law));
        }
    }
}
