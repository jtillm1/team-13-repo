
package com.example.MealShare.controller;

import com.example.MealShare.entity.CookEntity;
import com.example.MealShare.entity.CustomerEntity;
import com.example.MealShare.service.CookService;
import com.example.MealShare.service.CustomerService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerPageController {

    private static final List<String> CUISINES =
            List.of("Italian", "Mexican", "American", "Asian", "Mediterranean");

    private static final List<String> DIETS =
            List.of("Vegetarian", "Vegan", "Gluten-Free", "Dairy-Free", "High-Protein");

    private final CustomerService customerService;
    private final CookService cookService;

    public CustomerPageController(
            CustomerService customerService,
            CookService cookService) {
        this.customerService = customerService;
        this.cookService = cookService;
    }

    // home page
    @GetMapping("/")
    public String home(Model model) {
        List<CookEntity> cooks = cookService.getAllCooks();

        model.addAttribute(
                "featuredCooks",
                cooks.subList(0, Math.min(4, cooks.size()))
        );

        return "index";
    }

    // customer registration
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

    @PostMapping("/customers")
    public String register(
            @Valid @ModelAttribute("customer") CustomerEntity customer,
            BindingResult result,
            @RequestParam(defaultValue = "") String confirmPassword,
            HttpSession session,
            Model model) {

        Map<String, String> errors = fieldErrors(result);

        String password = customer.getPassword() == null
                ? ""
                : customer.getPassword();

        if (!password.equals(confirmPassword)) {
            errors.put("confirmPassword", "Passwords do not match");
        }

        if (errors.isEmpty()) {
            try {
                CustomerEntity saved = customerService.create(customer);
                startSession(session, saved);

                return "redirect:/customers/" + saved.getId();

            } catch (ResponseStatusException e) {
                errors.put(
                        e.getStatusCode().value() == 409 ? "email" : "password",
                        e.getReason()
                );
            }
        }

        return formView(model, customer, errors, false, "/customers");
    }

    // customer login
    @GetMapping("/customers/login")
    public String loginForm() {
        return "customer-login";
    }

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
            model.addAttribute("loginError", e.getReason());
            model.addAttribute("email", email);

            return "customer-login";
        }
    }

    @GetMapping("/customers/logout")
    public String logout(
            HttpSession session,
            RedirectAttributes redirect) {

        session.invalidate();

        redirect.addFlashAttribute(
                "flash",
                "You have been logged out."
        );

        return "redirect:/";
    }

    // customer dashboard
    @GetMapping("/customers/{id}")
    public String dashboard(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        CustomerEntity customer = customerService.getById(id);

        if (!isOwner(id, session)) {
            return "redirect:/customers/login";
        }

        model.addAttribute("customer", customer);

        return "customer-dashboard";
    }

    // edit customer settings
    @GetMapping("/customers/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        CustomerEntity customer = customerService.getById(id);

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

    @PostMapping("/customers/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("customer") CustomerEntity customer,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirect,
            Model model) {

        customerService.getById(id);

        if (!isOwner(id, session)) {
            return "redirect:/customers/login";
        }

        Map<String, String> errors = fieldErrors(result);

        if (errors.isEmpty()) {
            try {
                CustomerEntity saved = customerService.update(id, customer);

                session.setAttribute(
                        "customerName",
                        saved.getFirstName()
                );

                redirect.addFlashAttribute(
                        "flash",
                        "Your changes have been saved."
                );

                return "redirect:/customers/" + id;

            } catch (ResponseStatusException e) {
                errors.put(
                        e.getStatusCode().value() == 409 ? "email" : "password",
                        e.getReason()
                );
            }
        }

        return formView(
                model,
                customer,
                errors,
                true,
                "/customers/" + id
        );
    }

    // delete customer account
    @PostMapping("/customers/{id}/delete")
    public String delete(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirect) {

        customerService.getById(id);

        if (!isOwner(id, session)) {
            return "redirect:/customers/login";
        }

        customerService.delete(id);
        session.invalidate();

        redirect.addFlashAttribute(
                "flash",
                "Your account was deleted."
        );

        return "redirect:/";
    }

    // browse and filter cooks
    @GetMapping("/browse-cooks")
    public String browse(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String diet,
            Model model) {

        model.addAttribute(
                "cooks",
                cookService.search(name, cuisine, diet)
        );

        model.addAttribute("name", name);
        model.addAttribute("cuisine", cuisine);
        model.addAttribute("diet", diet);
        model.addAttribute("cuisines", CUISINES);
        model.addAttribute("diets", DIETS);

        return "browse-cooks";
    }

    // individual cook profile
    @GetMapping("/browse-cooks/{id}")
    public String cookProfile(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "cook",
                cookService.getCookById(id)
        );

        return "cook-profile";
    }

    // friendly error page
    @ExceptionHandler(ResponseStatusException.class)
    public String handleError(
            ResponseStatusException e,
            HttpServletResponse response,
            Model model) {

        response.setStatus(e.getStatusCode().value());
        model.addAttribute("errorMessage", e.getReason());

        return "error";
    }

    // shared form setup
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

    private void startSession(
            HttpSession session,
            CustomerEntity customer) {

        session.setAttribute("customerId", customer.getId());
        session.setAttribute("customerName", customer.getFirstName());
    }

    private boolean isOwner(Long id, HttpSession session) {
        Object loggedIn = session.getAttribute("customerId");

        return loggedIn != null && loggedIn.equals(id);
    }
}
