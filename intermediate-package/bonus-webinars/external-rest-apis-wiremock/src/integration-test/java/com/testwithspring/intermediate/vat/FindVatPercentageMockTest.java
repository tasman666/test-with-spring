package com.testwithspring.intermediate.vat;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.testwithspring.intermediate.ExampleApplication;
import com.testwithspring.intermediate.IntegrationTest;
import com.testwithspring.intermediate.Profiles;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ExampleApplication.class})
@AutoConfigureMockMvc
@Category(IntegrationTest.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class FindVatPercentageMockTest {

    private static final String COUNTRY_CODE = "FI";
    private static final Integer VAT_PERCENTAGE = 24;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options()
            .bindAddress("localhost")
            .port(8080)
    );

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void configureDefaultResponse() {
        givenThat(any(anyUrl())
                .willReturn(ok())
        );
    }
    
    @Test
    public void shouldGetVatPercentageFromExternalApi() throws Exception {
        findByCountryCode(COUNTRY_CODE);

        verify(1, getRequestedFor(urlEqualTo("/api/external/vat-percentage?countryCode=FI")));
    }

    private ResultActions findByCountryCode(String countryCode) throws Exception {
        return mockMvc.perform(get("/api/vat-percentage")
                .param("countryCode", countryCode)
        );
    }
}
