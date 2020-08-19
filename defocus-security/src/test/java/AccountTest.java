import com.funtree.defocus.security.controller.AccountController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(AccountController.class)
public class AccountTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void signWithEmail() throws Exception {
        MvcResult result= mvc.perform(MockMvcRequestBuilders.post("/accounts/signup-email")).andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println(response.getContentAsString());
//        ResponseEntity<Account> account = GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays();
//        Assert.isNull(Objects.requireNonNull(account.getBody()).getPassword(), "Password should not be returned");
    }
}
