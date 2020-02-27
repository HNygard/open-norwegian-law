package no.law;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static no.law.lawreference.NorwegianLawTextName_to_LawId.law;

public class NorwegianLawTextName_to_LawIdTest {

    @Test
    public void lawName_negative_invalid() {
        Assertions.assertNull(law(""));
    }

    @Test
    public void lawName_negative_nullInput() {
        Assertions.assertThrows(NullPointerException.class, () -> law(null));
    }

    @Test
    public void lawName_negative_invalidMonth() {
        Assertions.assertThrows(NullPointerException.class, () ->
                Assertions.assertNull(law("lov 20. june 2014 nr. 49")));
    }

    @Test
    public void lawName() {
        Assertions.assertEquals("LOV-2014-06-20-49", law("lov 20. juni 2014 nr. 49"));
        Assertions.assertEquals("LOV-2006-05-19-16", law("offentleglova"));
        Assertions.assertEquals("LOV-2006-05-19-16", law("Offentleglova (2006)"));
    }
}
