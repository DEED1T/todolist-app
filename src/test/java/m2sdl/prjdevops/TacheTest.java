package m2sdl.prjdevops;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import m2sdl.prjdevops.domain.Tache;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TacheTest {
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testIsDoneFalseEtDescription () {
        Tache tache = new Tache("Ikea", "Assembler le meuble.");

        assertTrue(validator.validate(tache).isEmpty());
    }

    @Test
    public void testIsDoneTrueEtDescription () {
        Tache tache = new Tache("Douche", "Déboucher le siphon.");

        assertTrue(validator.validate(tache).isEmpty());
    }

    @Test
    public void testIsDescriptionEmpty() {
        Tache tache = new Tache("Un titre original", "");

        assertFalse(validator.validate(tache).isEmpty());
    }

    @Test
    public void testIsDescriptionNull() {
        Tache tache = new Tache("J'ai pas d'idées", null);

        assertFalse(validator.validate(tache).isEmpty());
    }
}
