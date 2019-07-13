package yhau.product.service;

import com.yhau.product.common.DecreaseStockInput;
import com.yhau.product.common.ProductInfoOutput;
import org.springframework.stereotype.Service;
import yhau.product.model.ProductInfo;

import java.util.List;

@Service
public interface ProductService {
    /**
     * 查询所有在架商品列表
     */
    List<ProductInfo> findUpAll();

    /**
     * 查询商品列表
     *
     * @param productIdList
     * @return
     */
    List<ProductInfoOutput> findList(List<String> productIdList);

    /**
     * 扣库存
     *
     * @param decreaseStockInputList
     */
    void decreaseStock(List<DecreaseStockInput> decreaseStockInputList);
}
