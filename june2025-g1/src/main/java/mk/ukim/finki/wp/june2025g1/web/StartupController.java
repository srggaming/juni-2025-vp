package mk.ukim.finki.wp.june2025g1.web;

import mk.ukim.finki.wp.june2025g1.model.Industry;
import mk.ukim.finki.wp.june2025g1.model.Startup;
import mk.ukim.finki.wp.june2025g1.service.FounderService;
import mk.ukim.finki.wp.june2025g1.service.StartupService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StartupController {

    private final StartupService startupService;
    private final FounderService founderService;

    public StartupController(StartupService startupService, FounderService founderService) {
        this.startupService = startupService;
        this.founderService = founderService;
    }

    /**
     * This method should use the "list.html" template to display all startups.
     * The method should be mapped on paths '/' and '/startups'.
     * The arguments that this method takes are optional and can be 'null'.
     * The filtered startups that are the result of the call
     * findPage method from the StartupService should be displayed.
     * If you want to return a paginated result, you should also pass the page number and the page size as arguments.
     *
     * @param name        Filters startups whose names contain the specified text.
     * @param valuation   Filters startups with a valuation greater than the specified value.
     * @param yearFounded Filters startups founded after the specified year.
     * @param industry    Filters startups by the specified industry.
     * @param founderId   Filters startups by the specified founder ID.
     * @param pageNum     The page number.
     * @param pageSize    The number of items per page.
     * @return The view "list.html"
     */
    @GetMapping({"/", "/startups"})
    public String listAll(@RequestParam(required = false) String name,
                          @RequestParam(required = false) Double valuation,
                          @RequestParam(required = false) Integer yearFounded,
                          @RequestParam(required = false) Industry industry,
                          @RequestParam(required = false) Long founderId,
                          @RequestParam(defaultValue = "1") int pageNum,
                          @RequestParam(defaultValue = "20") int pageSize,
                          Model model) {
        Page<Startup> page = startupService.findPage(name, valuation, yearFounded, industry, founderId, pageNum - 1, pageSize);
        model.addAttribute("startups", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("industries", Industry.values());
        model.addAttribute("founders", founderService.listAll());
        return "list";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/startups/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/startups/add")
    public String showAdd(Model model) {
        model.addAttribute("industries", Industry.values());
        model.addAttribute("founders", founderService.listAll());
        return "form";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case, all 'input' elements should be filled with the appropriate value for the startup that is updated.
     * The method should be mapped on path '/startups/edit/[id]'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/startups/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        Startup startup = startupService.findById(id);
        model.addAttribute("startup", startup);
        model.addAttribute("industries", Industry.values());
        model.addAttribute("founders", founderService.listAll());
        return "form";
    }

    /**
     * This method should create a new startup given the arguments it takes.
     * The method should be mapped on path '/startups'.
     * After the startup is created, all startups should be displayed.
     *
     * @param name        The name of the startup
     * @param valuation   The valuation of the startup
     * @param yearFounded The year the startup was founded
     * @param industry    The industry of the startup
     * @param founderId   The id of the founder of the startup
     * @return Redirects to the list of startups
     */
    @PostMapping("/startups")
    public String create(@RequestParam String name,
                         @RequestParam Double valuation,
                         @RequestParam Integer yearFounded,
                         @RequestParam Industry industry,
                         @RequestParam Long founderId) {
        startupService.create(name, valuation, yearFounded, industry, founderId);
        return "redirect:/startups";
    }

    /**
     * This method should update an existing startup given the arguments it takes.
     * The method should be mapped on path '/startups/[id]'.
     * After the startup is updated, all startups should be displayed.
     *
     * @param id          The id of the startup that is being updated
     * @param name        The name of the startup
     * @param valuation   The valuation of the startup
     * @param yearFounded The year the startup was founded
     * @param industry    The industry of the startup
     * @param founderId   The id of the founder of the startup
     * @return Redirects to the list of startups
     */
    @PostMapping("/startups/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam Double valuation,
                         @RequestParam Integer yearFounded,
                         @RequestParam Industry industry,
                         @RequestParam Long founderId) {
        startupService.update(id, name, valuation, yearFounded, industry, founderId);
        return "redirect:/startups";
    }

    /**
     * This method should delete the startup that has the appropriate identifier.
     * The method should be mapped on path '/startups/delete/[id]'.
     * After the startup is deleted, all startups should be displayed.
     *
     * @param id The ID of the startup to delete
     * @return Redirects to the list of startups
     */
    @PostMapping("/startups/delete/{id}")
    public String delete(@PathVariable Long id) {
        startupService.delete(id);
        return "redirect:/startups";
    }

    /**
     * This method should deactivate the startup that has the appropriate identifier.
     * The method should be mapped on path '/startups/deactivate/[id]'.
     * After the selected startups is closed, all startups should be displayed.
     *
     * @param id The ID of the startup to deactivate
     * @return Redirects to the list of startups
     */
    @PostMapping("/startups/deactivate/{id}")
    public String deactivate(@PathVariable Long id) {
        startupService.deactivate(id);
        return "redirect:/startups";
    }
}

