package at.ac.tuwien.sepr.assignment.individual.rest;

import at.ac.tuwien.sepr.assignment.individual.TestBase;
import at.ac.tuwien.sepr.assignment.individual.dto.ArticleDetailDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class to integration-test the customer endpoint.
 */
@ActiveProfiles({"test", "datagen"})
// enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
public class ArticleEndpointTest extends TestBase {

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void gettingNonexistentUrlReturns404() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/asdf123")
                ).andExpect(status().isNotFound());
    }

    @Test
    public void gettingAllArticles() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/articles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<ArticleDetailDto> customerResult = objectMapper.readerFor(ArticleDetailDto.class)
                .<ArticleDetailDto>readValues(body).readAll();

        assertThat(customerResult).isNotNull();
        assertThat(customerResult)
                .hasSize(10);
    }


}
