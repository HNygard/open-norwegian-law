package no.law;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LawTest {

    @Test
    public void toStringTest() {
        Law law = new Law("LOV-1", "Lov om testing", "Offentleglova", LocalDate.now());
        law.chapters = new ArrayList<>();
        law.chapters.add(new Law.Chapter("Kapittel 1"));
        law.chapters.add(new Law.Chapter("Kapittel 2"));

        Law.Paragraph paragraph1 = new Law.Paragraph();
        paragraph1.name = "§ 1";
        paragraph1.title = "Første";
        paragraph1.sections = new ArrayList<>();
        paragraph1.sections.add(new Law.Section("Første ledd. Mye tekst."));
        paragraph1.sections.add(new Law.Section("Andre ledd. Mye tekst."));
        law.chapters.get(0).paragraphs = new ArrayList<>();
        law.chapters.get(0).paragraphs.add(paragraph1);

        Law.Paragraph paragraph2 = new Law.Paragraph();
        paragraph2.name = "§ 2";
        paragraph2.title = "Andre";
        paragraph2.sections = new ArrayList<>();
        paragraph2.sections.add(new Law.Section("Første ledd. Mye tekst."));
        law.chapters.get(0).paragraphs.add(paragraph2);


        Law.Paragraph paragraph3 = new Law.Paragraph();
        paragraph3.name = "§ 3";
        paragraph3.title = "Tredje";
        paragraph3.sections = new ArrayList<>();
        paragraph3.sections.add(new Law.Section("Kapittel 2, første ledd i § 3."));
        law.chapters.get(1).paragraphs = new ArrayList<>();
        law.chapters.get(1).paragraphs.add(paragraph3);

        Law.Paragraph paragraph4 = new Law.Paragraph();
        paragraph4.name = "§ 4";
        paragraph4.title = "Fjerde";
        paragraph4.sections = new ArrayList<>();
        paragraph4.sections.add(new Law.Section("Tekst tekst test.\na) Første bokstav\nb) Andre bokstav"));
        law.chapters.get(1).paragraphs.add(paragraph4);

        Assertions.assertEquals(
                "Lov om testing\n" +
                        "\n" +
                        "Kapittel 1\n" +
                        "\n" +
                        "§ 1. Første\n" +
                        "\n" +
                        "Første ledd. Mye tekst.\n" +
                        "\n" +
                        "Andre ledd. Mye tekst.\n" +
                        "\n" +
                        "§ 2. Andre\n" +
                        "\n" +
                        "Første ledd. Mye tekst.\n" +
                        "\n" +
                        "Kapittel 2\n" +
                        "\n" +
                        "§ 3. Tredje\n" +
                        "\n" +
                        "Kapittel 2, første ledd i § 3.\n" +
                        "\n" +
                        "§ 4. Fjerde\n" +
                        "\n" +
                        "Tekst tekst test.\n" +
                        "a) Første bokstav\n" +
                        "b) Andre bokstav",
                law.toString()
        );
    }

    @Test
    public void testChangeLawName() {
        assertChangeLawWrapper(
                Collections.singletonList("inkassoloven"),
                "Lov om endringer i inkassoloven"
        );
        assertChangeLawWrapper(
                Arrays.asList("opplæringslova", "privatskolelova", "folkehøyskoleloven"),
                "Lov om endringer i opplæringslova, privatskolelova og folkehøyskoleloven (leksehjelp m.m.)"
        );
        assertChangeLawWrapper(
                Collections.singletonList("lov 4. februar 1977 nr. 4 om arbeidervern og arbeidsmiljø m.v."),
                "Lov om endring i lov 4. februar 1977 nr. 4 om arbeidervern og arbeidsmiljø m.v. (arbeidsmiljøloven)"
        );
    }

    private void assertChangeLawWrapper(List<String> expectedChangeInLawNames, String lawNameOfChangeLaw) {
        Law.ChangeLawWrapper wrapper = Law.getChangeLawDetails(lawNameOfChangeLaw);
        Assertions.assertEquals(expectedChangeInLawNames, wrapper.changeInLawNames);
        Assertions.assertTrue(wrapper.changeLaw);
    }
}
