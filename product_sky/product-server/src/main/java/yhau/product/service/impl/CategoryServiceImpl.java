package yhau.product.service.impl;

import org.springframework.stereotype.Service;
import yhau.product.model.ProductCategory;
import yhau.product.repository.ProductCategoryRepository;
import yhau.product.service.CategoryService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return productCategoryRepository.findByCategoryTypeIn(categoryTypeList);
    }
}
