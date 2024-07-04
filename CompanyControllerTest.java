@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSearchCompany() throws Exception {
        // Mock TruProxyAPI response using WireMock
        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/TruProxyAPI/rest/Companies/v1/Search"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ /* Mock API response */ }")));

        mockMvc.perform(post("/api/companies/search")
                .content("{ \"companyName\" : \"BBC LIMITED\" }")
                .header("x-api-key", "your-api-key")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_results").value(1));
    }
}