package no.law.lawreference;

import no.law.Law;
import no.law.LawRepository;

import java.time.LocalDate;
import java.util.Objects;

/**
 * This class holds a reference to a law or a part of a law.
 */
public class LawReferenceFinder {
    private Law law;
    private String paragraphRef;
    private String sectionRef;
    private String letterRef;
    private String sentenceRef;

    public LawReferenceFinder() {
    }

    public LawReferenceFinder(String lawId) {
        law = LawRepository.getLaw(lawId);
    }

    public LawReferenceFinder(String lawId, String paragraph) {
        this(lawId);
        paragraphRef = paragraph;
    }

    public LawReferenceFinder(String lawId, String paragraph, String section) {
        this(lawId);
        paragraphRef = paragraph;
        sectionRef = section;
    }

    public LawReferenceFinder(String lawId, String paragraph, String section, String sentence) {
        this(lawId, paragraph, section);
        this.sentenceRef = sentence;
    }

    public LawReferenceFinder(String lawId, String paragraph, String section, String sentence, String letter) {
        this(lawId, paragraph, section, sentence);
        this.letterRef = letter;
    }

    public static LawReferenceFinder withLetter(String lawId, String paragraph, String section, String letter) {
        return new LawReferenceFinder(lawId, paragraph, section, null, letter);
    }

    public void law(String rawLawId, LocalDate date, String name) {
        String checkedLawId = NorwegianLawTextName_to_LawId.law(rawLawId, date);
        if (checkedLawId == null) {
            throw new LawNotFoundException_LawIdInvalid("Could not find law id/name [" + rawLawId + "] at the time [" + date + "].");
        }

        Law law = LawRepository.getLaw(checkedLawId);
        if (law == null) {
            throw new LawNotFoundException_LawIdNotFound("The law [" + checkedLawId + "] was not found.");
        }

        if (name != null && !law.getPossibleNamesForLaw().contains(name)) {
            throw new LawNotFoundException_ControlNameDoesNotMatch(
                    "The law [" + checkedLawId + "] does not have the name or subject [" + name + "].\n" +
                            "Names/subjects of the law:\n" +
                            String.join("\n", law.getPossibleNamesForLaw())
            );
        }

        this.law = law;
    }

    public Law getLaw() {
        return law;
    }

    public String getParagraphRef() {
        return paragraphRef;
    }

    public void addParagraph(String paragraphRef) {
        this.paragraphRef = paragraphRef;
    }

    public String getSectionRef() {
        return sectionRef;
    }

    public void addSection(String sectionRef) {
        this.sectionRef = sectionRef;
    }

    public String getLetterRef() {
        return letterRef;
    }

    public void addLetter(String letterRef) {
        this.letterRef = letterRef;
    }

    public String getSentenceRef() {
        return sentenceRef;
    }

    public void addSentence(String sentenceRef) {
        this.sentenceRef = sentenceRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LawReferenceFinder that = (LawReferenceFinder) o;
        return Objects.equals(law, that.law)
                && Objects.equals(paragraphRef, that.paragraphRef)
                && Objects.equals(sectionRef, that.sectionRef)
                && Objects.equals(sentenceRef, that.sentenceRef)
                && Objects.equals(letterRef, that.letterRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(law, paragraphRef, sectionRef, sentenceRef);
    }

    public String toString() {
        String result = "";
        if (law != null) {
            result += law.getShortName() + " (" + law.getAnnounementDate().getYear() + ")";
        }
        if (paragraphRef != null) {
            result += " " + paragraphRef;
        }
        if (sectionRef != null) {
            result += " " + sectionRef + " ledd";
        }
        if (letterRef != null) {
            result += " bokstav " + letterRef;
        }
        if (sentenceRef != null) {
            result += " " + sentenceRef + " punktum";
        }
        return result.trim();
    }

    public static class LawNotFoundException_LawIdInvalid extends RuntimeException {
        LawNotFoundException_LawIdInvalid(String message) {
            super(message);
        }
    }

    public static class LawNotFoundException_LawIdNotFound extends RuntimeException {
        LawNotFoundException_LawIdNotFound(String message) {
            super(message);
        }
    }

    public static class LawNotFoundException_ControlNameDoesNotMatch extends RuntimeException {
        LawNotFoundException_ControlNameDoesNotMatch(String message) {
            super(message);
        }
    }
}
