//package com.eric6166.user.config.keycloak;
//
//import com.eric6166.common.config.feign.FeignClientConfig;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@FeignClient(name = "${feign.client.config.inventory-client.name}",
//        url = "${feign.client.config.inventory-client.url}",
//        configuration = FeignClientConfig.class)
//public interface KeycloakClient {
//
//    @GetMapping("/product/test")
//    String productTest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws AppException;
////
////    @GetMapping("/product")
////    Object productFindAll(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
////                          @RequestParam(required = false, defaultValue = BaseConst.DEFAULT_PAGE_NUMBER_STRING)
//////                          @Min(value = Const.DEFAULT_PAGE_NUMBER)
//////                          @Max(value = Const.DEFAULT_MAX_INTEGER)
////                                  Integer pageNumber,
////                          @RequestParam(required = false, defaultValue = BaseConst.DEFAULT_PAGE_SIZE_STRING)
//////                          @Min(value = Const.DEFAULT_PAGE_SIZE)
//////                          @Max(value = Const.MAXIMUM_PAGE_SIZE)
////                                  Integer pageSize,
////                          @RequestParam(required = false, defaultValue = BaseConst.DEFAULT_SORT_COLUMN)
//////                          @ValidString(values = {
//////                                  Constants.SORT_COLUMN_ID,
//////                                  Constants.SORT_COLUMN_NAME,
//////                                  Constants.SORT_COLUMN_DESCRIPTION,
//////                                  Constants.SORT_COLUMN_PRICE,
//////                                  Constants.SORT_COLUMN_CATEGORY_ID,
//////                          })
////                                  String sortColumn,
////                          @RequestParam(required = false, defaultValue = BaseConst.DEFAULT_SORT_DIRECTION)
//////                          @ValidEnumString(value = Sort.Direction.class, caseSensitive = false)
////                                  String sortDirection) throws AppException;
////
////    @GetMapping("/admin/test")
////    String adminTest(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws AppException;
//
//
//}
