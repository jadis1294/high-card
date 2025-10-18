package it.sara.demo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.web.assembler.AddUserAssembler;
import it.sara.demo.web.user.UserController;
import it.sara.demo.web.user.request.AddUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AddUserAssembler addUserAssembler;

    @Test
    void shouldAddUserSuccessfully() throws Exception {
        AddUserRequest request = new AddUserRequest();
        request.setFirstName("Luca");

        CriteriaAddUser criteria = new CriteriaAddUser();
        when(addUserAssembler.toCriteria(any())).thenReturn(criteria);

        mockMvc.perform(put("/user/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200));
    }
}
