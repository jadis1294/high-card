package it.sara.demo.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.web.assembler.AddUserAssembler;
import it.sara.demo.jwt.util.JwtUtil;
import it.sara.demo.web.user.UserController;
import it.sara.demo.web.user.request.AddUserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AddUserAssembler addUserAssembler;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser
    void shouldAddUserSuccessfully() throws Exception {
        AddUserRequest request = new AddUserRequest();
        request.setFirstName("Luca");

        CriteriaAddUser criteria = new CriteriaAddUser();
        when(addUserAssembler.toCriteria(any())).thenReturn(criteria);

    mockMvc.perform(put("/user/v1/user").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value(200));
    }
}
