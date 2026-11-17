package com.example.MealShare.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.MealShare.entity.CookEntity;
import com.example.MealShare.entity.CustomerEntity;
import com.example.MealShare.service.CookService;
import com.example.MealShare.service.CustomerService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

// handles browser pages and returns freemarker template names
@Controller
public class CustomerPageController {

    // these lists fill the cuisine and diet dropdown menus on the website
    private static final List<String> CUISINES =
            List.of("Italian", "Mexican", "American", "Asian", "Mediterranean");

    private static final List<String> DIETS =
            List.of("Vegetarian", "Vegan", "Gluten-Free", "Dairy-Free", "High-Protein");

    // services contain the actual customer and cook operations
    private final CustomerService customerService;
    private final CookService cookService;

    // spring injects both services through this constructor
    public CustomerPageController(
            CustomerService customerService,
            CookService cookService) {
        this.customerService = customerService;
        this.cookService = cookService;
    }

    // loads the home page and adds up to four cooks to the featured section
    @GetMapping("/")
    public String home(Model model) {
        List<CookEntity> cooks = cookService.getAllCooks();

        // avoids an error if the database has fewer than four cooks
        model.addAttribute(
                "featuredCooks",
                cooks.subList(0, Math.min(4, cooks.size()))
        );

        return "index";
    }

    // displays an empty registration form
    @GetMapping("/customers/new")
    public String registerForm(Model model) {
        return formView(
                model,
                new CustomerEntity(),
                new HashMap<>(),
                false,
                "/customers"
        );
    }

    // receives the registration form after the customer submits it
    @PostMapping("/customers")
    public String register(
            @Valid @ModelAttribute("customer") CustomerEntity customer,
            BindingResult result,
            @RequestParam(defaultValue = "") String confirmPassword,
            HttpSession session,
            Model model) {

        // converts validation errors into a map that the template can display
        Map<String, String> errors = fieldErrors(result);

        // safely handles a missing password before comparing it
        String password = customer.getPassword() == null
                ? ""
                : customer.getPassword();

        // checks that the confirmation field matches the original password
        if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Passwords do not match");
        }

        // only try to save the customer when the form has no validation errors
        if (errors.isEmpty()) {
            try {
                CustomerEntity saved = customerService.create(customer);

                // remembers the new customer's id and name for this browser session
                startSession(session, saved);

                // sends the browser to the new customer's dashboard
                return "redirect:/customers/" + saved.getId();

            } catch (ResponseStatusException e) {
                // shows duplicate-email or other service errors beside the form
                errors.put(
                        e.getStatusCode().value() == 409 ? "email" : "password",
                        e.getReason()
                );
            }
        }

