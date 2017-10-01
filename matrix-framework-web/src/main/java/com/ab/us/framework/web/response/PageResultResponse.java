package com.ab.us.framework.web.response;

import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;

import java.util.List;

/**
 * Created by ZhongChongtao on 2017/2/13.
 */
public class PageResultResponse<T> extends BodyResponse<PaginationModel<T>> {

    /**
     * 准备有结果的相应信息信息
     *
     * @param base   响应基本信息
     * @param data   相应数据
     * @param params Message参数替换（根据枚举消息，确定替换的内容）
     * @return 相应结果
     */
    public static <T> PageResultResponse<T> preparePagination(BaseResponse base, List<T> data, PaginationInfo pagination, String... params) {
        String message = TypeChecker.isEmpty(params) ? base.getMessage() : StringUtil.replaceByIndex(base.getMessage(), params);
        PageResultResponse<T> pageResultResponse = new PageResultResponse<>();
        pageResultResponse.setCode(base.getCode());
        pageResultResponse.setMessage(message);
        PaginationModel<T> paginationInfo = new PaginationModel<>();
        paginationInfo.setDetails(data);
        pageResultResponse.setData(paginationInfo);
        return pageResultResponse;
    }
}
