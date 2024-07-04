@Service
public class CompanyService {

    @Autowired
    private RestTemplate restTemplate;

    public CompanySearchResponse searchCompany(CompanySearchRequest request, String apiKey, boolean activeOnly) {
        // Call TruProxyAPI and process the response
        String url = "https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Search?Query=" + 
                     (request.getCompanyNumber() != null ? request.getCompanyNumber() : request.getCompanyName());

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<TruProxyApiResponse> apiResponse = restTemplate.exchange(url, HttpMethod.GET, entity, TruProxyApiResponse.class);

        // Filter and process the API response
        List<Company> filteredCompanies = filterCompanies(apiResponse.getBody().getItems(), activeOnly);

        // Transform the data into your application's response format
        return new CompanySearchResponse(filteredCompanies.size(), filteredCompanies);
    }

    private List<Company> filterCompanies(List<Company> companies, boolean activeOnly) {
        return companies.stream()
                .filter(company -> !activeOnly || "active".equals(company.getCompanyStatus()))
                .peek(company -> {
                    List<Officer> activeOfficers = company.getOfficers().stream()
                            .filter(officer -> officer.getResignedOn() == null)
                            .collect(Collectors.toList());
                    company.setOfficers(activeOfficers);
                })
                .collect(Collectors.toList());
    }
}
