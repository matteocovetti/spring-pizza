package jana60.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import jana60.model.Ingredienti;

public interface IngredientiRepository extends CrudRepository<Ingredienti, Integer> {

	public List<Ingredienti> findAllByOrderByName();
}
