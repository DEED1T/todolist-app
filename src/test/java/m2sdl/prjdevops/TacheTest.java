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
        Tache tache = new Tache("Ikea", "Assembler le meuble.", "Jean");

        assertTrue(validator.validate(tache).isEmpty());
    }

    @Test
    public void testIsDoneTrueEtDescription () {
        Tache tache = new Tache("Douche", "Déboucher le siphon.", "Luc");

        assertTrue(validator.validate(tache).isEmpty());
    }

    @Test
    public void testIsDescriptionEmpty() {
        Tache tache = new Tache("Un titre original", "", "Marc");

        assertFalse(validator.validate(tache).isEmpty());
    }

    @Test
    public void testIsDescriptionNull() {
        Tache tache = new Tache("J'ai pas d'idées", null, "Lambda");

        assertFalse(validator.validate(tache).isEmpty());
    }

    @Test
    public void testIsUtilisateurNull() {
        Tache tache = new Tache("J'ai encore moins d'idées", "null ?", null);

        assertFalse(validator.validate(tache).isEmpty());
    }

    @Test
    public void testIsUtilisateurEmpty() {
        Tache tache = new Tache("Aucune idée à l'horizon...", "oups", "");

        assertFalse(validator.validate(tache).isEmpty());
    }

    @Test
    public void testIsBooleanIsDoneFalse() {
        Tache tache = new Tache("Trouver des idées", "... Pour les nouvelles taches à créer", "Mehdi");

        assertFalse(tache.getIsDone());
    }
}
