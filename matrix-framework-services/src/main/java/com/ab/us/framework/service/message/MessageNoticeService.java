package com.ab.us.framework.service.message;

import java.util.Map;

/**
 * Created by ZhongChongtao on 2017/5/4.
 * 如果使用，需要添加注解 {@code @EnableMessageNoticeService}
 * 消息提醒相关接口
 *
 * @author ZhongChongtao
 * @since 3.0.0
 */
@SuppressWarnings("unused")
public interface MessageNoticeService {
    /**
     * 根据TemplateId，创建MessageNotice
     * 标题，描述，图片url，访问页面url
     *
     * @param extraParam       需要替换的属性，例如 name=XXX 将会替换掉需要替换字段的 {name} 标签
     * @param templateCategory 消息分类（在模板中配置）
     * @return 创建的消息实体对象
     * @since 2.11.0
     */
    public boolean createNewMessageNoticeByTemplate(String templateCategory, String templateSubCategory, String accountId, Map<String, String> extraParam);

    /**
     * 根据TemplateId，创建MessageNotice
     * 标题，描述，图片url，访问页面url
     *
     * @param templateCategory 消息分类（在模板中配置）
     * @return 创建的消息实体对象
     * @since 2.11.0
     */
    public boolean createNewMessageNoticeByTemplate(String templateCategory, String templateSubCategory, String accountId);

}
