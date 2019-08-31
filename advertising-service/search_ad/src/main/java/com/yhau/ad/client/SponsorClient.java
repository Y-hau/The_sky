package com.yhau.ad.client;

import com.yhau.ad.client.vo.AdPlan;
import com.yhau.ad.client.vo.AdPlanGetRequest;
import com.yhau.ad.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "sponsor-ad",
        fallback = SponsorClientHystrix.class)
public interface SponsorClient {

    @RequestMapping(value = "/sponsor/get/adPlan",
            method = RequestMethod.POST)
    CommonResponse<List<AdPlan>> getAdPlans(
            @RequestBody AdPlanGetRequest request);
}
