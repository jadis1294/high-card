
package it.sara.demo.service.util;

import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Utility class for user-related operations such as validation, filtering, sorting, and pagination.
 */
public class UserUtil {

    private static final String EMAIL_REGEX = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
    private static final String PHONE_REGEX = "^(\\+39|0039)?(3\\d{9}|0\\d{8,9})$";
    private static final String NAME_REGEX = "^[a-zA-ZàèìòùÀÈÌÒÙ'\\s]{1,50}$";

    /**
     * Validates the fields of a User object.
     *
     * @param user the User object to validate
     * @throws GenericException if any field is invalid or null
     */
    public static void validate(User user) throws GenericException {
        if (user == null) {
            throw new GenericException(400, "User object is null");
        }

        if (isNullOrEmpty(user.getFirstName()) || !user.getFirstName().matches(NAME_REGEX)) {
            throw new GenericException(400, "Invalid first name");
        }

        if (isNullOrEmpty(user.getLastName()) || !user.getLastName().matches(NAME_REGEX)) {
            throw new GenericException(400, "Invalid last name");
        }

        if (isNullOrEmpty(user.getEmail()) || !user.getEmail().matches(EMAIL_REGEX)) {
            throw new GenericException(400, "Invalid email format");
        }

        if (isNullOrEmpty(user.getPhoneNumber()) || !user.getPhoneNumber().matches(PHONE_REGEX)) {
            throw new GenericException(400, "Invalid Italian phone number format");
        }
    }

    /**
     * Checks if a string is null or empty after trimming.
     *
     * @param value the string to check
     * @return true if null or empty, false otherwise
     */
    private static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates pagination parameters in CriteriaGetUsers.
     *
     * @param criteria the criteria containing pagination parameters
     * @throws GenericException if offset or limit are invalid
     */
    public static void validatePagination(CriteriaGetUsers criteria) throws GenericException {
        if (criteria == null) {
            throw new GenericException(400, "Criteria object is null");
        }
        if (criteria.getLimit() <= 0 || criteria.getOffset() < 0) {
            throw new GenericException(400, "Invalid pagination parameters");
        }
    }

    /**
     * Applies a case-insensitive filter to a list of users based on a search query.
     * The filter checks first name, last name, and email.
     *
     * @param users the list of users to filter
     * @param query the search string
     * @return a filtered list of users
     * @throws GenericException if the user list is null
     */
    public static List<User> applySearchFilter(List<User> users, String query) throws GenericException {
        if (users == null) {
            throw new GenericException(500, "User list is null");
        }

        if (query == null || query.trim().isEmpty()) {
            return users;
        }

        String lowerQuery = query.toLowerCase();
        return users.stream()
                .filter(u -> Optional.ofNullable(u.getFirstName()).orElse("").toLowerCase().contains(lowerQuery)
                        || Optional.ofNullable(u.getLastName()).orElse("").toLowerCase().contains(lowerQuery)
                        || Optional.ofNullable(u.getEmail()).orElse("").toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Sorts a list of users based on the specified order type.
     *
     * @param users     the list of users to sort
     * @param orderType the sorting criteria defined in CriteriaGetUsers.OrderType
     * @return a sorted list of users
     * @throws GenericException if the user list is null
     */
    public static List<User> applySorting(List<User> users, CriteriaGetUsers.OrderType orderType) throws GenericException {
        if (users == null) {
            throw new GenericException(500, "User list is null");
        }

        if (orderType == null) {
            orderType = CriteriaGetUsers.OrderType.BY_FIRSTNAME;
        }

        Comparator<User> comparator;

        switch (orderType) {
            case BY_FIRSTNAME_DESC:
                comparator = Comparator.comparing(User::getFirstName, Comparator.nullsLast(String::compareToIgnoreCase)).reversed();
                break;
            case BY_LASTNAME:
                comparator = Comparator.comparing(User::getLastName, Comparator.nullsLast(String::compareToIgnoreCase));
                break;
            case BY_LASTNAME_DESC:
                comparator = Comparator.comparing(User::getLastName, Comparator.nullsLast(String::compareToIgnoreCase)).reversed();
                break;
            case BY_FIRSTNAME:
            default:
                comparator = Comparator.comparing(User::getFirstName, Comparator.nullsLast(String::compareToIgnoreCase));
        }

        return users.stream().sorted(comparator).collect(Collectors.toList());
    }

    /**
     * Applies pagination to a list of users based on offset and limit.
     *
     * @param users  the list of users to paginate
     * @param offset the starting index
     * @param limit  the maximum number of users to return
     * @return a paginated sublist of users
     * @throws GenericException if the user list is null or pagination parameters are invalid
     */
    public static List<User> applyPagination(List<User> users, int offset, int limit) throws GenericException {
        if (users == null) {
            throw new GenericException(500, "User list is null");
        }
        if (limit <= 0 || offset < 0) {
            throw new GenericException(400, "Invalid pagination parameters");
        }

        int fromIndex = Math.min(offset, users.size());
        int toIndex = Math.min(fromIndex + limit, users.size());
        return users.subList(fromIndex, toIndex);
    }
}
