package no.law.app;

import no.law.Law;
import no.law.LawRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
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
        private final Map<String, String> changeInLaws;
        private final Map<String, String> thisLawChangedBy;
        private final Collection<String> debugInformation;

        public LawDto(Law law) {
            this.lawId = law.getLawId();
            this.fullName = law.getFullName();
            this.isChangeLaw = law.isChangeLaw();
            this.changeInLaws = law.getChangeInLawIds().stream()
                    .collect(Collectors.toMap(lawId -> lawId, lawId -> {
                        Law law1 = LawRepository.getLaw(lawId);
                        if (law1 != null) {
                            return law1.getFullName();
                        }
                        else {
                            return lawId + " (law not found)";
                        }
                    }));
            this.debugInformation = law.getDebugInformation();

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

        public Map<String, String> getChangeInLaws() {
            return changeInLaws;
        }

        public Collection<String> getDebugInformation() {
            return debugInformation;
        }

        public Map<String, String> getThisLawChangedBy() {
            return thisLawChangedBy;
        }
    }
}
