package com.yhau.ad.index.district;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广告单元区域对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitDistrictObject {
    private Long unitId;
    private String province;
    private String city;
}
