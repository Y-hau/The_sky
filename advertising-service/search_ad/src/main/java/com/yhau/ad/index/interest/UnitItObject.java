package com.yhau.ad.index.interest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广告单元兴趣对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitItObject {
    private Long unitId;
    private String itTag;
}
