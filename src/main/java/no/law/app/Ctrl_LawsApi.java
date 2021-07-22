package no.law.app;

import no.law.Law;
import no.law.LawRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class Ctrl_LawsApi {
    @GetMapping(value = "/api/laws")
    public LawsDto laws() {
        return new LawsDto(LawRepository.getLaws()
                .stream()
                .map(LawDto::new)
                .sorted(Comparator.comparing(lawDto -> lawDto.lawId))
                .collect(Collectors.toList())
        );

    }

    public static class LawsDto {
        private final List<LawDto> laws;

        public LawsDto(List<LawDto> collect) {
            laws = collect;
        }

        public List<LawDto> getLaws() {
            return laws;
        }
    }

    public static class LawDto {
        private final String lawId;
        private final String fullName;
        private final boolean isChangeLaw;
        private final String changeInLawName;
        private final String changeInLawId;
        private final Map<String, String> thisLawChangedBy;

        public LawDto(Law law) {
            this.lawId = law.getLawId();
            this.fullName = law.getFullName();
            this.isChangeLaw = law.isChangeLaw();
            this.changeInLawName = law.getChangeInLawName();
            this.changeInLawId = law.getChangeInLawId();

            if (!law.getThisLawChangedBy().isEmpty()) {
                this.thisLawChangedBy = law.getThisLawChangedBy().stream()
                        .collect(Collectors.toMap(Law::getLawId, Law::getFullName));
            }
            else {
                this.thisLawChangedBy = null;
            }
        }

        public String getLawId() {
            return lawId;
        }

        public String getFullName() {
            return fullName;
        }

        public boolean isChangeLaw() {
            return isChangeLaw;
        }

        public String getChangeInLawName() {
            return changeInLawName;
        }

        public String getChangeInLawId() {
            return changeInLawId;
        }

        public Map<String, String> getThisLawChangedBy() {
            return thisLawChangedBy;
        }
    }
}
