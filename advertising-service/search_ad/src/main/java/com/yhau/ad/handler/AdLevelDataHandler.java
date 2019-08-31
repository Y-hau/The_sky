package com.yhau.ad.handler;

import com.alibaba.fastjson.JSON;
import com.yhau.ad.dump.table.*;
import com.yhau.ad.index.DataTable;
import com.yhau.ad.index.IndexAware;
import com.yhau.ad.index.adplan.AdPlanIndex;
import com.yhau.ad.index.adplan.AdPlanObject;
import com.yhau.ad.index.adunit.AdUnitIndex;
import com.yhau.ad.index.adunit.AdUnitObject;
import com.yhau.ad.index.creative.CreativeIndex;
import com.yhau.ad.index.creative.CreativeObject;
import com.yhau.ad.index.creativeunit.CreativeUnitIndex;
import com.yhau.ad.index.creativeunit.CreativeUnitObject;
import com.yhau.ad.index.district.UnitDistrictIndex;
import com.yhau.ad.index.district.UnitDistrictObject;
import com.yhau.ad.index.interest.UnitItIndex;
import com.yhau.ad.index.keyword.UnitKeywordIndex;
import com.yhau.ad.mysql.constant.OpType;
import com.yhau.ad.units.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1.索引之间存在着层级的划分（依赖关系的划分） 用户层级->推广计划->推广限制->...
 * 2.加载全量索引其实是增量索引 “添加”的一种特殊实现
 */
@Slf4j
public class AdLevelDataHandler {

    private static <K, V> void handlerBinlogEvent(IndexAware<K, V> index,
                                                  K key, V value, OpType type) {
        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
            case OTHER:
                break;
        }
    }

    /**
     * 推广计划层级 2级
     *
     * @param planTable
     * @param type
     */
    public static void handleLevel2(AdPlanTable planTable, OpType type) {
        AdPlanObject planObject = new AdPlanObject(
                planTable.getId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );
        handlerBinlogEvent(
                DataTable.of(AdPlanIndex.class),
                planObject.getPlanId(),
                planObject,
                type
        );
    }

    /**
     * 推广创意层级 2级
     *
     * @param creativeTable
     * @param type
     */
    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );
        handlerBinlogEvent(
                DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type
        );
    }

    /**
     * 广告单元层级 3级
     *
     * @param unitTable
     * @param type
     */
    public static void handleLevel3(AdUnitTable unitTable, OpType type) {
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if (null == adPlanObject) {
            log.error("handleLevel3 found AdPlanObject error:{}", unitTable.getPlanId());
            return;
        }
        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                adPlanObject
        );
        handlerBinlogEvent(
                DataTable.of(AdUnitIndex.class),
                unitTable.getUnitId(),
                unitObject,
                type
        );
    }

    /**
     * 推广创意——推广单元层级 3级
     *
     * @param creativeUnitTable
     * @param type
     */
    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        if (null == unitObject || null == creativeObject) {
            log.error("AdCreativeUnitTable index error:{}", JSON.toJSONString(creativeUnitTable));
            return;
        }
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId()
        );
        handlerBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringConcat(
                        creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,
                type
        );
    }

    /**
     * 推广单元区域分类级别 4级
     *
     * @param unitDistrictTable
     * @param type
     */
    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("district index can not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error:{}", unitDistrictTable.getUnitId());
            return;
        }
        UnitDistrictObject unitDistrictObject = new UnitDistrictObject(
                unitDistrictTable.getUnitId(),
                unitDistrictTable.getProvince(),
                unitDistrictTable.getCity()
        );
        String key = CommonUtils.stringConcat(
                unitDistrictTable.getProvince(),
                unitDistrictTable.getCity()
        );
        Set<Long> value = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId()));
        handlerBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                key, value,
                type
        );
    }

    /**
     * 推广单元兴趣类别层级 4级
     *
     * @param unitItTable
     * @param type
     */
    public static void handleLevel4(AdUnitItTable unitItTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("interest index can not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if (null == unitObject) {
            log.error("AdUnitItTable index error: {}", unitItTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(Collections.singleton(unitItTable.getUnitId()));
        handlerBinlogEvent(
                DataTable.of(UnitItIndex.class),
                unitItTable.getItTag(),
                value,
                type
        );
    }

    /**
     * 关键字层级 4级
     *
     * @param keywordTable
     * @param type
     */
    public static void handleLevel4(AdUnitKeywordTable keywordTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("keyword index can not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(keywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}",
                    keywordTable.getUnitId());
            return;
        }
        Set<Long> value = new HashSet<>(
                Collections.singleton(keywordTable.getUnitId())
        );
        handlerBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                keywordTable.getKeyword(),
                value,
                type
        );
    }
}
