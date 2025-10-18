package it.sara.demo.web.user;

import it.sara.demo.exception.GenericException;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.web.assembler.AddUserAssembler;
import it.sara.demo.web.response.GenericResponse;
import it.sara.demo.web.user.request.AddUserRequest;
import it.sara.demo.web.user.request.GetUsersRequest;
import it.sara.demo.web.user.response.GetUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user-related operations.
 * Provides endpoints for adding a user and retrieving a list of users.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * Service layer for user operations.
     */
    @Autowired
    private UserService userService;

    /**
     * Assembler to convert web-layer requests into service-layer criteria.
     */
    @Autowired
    private AddUserAssembler addUserAssembler;

    /**
     * Adds a new user to the system.
     *
     * @param request the request containing user data
     * @return a {@link GenericResponse} indicating success or failure
     * @throws GenericException if validation or persistence fails
     */
    @PutMapping("/v1/user")
    public ResponseEntity<GenericResponse> addUser(@RequestBody AddUserRequest request) throws GenericException {
        CriteriaAddUser criteria = addUserAssembler.toCriteria(request);
        userService.addUser(criteria);
        return ResponseEntity.ok(GenericResponse.success("User added."));
    }

    /**
     * Retrieves a list of users with optional filtering, sorting, and pagination.
     *
     * @param query optional search query to filter users by name or email
     * @param offset pagination offset (default is 0)
     * @param limit pagination limit (default is 10)
     * @param order sorting order (default is BY_FIRSTNAME)
     * @return a {@link GetUsersResponse} containing the list of users
     * @throws GenericException if parameters are invalid or retrieval fails
     */
    @GetMapping("/v1/users")
    public ResponseEntity<GetUsersResponse> getUsers(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "BY_FIRSTNAME") CriteriaGetUsers.OrderType order
    ) throws GenericException {
        CriteriaGetUsers criteria = new CriteriaGetUsers();
        criteria.setQuery(query);
        criteria.setOffset(offset);
        criteria.setLimit(limit);
        criteria.setOrder(order);

        GetUsersResult result = userService.getUsers(criteria);

        GetUsersResponse response = new GetUsersResponse();
        response.setUsers(result.getUsers());
        return ResponseEntity.ok(response);
    }
}
