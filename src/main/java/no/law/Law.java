package no.law;

import no.law.lawreference.LawReferenceFinder;
import no.law.lawreference.NorwegianNumbers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class holds the main text of a law. Plain text.
 */
public class Law implements LawReference {
    private final String lawId;
    private final String lawName;
    private final String shortName;
    private final LocalDate announcementDate;
    private final Collection<String> allPossibleNamesForLaw;
    List<Chapter> chapters;
    private boolean changeLaw;
    private final List<String> changeInLawNames;
    private Collection<String> changeInLawIds = new ArrayList<>();
    private Collection<Law> thisLawChangedBy = new ArrayList<>();
    private Collection<String> debugInformation = new ArrayList<>();

    public Law(String lawId, String lawName, String shortName, LocalDate announcementDate) {
        this(lawId, lawName, shortName, new ArrayList<>(), announcementDate);
    }

    public Law(String lawId, String lawName, String shortName, Collection<String> otherNames, LocalDate announcementDate) {
        // Trim . at the end
        if (lawName.endsWith(".")) {
            lawName = lawName.substring(0, lawName.length() - 1);
        }


        if (announcementDate == null) {
            Pattern pattern = Pattern.compile("^LOV-([0-9]{4})-([0-9]{2})-([0-9]{2})-[0-9]*$");
            Matcher matcher = pattern.matcher(lawId);
            matcher.matches();
            announcementDate = LocalDate.of(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3))
            );
        }

        this.lawId = lawId;
        this.lawName = lawName;
        this.shortName = shortName;
        this.announcementDate = announcementDate;

        allPossibleNamesForLaw = new ArrayList<>();
        allPossibleNamesForLaw.add(lawId);
        allPossibleNamesForLaw.add(lawName);
        allPossibleNamesForLaw.add(shortName);
        allPossibleNamesForLaw.add(shortName + " (" + this.announcementDate.getYear() + ")");
        otherNames.forEach(name -> {
            allPossibleNamesForLaw.add(name);
            allPossibleNamesForLaw.add(name + " (" + this.announcementDate.getYear() + ")");
        });

        // Lov om rett til innsyn i dokument i offentleg verksemd (offentleglova).
        // => rett til innsyn i dokument i offentleg verksemd
        allPossibleNamesForLaw.add(
                this.lawName
                        .replace("Lov om ", "")
                        .replace(" (" + shortName + ")", "")
                        .replace(" (" + shortName.toLowerCase() + ")", "")
        );

        // Analyze law name
        ChangeLawWrapper details = getChangeLawDetails(lawName);
        changeLaw = details.changeLaw;
        changeInLawNames = details.changeInLawNames;
    }

    public static ChangeLawWrapper getChangeLawDetails(String lawName) {
        boolean changeLaw = lawName.toLowerCase().startsWith("lov om endring");
        List<String> changeInLawNames = null;
        if (changeLaw) {
            String changeIn = lawName;
            // :: Strip the first bit
            // Lov om endringer i inkassoloven
            // => inkassoloven
            if (changeIn.toLowerCase().startsWith("lov om endringer i ")) {
                changeIn = changeIn.substring("lov om endringer i ".length());
            }
            if (changeIn.toLowerCase().startsWith("lov om endringar i ")) {
                changeIn = changeIn.substring("lov om endringar i ".length());
            }
            if (changeIn.toLowerCase().startsWith("lov om endring i ")) {
                changeIn = changeIn.substring("lov om endring i ".length());
            }
            if (changeIn.toLowerCase().startsWith("lov om endring")) {
                changeIn = changeIn.substring("lov om endring".length());
            }
            changeIn = changeIn.trim();

            // :: Chop of on the end
            // lov 4. februar 1977 nr. 4 om arbeidervern og arbeidsmiljø m.v. (arbeidsmiljøloven)
            // => lov 4. februar 1977 nr. 4 om arbeidervern og arbeidsmiljø
            if (changeIn.contains("mv. (")) {
                changeIn = changeIn.substring(0, changeIn.indexOf("mv. ("));
            }
            if (changeIn.contains(" (")) {
                changeIn = changeIn.substring(0, changeIn.indexOf(" ("));
            }


            // :: Match multiple laws names in the string
            // This is done with different relative easy regexes to keep this code as simple as possible to understand.
            Matcher matcher;
            String regexLawName = "(([A-Za-zæøåÆØÅ0-9 ]*)lov(a|en))";

            // 1, 2 og 3
            matcher = Pattern.compile("^" + regexLawName + ", " + regexLawName + " og " + regexLawName + "$").matcher(changeIn);
            if (matcher.matches()) {
                changeInLawNames = Arrays.asList(
                        matcher.group(1).trim(),
                        matcher.group(4).trim(),
                        matcher.group(7).trim()
                );
            }

            if (changeInLawNames == null) {

                changeInLawNames = Collections.singletonList(changeIn);
            }


        }
        return new ChangeLawWrapper(changeLaw, changeInLawNames);
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return lawName;
    }

    public LocalDate getAnnounementDate() {
        return announcementDate;
    }

    public String getLawId() {
        return lawId;
    }

    public Collection<String> getPossibleNamesForLaw() {
        return allPossibleNamesForLaw;
    }

    /**
     * This law is mainly a change to another law and not the start of a new law.
     */
    public boolean isChangeLaw() {
        return changeLaw;
    }

    public List<String> getChangeInLawNames() {
        return changeInLawNames;
    }

    public void addChangeInLawId(String changeInLawId) {
        this.changeInLawIds.add(changeInLawId);
    }

    public Collection<String> getChangeInLawIds() {
        return changeInLawIds;
    }

    public void addDebugInformation(String logLine) {
        this.debugInformation.add(logLine);
    }

    /**
     * List of all laws that changed this law.
     */
    public Collection<Law> getThisLawChangedBy() {
        return thisLawChangedBy;
    }

    public Collection<String> getDebugInformation() {
        return debugInformation;
    }

    public String toString() {
        return lawName + "\n\n"
                + chapters.stream()
                .map(Chapter::toString)
                .collect(Collectors.joining("\n\n"));
    }

    public String toHtml() {
        return
                (
                    "<div class=\"law\">\n"
                            + addIntent4spaces(
                                "<h1 class=\"law-name\">" + lawName + "</h1>\n\n"
                                + chapters.stream()
                                    .map(Chapter::toHtml)
                                    .collect(Collectors.joining("\n\n"))
                            )
                            + "\n</div>"
                )
                // Remove all white space only lines
                .replaceAll("\\n\\s*\\n", "\n\n");
    }

    @Override
    public boolean isMatchinLawRef(LawReferenceFinder lawRef) {
        return this == lawRef.getLaw();
    }

    @Override
    public List<? extends LawReference> getMatchingLawRef(LawReferenceFinder lawRef) {
        return getMatching(lawRef, chapters, this);
    }

    public void addChangeLaw(Law lawInstance) {
        thisLawChangedBy.add(lawInstance);
    }

    public static class Chapter implements LawReference {
        String name;
        List<Paragraph> paragraphs;

        public Chapter(String name) {
            this.name = name;
        }

        public String toString() {
            return name + "\n\n" + paragraphs.stream()
                    .map(Paragraph::toString)
                    .collect(Collectors.joining("\n\n"));
        }

        public String toHtml() {
            return
                    "<div class=\"law-chapter\">\n"
                            + addIntent4spaces(
                                    "<h2 class=\"law-chapter-name\">" + name + "</h2>\n\n"
                                    + "<div class=\"law-chapter-paragraphs\">\n"
                                    + addIntent4spaces(
                                        paragraphs.stream()
                                                .map(Paragraph::toHtml)
                                                .collect(Collectors.joining("\n\n"))
                                    )
                                    + "\n</div>"
                            )
                            + "\n</div>";
        }

        @Override
        public boolean isMatchinLawRef(LawReferenceFinder lawRef) {
            // TODO: implement
            return false;
        }

        @Override
        public List<? extends LawReference> getMatchingLawRef(LawReferenceFinder lawRef) {
            List<Paragraph> paragraphs = this.paragraphs;
            if (lawRef.getParagraphRef() != null) {
                // Filter Parapraphs so that only matching ParagraphRef is consided
                paragraphs = paragraphs.stream()
                        .filter(p -> p.isMatchinLawRef(lawRef))
                        .collect(Collectors.toList());
            }

            return getMatching(lawRef, paragraphs, this);
        }
    }

    public static class Paragraph implements LawReference {
        String name;
        String title;
        List<Section> sections;

        public String toString() {
            return name + ". " + title + "\n\n" + sections.stream()
                    .map(Section::toString)
                    .collect(Collectors.joining("\n\n"));
        }

        public String toHtml() {
            return "<div class=\"law-chapter-paragraph\">\n"
                    + addIntent4spaces(
                    "<h3 class=\"law-chapter-paragraph-name\">" + name + ". " + title + "</h3>\n\n"
                            + "<div class=\"law-chapter-paragraph-sections\">\n"
                            + addIntent4spaces(
                            sections.stream()
                                    .map(Section::toHtml)
                                    .collect(Collectors.joining("\n\n"))
                            )
                            + "\n</div>"
                    )
                    + "\n</div>";
        }

        @Override
        public boolean isMatchinLawRef(LawReferenceFinder lawRef) {
            if (lawRef.getParagraphRef() != null) {
                return name.equals(lawRef.getParagraphRef());
            }
            return false;
        }

        @Override
        public List<? extends LawReference> getMatchingLawRef(LawReferenceFinder lawRef) {
            if (lawRef.getSectionRef() != null) {
                int sectionNum = NorwegianNumbers.nameToNumber.get(lawRef.getSectionRef());
                if (sections.size() >= sectionNum) {
                    return sections.get(sectionNum - 1).getMatchingLawRef(lawRef);
                }
            }
            if (isMatchinLawRef(lawRef)) {
                return Collections.singletonList(this);
            }
            return Collections.emptyList();
        }
    }

    public static class Section implements LawReference {
        String text;
        List<Sentence> sentences;

        public Section(String text) {
            this.text = text;

            this.sentences = new ArrayList<>();

            List<String> strings = Arrays.asList(text.split("\n"));
            for(String line : strings) {
                Pattern numberOrLetterPattern = Pattern.compile("^([a-zA-Z0-9])\\)\t");
                Matcher matcher = numberOrLetterPattern.matcher(line);
                if (matcher.find()) {
                    this.sentences.add(new NumberedSentence(matcher.group(1), line.substring("a) ".length())));
                }
                else {
                    this.sentences.addAll(Stream.of(line.split("\\."))
                            .map(sentence -> sentence.trim() + ".")
                            .map(Sentence::new)
                            .collect(Collectors.toList()));
                }
            }

        }

        public String toString() {
            return text;
        }

        public String toHtml() {
            return "<div class=\"law-chapter-paragraph-section\">" +
                    sentences.stream()
                            .map(Sentence::toHtml)
                            .collect(Collectors.joining("<br>\n"))
                    + "</div>";
        }

        @Override
        public boolean isMatchinLawRef(LawReferenceFinder lawRef) {
            // Matching on Paragraph level
            return false;
        }

        @Override
        public List<? extends LawReference> getMatchingLawRef(LawReferenceFinder lawRef) {
            if (lawRef.getSentenceRef() != null) {
                int ref = NorwegianNumbers.nameToNumber.get(lawRef.getSentenceRef()) - 1;
                if (sentences.get(ref) != null) {
                    return Collections.singletonList(sentences.get(ref));
                }
            }
            if (lawRef.getLetterRef() != null) {
                return sentences.stream()
                        .filter(s -> {
                            if (!(s instanceof NumberedSentence)) {
                                return false;
                            }
                            return ((NumberedSentence) s).numberOrLetter.equals(lawRef.getLetterRef());
                        })
                        .collect(Collectors.toList());
            }

            return Collections.singletonList(this);
        }
    }

    public static class Sentence implements LawReference {
        private final String text;

        public Sentence(String text) {
            this.text = text;
        }

        public String toString() {
            return text;
        }

        @Override
        public String toHtml() {
            return "<span class=\"law-chapter-paragraph-section-sentence\">" + text + "</span>";
        }

        @Override
        public boolean isMatchinLawRef(LawReferenceFinder lawRef) {
            throw new RuntimeException("Not implemented.");
        }

        @Override
        public List<? extends LawReference> getMatchingLawRef(LawReferenceFinder lawRef) {
            throw new RuntimeException("Not implemented.");
        }
    }

    public static class NumberedSentence extends Sentence {
        private final String numberOrLetter;
        private final String text;

        public NumberedSentence(String numberOrLetter, String text) {
            super(text);
            this.numberOrLetter = numberOrLetter;
            this.text = text;
        }

        public String toString() {
            return numberOrLetter + ")\t" + text;
        }

        @Override
        public String toHtml() {
            return "<span class=\"law-chapter-paragraph-section-numbered-sentence\">"
                    + "<span class=\"law-chapter-paragraph-section-numbered-sentence-letter-or-number\">"
                    + numberOrLetter + ")"
                    + "</span> "
                    + text
                    + "</span>";
        }

        @Override
        public boolean isMatchinLawRef(LawReferenceFinder lawRef) {
            throw new RuntimeException("Not implemented.");
        }

        @Override
        public List<? extends LawReference> getMatchingLawRef(LawReferenceFinder lawRef) {
            throw new RuntimeException("Not implemented.");
        }
    }

    private static String addIntent4spaces(String input) {
        return "    " + input.replaceAll("\n", "\n    ");
    }

    /**
     * Return all subParts matching lawRef. If non is found, check if currentPart is matching.
     */
    private static List<? extends LawReference> getMatching(
            LawReferenceFinder lawRef,
            Collection<? extends LawReference> subParts,
            LawReference currentPart) {
        List<LawReference> matches = subParts.stream()
                .map(part -> part.getMatchingLawRef(lawRef))
                .filter(subPartsMatches -> !subPartsMatches.isEmpty())
                .flatMap(List::stream)
                .collect(Collectors.toList());
        if (matches.isEmpty()) {
            if (currentPart.isMatchinLawRef(lawRef)) {
                return Collections.singletonList(currentPart);
            }
        }
        return matches;
    }

    static class ChangeLawWrapper {
        final boolean changeLaw;
        final List<String> changeInLawNames;

        public ChangeLawWrapper(boolean changeLaw, List<String> changeInLawNames) {
            this.changeLaw = changeLaw;
            this.changeInLawNames = changeInLawNames;
        }
    }
}
