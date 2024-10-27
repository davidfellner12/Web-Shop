package at.ac.tuwien.sepr.assignment.individual.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepr.assignment.individual.TestBase;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerCreateDto;
import at.ac.tuwien.sepr.assignment.individual.dto.CustomerDetailDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Test class to integration-test the customer endpoint.
 */
@ActiveProfiles({"test", "datagen"})
// enable "test" spring profile during test execution in order to pick up configuration from application-test.yml
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
public class CustomerEndpointTest extends TestBase {

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
    public void gettingAllCustomers() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/customers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<CustomerDetailDto> customerResult = objectMapper.readerFor(CustomerDetailDto.class)
                .<CustomerDetailDto>readValues(body).readAll();

        assertThat(customerResult).isNotNull();
        assertThat(customerResult)
                .hasSize(10);
    }

    @Test
    public void createCustomer() throws Exception {
        CustomerCreateDto customerCreateDto = new CustomerCreateDto(
                "Vincent",
                "Balau",
                LocalDate.of(1982, 3, 15),
                "vintischi@gmail.com"
        );

        String jsonRequest = objectMapper.writeValueAsString(customerCreateDto);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        byte[] body = mvcResult.getResponse().getContentAsByteArray();
        CustomerDetailDto createdCustomer = objectMapper.readValue(body, CustomerDetailDto.class);

        assertNotNull(createdCustomer);
        assertNotNull(createdCustomer.id());
        assertThat(createdCustomer.firstName()).isEqualTo("Vincent");
        assertThat(createdCustomer.lastName()).isEqualTo("Balau");
        assertThat(createdCustomer.dateOfBirth()).isEqualTo(LocalDate.of(1982, 3, 15));
        assertThat(createdCustomer.email()).isEqualTo("vintischi@gmail.com");
    }

    /*
    @Test
    public void createCustomer_MissingFirstName() throws Exception {
        CustomerCreateDto customerCreateDto = new CustomerCreateDto(
                null, // Missing first name
                "Balau",
                LocalDate.of(1982, 3, 15),
                "vintischi@gmail.com"
        );

        String jsonRequest = objectMapper.writeValueAsString(customerCreateDto);

         mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists());
    }*/






}
