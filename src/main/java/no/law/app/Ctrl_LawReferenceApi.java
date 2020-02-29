package no.law.app;

import no.law.LawReference;
import no.law.lawreference.LawReferenceFinder;
import no.law.lawreference.NorwegianText_to_LawReference;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Ctrl_LawReferenceApi {
    @GetMapping(value = "/api/law-reference")
    public LawReferenceWithLawDto lawRef(@RequestParam String searchQuery) {
        LawReferenceFinder law = NorwegianText_to_LawReference.textToLawReference(searchQuery, LocalDate.now());

        if (law.getLaw() == null) {
            return new LawReferenceWithLawDto("Law not found.");
        }

        List<? extends LawReference> matchingLaw = law.getLaw().getMatchingLawRef(law);
        return new LawReferenceWithLawDto(law, matchingLaw);
    }

    public static class LawReferenceWithLawDto {
        private final String lawReference;
        private final List<String> lawReferenceMatchTypes;
        private final String html;

        LawReferenceWithLawDto(LawReferenceFinder lawRef, List<? extends LawReference> lawRefs) {
            this.lawReference = lawRef.toString();
            this.lawReferenceMatchTypes = lawRefs.stream()
                    .map(m -> m.getClass())
                    .map(Class::getSimpleName)
                    .collect(Collectors.toList());

            this.html = lawRefs.stream()
                    .map(LawReference::toHtml)
                    .collect(Collectors.joining("\n\n\n<br><hr><br>\n\n\n"));
        }

        LawReferenceWithLawDto(String html) {
            this.lawReference = "";
            this.lawReferenceMatchTypes = new ArrayList<>();
            this.html = html;
        }

        public String getLawReference() {
            return lawReference;
        }

        public List<String> getLawReferenceMatchTypes() {
            return lawReferenceMatchTypes;
        }

        public String getHtml() {
            return html;
        }
    }
}
