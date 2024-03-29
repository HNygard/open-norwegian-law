package no.law;

import no.law.lawreference.NorwegianLawTextName_to_LawId;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LawRepository {
    public static Map<String, Law> laws = new HashMap<>();
    private static volatile boolean lazyUpdateChangeLawIdsDone = false;

    static {
        // Parts of Offentleglova
        // Source: https://lovdata.no/dokument/LTI/lov/2006-05-19-16 (The original announcement of the law)
        Law law = new Law(
                "LOV-2006-05-19-16",
                "Lov om rett til innsyn i dokument i offentleg verksemd (offentleglova).",
                "Offentleglova",
                LocalDate.of(2006, 5, 19)
        );
        law.chapters = new ArrayList<>();
        law.chapters.add(new Law.Chapter("Kapittel 1 Innleiande føresegner"));
        law.chapters.add(new Law.Chapter("Kapittel 2 Hovudreglane om innsyn"));

        Law.Paragraph paragraph1 = new Law.Paragraph();
        paragraph1.name = "§ 1";
        paragraph1.title = "Formål";
        paragraph1.sections = new ArrayList<>();
        paragraph1.sections.add(new Law.Section("Formålet med lova er å leggje til rette for at offentleg verksemd er open og gjennomsiktig, for slik å styrkje informasjons- og ytringsfridommen, den demokratiske deltakinga, rettstryggleiken for den enkelte, tilliten til det offentlege og kontrollen frå ålmenta. Lova skal òg leggje til rette for vidarebruk av offentleg informasjon."));
        law.chapters.get(0).paragraphs = new ArrayList<>();
        law.chapters.get(0).paragraphs.add(paragraph1);

        Law.Paragraph paragraph2 = new Law.Paragraph();
        paragraph2.name = "§ 2";
        paragraph2.title = "Verkeområdet til lova";
        paragraph2.sections = new ArrayList<>();
        paragraph2.sections.add(new Law.Section("Lova gjeld for\n" +
                "a)\tstaten, fylkeskommunane og kommunane,\n" +
                "b)\tandre rettssubjekt i saker der dei gjer enkeltvedtak eller utferdar forskrift,\n" +
                "c)\tsjølvstendige rettssubjekt der stat, fylkeskommune eller kommune direkte eller indirekte har ein eigardel som gir meir enn halvparten av røystene i det øvste organet i rettssubjektet, og\n" +
                "d)\tsjølvstendige rettssubjekt der stat, fylkeskommune eller kommune direkte eller indirekte har rett til å velje meir enn halvparten av medlemmene med røysterett i det øvste organet i rettssubjektet.\n" +
                "Bokstavane c og d gjeld ikkje rettssubjekt som hovudsakleg driv næring i direkte konkurranse med og på same vilkår som private. For verksemder som etter offentleg oppkjøp eller liknande kjem inn under bokstavane c eller d, gjeld lova frå og med fjerde månadsskiftet etter den månaden da vilkåra vart oppfylte."));
        paragraph2.sections.add(new Law.Section("Kongen kan gi forskrift om at lova ikkje skal gjelde for sjølvstendige rettssubjekt eller for visse dokument hos sjølvstendige rettssubjekt som er omfatta av første ledd bokstavane c eller d, dersom det må reknast som nødvendig ut frå omsynet til arten av verksemda, konkurransesituasjonen eller andre særlege tilhøve. Det same gjeld dersom det gjeld unntak frå innsynsretten for det alt vesentlege av dokumenta til verksemda og særlege tungtvegande omsyn tilseier det. Kongen kan gi forskrift om at lova heilt eller delvis skal gjelde for sjølvstendige rettssubjekt som er knytte til stat eller kommune utan å oppfylle vilkåra i første ledd bokstav c eller d, eller som er unnatekne etter første ledd andre punktum."));
        paragraph2.sections.add(new Law.Section("Lova gjeld ikkje for Stortinget, Riksrevisjonen, Stortingets ombodsmann for forvaltninga og andre organ for Stortinget."));
        paragraph2.sections.add(new Law.Section("Lova gjeld ikkje for gjeremål som domstolane har etter rettsstellovene. Lova gjeld heller ikkje for gjeremål som andre organ har etter rettsstellovene i eigenskap av rettsstellorgan. Lova gjeld dessutan ikkje for gjeremål som politiet og påtalemakta har etter straffeprosessloven. Kongen kan gi forskrifter om kva lover som skal reknast som rettsstellover, og om at enkelte gjeremål etter rettsstellovene likevel skal vere omfatta av lova."));
        paragraph2.sections.add(new Law.Section("Lova gjeld for Svalbard dersom ikkje anna blir fastsett av Kongen."));
        paragraph2.sections.add(new Law.Section("Føresegnene i § 6, § 7 andre ledd, § 8 tredje ledd andre punktum og fjerde og femte ledd og § 30 første ledd tredje punktum og andre ledd gjeld uavhengig av føresegnene i paragrafen her for alle verksemder som er omfatta av EØS-avtalen vedlegg XI nr. 5k (direktiv 2003/98/EF) om vidarebruk av informasjon frå offentleg sektor."));
        law.chapters.get(0).paragraphs.add(paragraph2);


        Law.Paragraph paragraph3 = new Law.Paragraph();
        paragraph3.name = "§ 3";
        paragraph3.title = "Hovudregel";
        paragraph3.sections = new ArrayList<>();
        paragraph3.sections.add(new Law.Section("Saksdokument, journalar og liknande register for organet er opne for innsyn dersom ikkje anna følgjer av lov eller forskrift med heimel i lov. Alle kan krevje innsyn i saksdokument, journalar og liknande register til organet hos vedkommande organ."));
        law.chapters.get(1).paragraphs = new ArrayList<>();
        law.chapters.get(1).paragraphs.add(paragraph3);

        //laws.put(law.getLawId(), law);

        law = new Law("LOV-1970-06-19-69",
                "Lov om offentlighet i forvaltningen (offentlighetsloven)",
                "Offentlighetsloven",
                Collections.singleton("Offentleglova"),
                LocalDate.of(1970, 6, 19)
        );
        law.chapters = Collections.emptyList();
        // Adding this law manually for tests that check on date
        laws.put(law.getLawId(), law);


        // :: Read laws from JSON files i resources/laws
        List<Law> lawList = new ArrayList<>();
        try {
            String scannedPackage = "laws/**";
            PathMatchingResourcePatternResolver scanner = new PathMatchingResourcePatternResolver();
            Resource[] resources = scanner.getResources(scannedPackage);

            for (Resource resource : resources) {
                if (!resource.getFilename().endsWith(".json")) {
                    continue;
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                StringBuffer lines = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    lines.append(line);
                }
                bufferedReader.close();
                lawList.add(LawGson.getGson().fromJson(lines.toString(), Law.class));
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to read laws from resources: " + e.getMessage(), e);
        }

        Map<String, Law> collect = lawList.stream()
                .collect(Collectors.toMap(
                        Law::getLawId,
                        f -> {
                            Collection<String> otherNames = (f.getPossibleNamesForLaw() != null) ? f.getPossibleNamesForLaw() : new ArrayList<>();
                            Law law1 = new Law(f.getLawId(), f.getFullName(), f.getShortName(), otherNames, f.getAnnounementDate());
                            law1.chapters = f.chapters;
                            return law1;
                        }
                ));

        laws.putAll(collect);
    }

    private static void lazyUpdateChangeLawIds() {
        lazyUpdateChangeLawIdsDone = true;
        laws.values().forEach(lawInstance -> {
            if (lawInstance.getChangeInLawNames() != null) {
                lawInstance.getChangeInLawNames().forEach(name -> {
                    String lawId = NorwegianLawTextName_to_LawId.law(name, lawInstance.getAnnounementDate());
                    if (lawId == null) {
                        lawInstance.addDebugInformation("[ERROR] Not able to find law id for law name [" + name + "]. This law was changing that law.");
                        return;
                    }
                    lawInstance.addChangeInLawId(lawId);

                    // E: Found a law id. Might have a law.
                    Law law = getLaw(lawId);
                    if (law != null) {
                        // -> Found the law in registry, so put a reverse reference in place reference.
                        law.addChangeLaw(lawInstance);
                    }
                    else {
                        lawInstance.addDebugInformation("[ERROR] Not able to find law with law id [" + lawId + "] (law name [" + name + "]). This law was changing that law.");
                    }
                });
            }
        });
    }

    public static Law getLaw(String lawId) {
        return laws.get(lawId);
    }

    public static Collection<Law> getLaws() {
        if (!lazyUpdateChangeLawIdsDone) {
            lazyUpdateChangeLawIds();
        }

        return laws.values();
    }
}
