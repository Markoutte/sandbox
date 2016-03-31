package me.markoutte.sandbox.jee.validation;

import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Pelevin <maks.pelevin@oogis.ru>
 * @since 2016-03-31
 */
public class AddressTest {

    @Test
    public void shouldRaiseConstraintViolationCauseInvalidZipCode() {

        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();

        Address address = new Address("233 Spring Street", "New York", "NY", "DummyZip", "USA");

        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        assertEquals(1, violations.size());

        vf.close();
    }

}
