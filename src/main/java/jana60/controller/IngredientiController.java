package jana60.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.model.Ingredienti;
import jana60.repository.IngredientiRepository;

@Controller
@RequestMapping("/ingredienti")
public class IngredientiController {

	@Autowired
	private IngredientiRepository repo;

	@GetMapping
	public String ingredientiList(Model model) {
		model.addAttribute("ingredienti", repo.findAllByOrderByName());
		model.addAttribute("newIngrediente", new Ingredienti());
		return "/ingredienti/list";
	}

	@PostMapping("/save")
	public String saveIngrediente(@Valid @ModelAttribute("newIngrediente") Ingredienti formIngrediente,
			BindingResult br, Model model) {
		if (br.hasErrors()) {
			model.addAttribute("ingredienti", repo.findAllByOrderByName());
			return "/ingredienti/list";
		} else {
			repo.save(formIngrediente);
			return "redirect:/ingredienti";
		}

	}
}
