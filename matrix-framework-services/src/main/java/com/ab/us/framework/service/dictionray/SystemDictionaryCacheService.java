package com.ab.us.framework.service.dictionray;


import com.ab.us.framework.service.entity.SystemDictionary;

import java.util.List;

/**
 * @author Zhongchongtao
 */
public interface SystemDictionaryCacheService {

    /**
     * 根据字典类型和字典Code，获取字典对象
     *
     * @param type 字典类型
     * @param code 字典code
     * @return 查找到的字典
     */
    public SystemDictionary getDictionaryByTypeAndCode(String type, String code);

    /**
     * 向字典库中添加字典
     *
     * @param category 字典分类
     * @param dictCode 字典编码
     * @param dictName 字典名称
     * @return 添加后的字典
     */
    public SystemDictionary createDictionary(String category, String dictCode, String dictName);


    /**
     * 根据字典类型和字典Code，获取字典的标准字段（dictName）
     *
     * @param type 字典类型
     * @param code 字典Code
     * @return 字典值
     */
    public String getDictionaryNameByTypeAndCode(String type, String code);
    
    /**
     * 根据字典类型和字典Code，获取字典对象列表
     *
     * @param type 字典类型
     * @return 查找到的字典
     */
    public List<SystemDictionary> getDictionaryListByTypeAndCode(String type, String code);
    
    /**
     * 根据字典类型获取字典对象列表
     *
     * @param type 字典类型
     * @return 查找到的字典
     */
    public List<SystemDictionary> getDictionaryListByType(String type);
}
