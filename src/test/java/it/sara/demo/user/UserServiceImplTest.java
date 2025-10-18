package it.sara.demo.user;

import it.sara.demo.dto.UserDTO;
import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.UserRepository;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.impl.UserServiceImpl;
import it.sara.demo.service.user.result.AddUserResult;
import it.sara.demo.service.user.result.GetUsersResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private UserServiceImpl userService;

    @Test
    void shouldAddUserSuccessfully() throws GenericException {
        CriteriaAddUser criteria = new CriteriaAddUser();
        criteria.setFirstName("Luca");
        criteria.setLastName("De Silvestris");
        criteria.setEmail("luca@example.com");
    // Italian phone number format expected by UserUtil (e.g. starts with 3 for mobile or 0/\u002B39 prefix)
    criteria.setPhoneNumber("3123456789");

        when(userRepository.save(any(User.class))).thenReturn(true);

        AddUserResult result = userService.addUser(criteria);
        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenUserSaveFails() throws GenericException {
        CriteriaAddUser criteria = new CriteriaAddUser();
        criteria.setFirstName("Luca");
        criteria.setLastName("De Silvestris");
        criteria.setEmail("luca@example.com");
    criteria.setPhoneNumber("3123456789");

        when(userRepository.save(any(User.class))).thenReturn(false);

        GenericException ex = assertThrows(GenericException.class, () -> userService.addUser(criteria));
        assertEquals(500, ex.getStatus().getCode());
    }

    @Test
    void shouldReturnFilteredAndPaginatedUsers() throws GenericException {
        CriteriaGetUsers criteria = new CriteriaGetUsers();
        criteria.setQuery("luc");
        criteria.setOffset(0);
        criteria.setLimit(2);
        criteria.setOrder(CriteriaGetUsers.OrderType.BY_FIRSTNAME);

    User user1 = new User("Luca", "Rossi", "luca@example.com", "312");
    User user2 = new User("Lucia", "Verdi", "lucia@example.com", "345");
    User user3 = new User("Marco", "Bianchi", "marco@example.com", "378");

        when(userRepository.getAll()).thenReturn(Arrays.asList(user1, user2, user3));

        GetUsersResult result = userService.getUsers(criteria);
        List<UserDTO> users = result.getUsers();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getFirstName().startsWith("Luc")));
    }
}