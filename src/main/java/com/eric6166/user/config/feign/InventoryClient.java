package com.eric6166.user.config.feign;

import com.eric6166.common.config.feign.FeignClientConfig;
import com.eric6166.common.exception.AppException;
import com.eric6166.common.utils.Const;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.client.config.inventory-client.name}",
        url = "${feign.client.config.inventory-client.url}",
        configuration = FeignClientConfig.class)
public interface InventoryClient {

    @GetMapping("/product/test")
    String productTest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws AppException;

    @GetMapping("/product")
    Object productFindAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                          @RequestParam(required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER_STRING)
//                          @Min(value = Const.DEFAULT_PAGE_NUMBER)
//                          @Max(value = Const.DEFAULT_MAX_INTEGER)
                                  Integer pageNumber,
                          @RequestParam(required = false, defaultValue = Const.DEFAULT_PAGE_SIZE_STRING)
//                          @Min(value = Const.DEFAULT_PAGE_SIZE)
//                          @Max(value = Const.MAXIMUM_PAGE_SIZE)
                                  Integer pageSize,
                          @RequestParam(required = false, defaultValue = Const.DEFAULT_SORT_COLUMN)
//                          @ValidString(values = {
//                                  Constants.SORT_COLUMN_ID,
//                                  Constants.SORT_COLUMN_NAME,
//                                  Constants.SORT_COLUMN_DESCRIPTION,
//                                  Constants.SORT_COLUMN_PRICE,
//                                  Constants.SORT_COLUMN_CATEGORY_ID,
//                          })
                                  String sortColumn,
                          @RequestParam(required = false, defaultValue = Const.DEFAULT_SORT_DIRECTION)
//                          @ValidEnumString(value = Sort.Direction.class, caseSensitive = false)
                                  String sortDirection) throws AppException;

    @GetMapping("/admin/test")
    String adminTest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws AppException;


}
