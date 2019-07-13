package yhau.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yhau.product.model.ProductCategory;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
