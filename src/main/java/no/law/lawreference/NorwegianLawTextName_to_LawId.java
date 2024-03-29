package no.law.lawreference;

import no.law.Law;
import no.law.LawRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NorwegianLawTextName_to_LawId {
    // lov 20. juni 2014 nr. 49
    static String lawIdInTextRegex = "^lov ([0-9]*)\\. ([a-zA-Z]*) ([0-9]{4}) nr\\. ([0-9]*)";
    static Pattern lawIdInTextPattern = Pattern.compile(lawIdInTextRegex + "$");

    // lov 6. desember 2002 nr. 72 om folkehøyskoler
    static Pattern lawIdInTextWithChecksumOnName = Pattern.compile(lawIdInTextRegex + " om ([A-Za-zÆØÅæøå\\-, ]*)$");

    private static Map<String, Integer> MONTHS = new HashMap<>();

    static {
        MONTHS.put("januar", 1);
        MONTHS.put("februar", 2);
        MONTHS.put("mars", 3);
        MONTHS.put("april", 4);
        MONTHS.put("mai", 5);
        MONTHS.put("juni", 6);
        MONTHS.put("juli", 7);
        MONTHS.put("august", 8);
        MONTHS.put("september", 9);
        MONTHS.put("oktober", 10);
        MONTHS.put("november", 11);
        MONTHS.put("desember", 12);
    }

    public static String law(String norwegianLawTextName) {
        return law(norwegianLawTextName, LocalDate.now());
    }

    public static String law(String norwegianLawTextName, LocalDate currentDate) {
        Matcher matcher = lawIdInTextPattern.matcher(norwegianLawTextName);
        if (!matcher.matches()) {
            matcher = lawIdInTextWithChecksumOnName.matcher(norwegianLawTextName);
        }
        if (matcher.matches()) {
            int month = MONTHS.get(matcher.group(2));
            String date = matcher.group(1);
            return "LOV-" + matcher.group(3)
                    + "-" + (month > 9 ? month : "0" + month)
                    + "-" + (date.length() == 1 ? "0" : "") + date
                    + "-" + matcher.group(4);
        }

        // Sort so that we find the first law
        return LawRepository.getLaws().stream()
                // Remove any laws that are not relevant
                .filter(law -> law.getAnnounementDate().isBefore(currentDate))
                // Must match on name
                .filter(law ->
                        law.getPossibleNamesForLaw().stream()
                                .anyMatch(name -> name.equalsIgnoreCase(norwegianLawTextName))
                )
                // Get the last law
                .max(Comparator.comparing(Law::getAnnounementDate))
                .map(Law::getLawId)
                .orElse(null);
    }
}