        // reloads the form with the customer's entered values and any errors
        return formView(model, customer, errors, false, "/customers");
    }

    // displays the customer login page
    @GetMapping("/customers/login")
    public String loginForm() {
        return "customer-login";
    }

    // checks the submitted login details and starts a session when valid
    @PostMapping("/customers/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {
            CustomerEntity customer = customerService.login(email, password);
            startSession(session, customer);

            return "redirect:/customers/" + customer.getId();

        } catch (ResponseStatusException e) {
            // keeps the email in the form and displays the login error
            model.addAttribute("loginError", e.getReason());
            model.addAttribute("email", email);

            return "customer-login";
        }
    }

    // logs the customer out by clearing their current session
    @GetMapping("/customers/logout")
    public String logout(
            HttpSession session,
            RedirectAttributes redirect) {

        session.invalidate();

        // flash attributes appear once on the next page after the redirect
        redirect.addFlashAttribute("flash", "You have been logged out.");

        return "redirect:/";
    }

    // loads a customer's dashboard, but only lets that customer view their own account
    @GetMapping("/customers/{id}")
    public String dashboard(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        CustomerEntity customer = customerService.getById(id);

        // sends visitors to login if the session does not belong to this customer
        if (!isOwner(id, session)) {
            return "redirect:/customers/login";
        }

        model.addAttribute("customer", customer);
        return "customer-dashboard";
    }

    // displays the edit form for the signed-in customer's account
    @GetMapping("/customers/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        CustomerEntity customer = customerService.getById(id);

        // prevents one customer from opening another customer's edit page
        if (!isOwner(id, session)) {
            return "redirect:/customers/login";
        }

        return formView(
                model,
                customer,
                new HashMap<>(),
                true,
                "/customers/" + id
        );
    }

    // receives changes submitted from the customer settings form
    @PostMapping("/customers/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("customer") CustomerEntity customer,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirect,
            Model model) {

        // confirms the customer record exists before attempting the update
        customerService.getById(id);

        if (!isOwner(id, session)) {
            return "redirect:/customers/login";
        }

        Map<String, String> errors = fieldErrors(result);

        if (errors.isEmpty()) {
            try {
                CustomerEntity saved = customerService.update(id, customer);

                // updates the name stored in the current session
                session.setAttribute("customerName", saved.getFirstName());

                // displays a one-time success message after redirecting
                redirect.addFlashAttribute(
                        "flash",
                        "Your changes have been saved."
                );

                return "redirect:/customers/" + id;

            } catch (ResponseStatusException e) {
                // puts service errors beside the field that most likely caused them
                errors.put(
                        e.getStatusCode().value() == 409 ? "email" : "password",
                        e.getReason()
                );
            }
        }

        // keeps the form open and displays validation errors
        return formView(
                model,
                customer,
                errors,
                true,
                "/customers/" + id
        );
    }

    // deletes the signed-in customer's account
    @PostMapping("/customers/{id}/delete")
    public String delete(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirect) {

        customerService.getById(id);

        // only the owner of the account can delete it
        if (!isOwner(id, session)) {
            return "redirect:/customers/login";
        }

        customerService.delete(id);
        session.invalidate();

        redirect.addFlashAttribute("flash", "Your account was deleted.");
        return "redirect:/";
    }

    // shows cooks and applies any name, cuisine, or dietary filters from the url
    @GetMapping("/browse-cooks")
    public String browse(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String diet,
            Model model) {

        // asks the cook service to return only cooks matching the selected filters
        model.addAttribute("cooks", cookService.search(name, cuisine, diet));

        // sends the current filter values back so the form keeps its selections
        model.addAttribute("name", name);
        model.addAttribute("cuisine", cuisine);
        model.addAttribute("diet", diet);
        model.addAttribute("cuisines", CUISINES);
        model.addAttribute("diets", DIETS);

        return "browse-cooks";
    }

    // displays one cook's profile page based on the id in the url
    @GetMapping("/browse-cooks/{id}")
    public String cookProfile(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("cook", cookService.getCookById(id));
        return "cook-profile";
    }

    // catches response status errors from page requests and displays the friendly error template
    @ExceptionHandler(ResponseStatusException.class)
    public String handleError(
            ResponseStatusException e,
            HttpServletResponse response,
            Model model) {

        // keeps the correct http status code even though an error template is returned
        response.setStatus(e.getStatusCode().value());
        model.addAttribute("errorMessage", e.getReason());

        return "error";
    }

    // shared method that prepares the model for both new and edit customer forms
    private String formView(
            Model model,
            CustomerEntity customer,
            Map<String, String> errors,
            boolean editing,
            String action) {

        model.addAttribute("customer", customer);
        model.addAttribute("errors", errors);
        model.addAttribute("editing", editing);
        model.addAttribute("formAction", action);
        model.addAttribute("cuisines", CUISINES);
        model.addAttribute("diets", DIETS);

        return "customer-form";
    }

    // collects field validation messages into a map using the field name as the key
    private Map<String, String> fieldErrors(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(
                e -> errors.putIfAbsent(
                        e.getField(),
                        e.getDefaultMessage()
                )
        );

        return errors;
    }

    // stores the signed-in customer's id and first name in the browser session
    private void startSession(HttpSession session, CustomerEntity customer) {
        session.setAttribute("customerId", customer.getId());
        session.setAttribute("customerName", customer.getFirstName());
    }

    // checks whether the current session belongs to the customer in the url
    private boolean isOwner(Long id, HttpSession session) {
        Object loggedIn = session.getAttribute("customerId");

        return loggedIn != null && loggedIn.equals(id);
    }
}