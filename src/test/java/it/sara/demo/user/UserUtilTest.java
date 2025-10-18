package it.sara.demo.user;

import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.util.UserUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserUtilTest {

    @Test
    void testValidate_validUser_shouldPass() {
        User user = new User();
        user.setFirstName("Mario");
        user.setLastName("Rossi");
        user.setEmail("mario.rossi@example.com");
        user.setPhoneNumber("+393331234567");

        assertDoesNotThrow(() -> UserUtil.validate(user));
    }

    @Test
    void testValidate_invalidEmail_shouldThrow() {
        User user = new User();
        user.setFirstName("Mario");
        user.setLastName("Rossi");
        user.setEmail("invalid-email");
        user.setPhoneNumber("+393331234567");

        GenericException ex = assertThrows(GenericException.class, () -> UserUtil.validate(user));
        assertEquals("Invalid email format", ex.getMessage());
    }

    @Test
    void testValidatePagination_valid_shouldPass() {
        CriteriaGetUsers criteria = new CriteriaGetUsers();
        criteria.setOffset(0);
        criteria.setLimit(10);

        assertDoesNotThrow(() -> UserUtil.validatePagination(criteria));
    }

    @Test
    void testValidatePagination_invalid_shouldThrow() {
        CriteriaGetUsers criteria = new CriteriaGetUsers();
        criteria.setOffset(-1);
        criteria.setLimit(0);

        GenericException ex = assertThrows(GenericException.class, () -> UserUtil.validatePagination(criteria));
        assertEquals("Invalid pagination parameters", ex.getMessage());
    }

    @Test
    void testApplySearchFilter_shouldReturnMatchingUsers() throws GenericException {
        User u1 = new User(); u1.setFirstName("Mario"); u1.setEmail("mario@example.com");
        User u2 = new User(); u2.setFirstName("Luigi"); u2.setEmail("luigi@example.com");

        List<User> result = UserUtil.applySearchFilter(Arrays.asList(u1, u2), "mario");
        assertEquals(1, result.size());
        assertEquals("Mario", result.get(0).getFirstName());
    }

    @Test
    void testApplySorting_byLastNameDesc_shouldSortCorrectly() throws GenericException {
        User u1 = new User(); u1.setLastName("Bianchi");
        User u2 = new User(); u2.setLastName("Zanetti");

        List<User> sorted = UserUtil.applySorting(Arrays.asList(u1, u2), CriteriaGetUsers.OrderType.BY_LASTNAME_DESC);
        assertEquals("Zanetti", sorted.get(0).getLastName());
    }

    @Test
    void testApplyPagination_shouldReturnCorrectSublist() throws GenericException {
        User u1 = new User(); u1.setFirstName("A");
        User u2 = new User(); u2.setFirstName("B");
        User u3 = new User(); u3.setFirstName("C");

        List<User> paginated = UserUtil.applyPagination(Arrays.asList(u1, u2, u3), 1, 2);
        assertEquals(2, paginated.size());
        assertEquals("B", paginated.get(0).getFirstName());
        assertEquals("C", paginated.get(1).getFirstName());
    }
}