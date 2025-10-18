
package it.sara.demo.service.user.impl;

import it.sara.demo.dto.UserDTO;
import it.sara.demo.exception.GenericException;
import it.sara.demo.service.assembler.UserAssembler;
import it.sara.demo.service.database.UserRepository;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.result.AddUserResult;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.service.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the UserService interface.
 * Handles user creation and retrieval operations.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Adds a new user to the system after validating the input.
     *
     * @param criteria the criteria containing user data to be added
     * @return AddUserResult containing the outcome of the operation
     * @throws GenericException if validation fails or saving the user encounters an error
     */
    @Override
    public AddUserResult addUser(CriteriaAddUser criteria) throws GenericException {
        AddUserResult returnValue;
        User user;

        try {
            returnValue = new AddUserResult();

            user = new User();
            user.setFirstName(criteria.getFirstName());
            user.setLastName(criteria.getLastName());
            user.setEmail(criteria.getEmail());
            user.setPhoneNumber(criteria.getPhoneNumber());
            UserUtil.validate(user);
            if (!this.userRepository.save(user)) {
                throw new GenericException(500, "Error saving user");
            }

        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            throw new GenericException(GenericException.GENERIC_ERROR);
        }
        return returnValue;
    }

    /**
     * Retrieves a paginated, sorted, and filtered list of users.
     *
     * @param criteria the criteria containing pagination, sorting, and search parameters
     * @return GetUsersResult containing the list of matching users
     * @throws GenericException if pagination parameters are invalid
     */
    @Override
    public GetUsersResult getUsers(CriteriaGetUsers criteria) throws GenericException {
        UserUtil.validatePagination(criteria);
        List<User> users = this.userRepository.getAll();
        users = UserUtil.applySearchFilter(users, criteria.getQuery());
        users = UserUtil.applySorting(users, criteria.getOrder());

        List<User> paginated = UserUtil.applyPagination(users, criteria.getOffset(), criteria.getLimit());

        List<UserDTO> dtoList = paginated.stream()
                .map(UserAssembler::toDTO)
                .collect(Collectors.toList());

        GetUsersResult result = new GetUsersResult();
        result.setUsers(dtoList);
        return result;
    }
}
