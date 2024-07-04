@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping("/search")
    public ResponseEntity<CompanySearchResponse> searchCompany(
            @RequestBody CompanySearchRequest request,
            @RequestHeader("x-api-key") String apiKey,
            @RequestParam(value = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
        CompanySearchResponse response = companyService.searchCompany(request, apiKey, activeOnly);
        return ResponseEntity.ok(response);
    }
}