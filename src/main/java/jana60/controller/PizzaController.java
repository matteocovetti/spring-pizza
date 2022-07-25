package jana60.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jana60.model.Pizza;
import jana60.repository.PizzaRepository;

@Controller
@RequestMapping("/")
public class PizzaController {

	@Autowired
	private PizzaRepository repo;

	@GetMapping
	public String pizzaList(Model model) {
		model.addAttribute("pizzaList", repo.findAll());
		return "/pizza/list";
	}

	@GetMapping("/add")
	public String pizzaForm(Model model) {
		model.addAttribute("pizza", new Pizza());
		return "/pizza/edit";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult br, Model model) {
		boolean hasErrors = br.hasErrors();
		boolean validateName = true;

		if (formPizza.getId() != null) {
			Pizza pizzaBeforeUpdate = repo.findById(formPizza.getId()).get();
			if (pizzaBeforeUpdate.getNome().equals(formPizza.getNome())) {
				validateName = false;
			}
		}

		if (validateName && repo.countByNome(formPizza.getNome()) > 0) {
			br.addError(new FieldError("pizza", "nome", "Pizza's name must be unique"));
			hasErrors = true;
		}

		if (hasErrors) {
			return "/pizza/edit";
		} else {
			try {
				repo.save(formPizza);
			} catch (Exception e) {
				model.addAttribute("errorMessage", "Unable to save the pizza");
				return "/pizza/edit";
			}
			return "redirect:/";
		}

	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer pizzaId, RedirectAttributes ra) {
		Optional<Pizza> result = repo.findById(pizzaId);
		if (result.isPresent()) {
			repo.delete(result.get());
			ra.addFlashAttribute("successMessage", "Pizza " + result.get().getNome() + " deleted");
			return "redirect:/";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza con id " + pizzaId + " not present");
		}

	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Integer pizzaId, Model model) {
		Optional<Pizza> result = repo.findById(pizzaId);
		if (result.isPresent()) {

			model.addAttribute("pizza", result.get());
			return "/pizza/edit";
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pizza con id " + pizzaId + " not present");
		}

	}
}
