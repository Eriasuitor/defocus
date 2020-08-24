import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funtree.defocus.app.AppApplication;
import com.funtree.defocus.app.controller.SystemController;
import com.funtree.defocus.app.entity.SystemConfig;
import com.funtree.defocus.security.controller.AccountController;
import com.funtree.defocus.security.entity.Account;
import com.funtree.defocus.security.repository.AccountRepository;
import com.funtree.defocus.security.util.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class AccountTest {
    // @Autowired
    MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;
    @LocalServerPort
    int port;

    @Autowired
    AccountController accountController;

    @Autowired
    SystemController systemController;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TestRestTemplate testRestTemplate;

    Account account;
    String auauthorization;

    {
        this.account = new Account();
        account.setUsername("lory");
        account.setPassword("lory's_password");
        account.setEmail("lory.j@gmail.com");
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.accountController, this.systemController).build();
        accountRepository.removeAccountByUsername(account.getUsername());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/sign-up/email")
                .content(new ObjectMapper().writeValueAsString(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        this.account = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readValue(result.getResponse().getContentAsString(), Account.class);
    }

    @Test
    public void testSignUpWithEmail() throws Exception {

    }

    @Test
    public void testSignInWithPassword() throws Exception {
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/sign-in/password")
//                .content(new ObjectMapper().writeValueAsString(account))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//        this.auauthorization = result.getResponse().getHeader(JwtUtil.getTokenHeader());

//        MvcResult cfgResult = mockMvc.perform(MockMvcRequestBuilders.get("/system/config")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn();
//        System.out.println(cfgResult.getResponse().getContentAsString());
        Map<String, Object> claims = new HashMap<>();
        claims.put("subject", account.getId());
        claims.put("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        this.auauthorization = JwtUtil.generateToken(claims);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtUtil.getTokenHeader(), "Bearer " + this.auauthorization);
        ResponseEntity<SystemConfig> response = this.testRestTemplate
                .exchange(String.format("http://localhost:%d/system/config", this.port), HttpMethod.GET,new HttpEntity<>(headers), SystemConfig.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
        response = this.testRestTemplate
                .exchange(String.format("http://localhost:%d/system/config", this.port), HttpMethod.GET,new HttpEntity<>(null), SystemConfig.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

//    @Test
//    public void testSearchASync() throws Exception {
//        MvcResult result = mockMvc.perform(get("/manga/async/ken").contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(request().asyncStarted())
//                .andDo(print())
//                .andReturn();
//        mockMvc.perform(asyncDispatch(result))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.*.title", hasItem(is("Hokuto no Ken"))));
//    }
}
