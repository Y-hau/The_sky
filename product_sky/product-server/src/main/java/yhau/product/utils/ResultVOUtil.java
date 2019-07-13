package yhau.product.utils;

import yhau.product.vo.ProductVO;
import yhau.product.vo.ResultVO;

import java.util.List;

public class ResultVOUtil {

    public static ResultVO success(List<ProductVO> productVOs) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(productVOs);
        return resultVO;
    }

}
