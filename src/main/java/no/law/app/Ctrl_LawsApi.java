package no.law.app;

import no.law.Law;
import no.law.LawRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Ctrl_LawsApi {
    @GetMapping(value = "/api/laws")
    public LawsDto laws(@RequestParam String searchQuery) {
        return new LawsDto(LawRepository.getLaws()
                .stream()
                .map(LawDto::new)
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
        private final String fullName;

        public LawDto(Law law) {
            this.fullName = law.getFullName();
        }

        public String getFullName() {
            return fullName;
        }
    }
}
