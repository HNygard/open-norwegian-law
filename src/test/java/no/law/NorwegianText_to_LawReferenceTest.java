package no.law;

import no.law.lawreference.LawReferenceFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static no.law.lawreference.NorwegianText_to_LawReference.textToLawReference;

public class NorwegianText_to_LawReferenceTest {
    @Test
    void refTest() {
        LawReferenceFinder lawRef;
        LocalDate date = LocalDate.of(2010, 1, 1);
        LocalDate date2 = LocalDate.of(2000, 1, 1);

        Assertions.assertEquals(
                new LawReferenceFinder(),
                textToLawReference("", date)
        );

        // :: Basic law name
        lawRef = new LawReferenceFinder("LOV-2006-05-19-16");
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006)", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) some other word", date));
        Assertions.assertEquals(lawRef, textToLawReference("LOV-2006-05-19-16", date));

        // :: Search done back in time (01.01.2000) should match old law
        lawRef = new LawReferenceFinder("LOV-1970-06-19-69");
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova", date2));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova §", date2));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova § abc", date2));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova some other word", date2));

        // :: Specific old law name should match the old law
        lawRef = new LawReferenceFinder("LOV-1970-06-19-69");
        Assertions.assertEquals(lawRef, textToLawReference("Offentlighetsloven (1970)", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (1970)", date));

        // :: Law name and paragraph
        lawRef = new LawReferenceFinder("LOV-2006-05-19-16", "§ 1");
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) §1", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 1", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) paragraf1", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) paragraf 1", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 1 some other word", date));

        // :: Law name, paragraph and section
        lawRef = new LawReferenceFinder("LOV-2006-05-19-16", "§ 2", "første");
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 2 første ledd", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 2 første ledd some other word", date));

        // :: Law name, paragraph, section and sentence
        lawRef = new LawReferenceFinder("LOV-2006-05-19-16", "§ 1", "første", "andre");
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 1 første ledd andre punktum", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 1 første ledd andre punktum some other word", date));

        // :: Law name, paragraph, letter
        // https://www.sprakradet.no/sprakhjelp/Skriveregler/Lovhenvisninger/:
        // Når vi viser til et bokstavpunkt i en oppramsing i en paragraf, skal ordet bokstav være med:
        //     § 126 bokstav a
        lawRef = LawReferenceFinder.withLetter("LOV-2006-05-19-16", "§ 126", null, "a");
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 126 bokstav a", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 126 bokstav a some other word", date));

        // :: Law name, paragraph, section, letter
        // https://www.sprakradet.no/sprakhjelp/Skriveregler/Lovhenvisninger/:
        // Merk: En «§ 126 bokstav a» er en paragraf med bare ett ledd. Når paragrafen er delt i flere ledd, slik de fleste er, må nummeret på leddet være med:
        //     § 126 første ledd bokstav a
        lawRef = LawReferenceFinder.withLetter("LOV-2006-05-19-16", "§ 126", "første", "a");
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 126 første ledd bokstav a", date));
        Assertions.assertEquals(lawRef, textToLawReference("Offentleglova (2006) § 126 første ledd bokstav a some other word", date));
    }
}
