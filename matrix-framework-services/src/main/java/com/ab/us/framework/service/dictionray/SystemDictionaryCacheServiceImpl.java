package com.ab.us.framework.service.dictionray;

import com.ab.us.framework.core.exception.BaseException;
import com.ab.us.framework.core.spring.SpringApplicationContextHolder;
import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.core.utils.UUIDUtils;
import com.ab.us.framework.service.BaseService;
import com.ab.us.framework.service.entity.SystemDictionary;
import com.ab.us.framework.service.dao.SystemDictionaryDao;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author Zhongchongtao
 */
public class SystemDictionaryCacheServiceImpl extends BaseService implements SystemDictionaryCacheService {

    private final SystemDictionaryDao systemDictionaryDao;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    public SystemDictionaryCacheServiceImpl(SystemDictionaryDao systemDictionaryDao) {
        this.systemDictionaryDao = systemDictionaryDao;
    }

    @Cacheable(cacheNames = "systemDictionary", key = "#type+':'+#code")
    @Override
    public SystemDictionary getDictionaryByTypeAndCode(String type, String code) {
        SystemDictionaryCacheService systemDictionaryCacheService = SpringApplicationContextHolder.getBean(SystemDictionaryCacheService.class);
        List<SystemDictionary> systemDictionaries = systemDictionaryCacheService.getDictionaryListByTypeAndCode(type, code);
        //没有找到记录，返回null
        if (TypeChecker.isEmpty(systemDictionaries)) {
            //没有找到字典
            return null;
        }
        //只有一条记录，返回
        if (systemDictionaries.size() == 1) {
            return systemDictionaries.get(0);
        }
        logger.error("只需要一个字典，数据库中存在多个字典值，TYPE[{}],CODE[{}]", type, code);
        throw new BaseException();
    }

    @Override
    public void Init() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    @Cacheable(cacheNames = "systemDictionary", key = "#type+" + "#dictCode")
    public SystemDictionary createDictionary(String type, String dictCode, String dictName) {
        SystemDictionaryCacheService systemDictionaryCacheService = SpringApplicationContextHolder.getBean(SystemDictionaryCacheService.class);
        SystemDictionary dictionaryByTypeAndCode = systemDictionaryCacheService.getDictionaryByTypeAndCode(type, dictCode);
        if (TypeChecker.notNull(dictionaryByTypeAndCode)) {
            if(logger.isDebugEnabled()){
                logger.debug("字典已经存在");
            }
            return null;
        }
        SystemDictionary systemDictionary = new SystemDictionary();
        systemDictionary.setDictId(UUIDUtils.generate());
        systemDictionary.setDictCode(dictCode);
        systemDictionaryDao.insert(systemDictionary);
        return systemDictionary;
    }

    @Override
    public String getDictionaryNameByTypeAndCode(String type, String code) {
        SystemDictionaryCacheService systemDictionaryCacheService = SpringApplicationContextHolder.getBean(SystemDictionaryCacheService.class);
        SystemDictionary dictionaryByTypeAndCode = systemDictionaryCacheService.getDictionaryByTypeAndCode(type, code);
        if (TypeChecker.notNull(dictionaryByTypeAndCode)) {
            return dictionaryByTypeAndCode.getDictName();
        }
        return "";
    }

    @Override
    @Cacheable(cacheNames = "systemDictionaryList", key = "#type+':'+#code")
    public List<SystemDictionary> getDictionaryListByTypeAndCode(String type, String code) {
        SystemDictionary systemDictionary = new SystemDictionary();
        systemDictionary.setDictCode(code);
        systemDictionary.setDictType(type);
        //noinspection Duplicates,unchecked
        List<SystemDictionary> systemDictionaryList = (List<SystemDictionary>) systemDictionaryDao.queryByEntity(systemDictionary);
        if (TypeChecker.isNull(systemDictionaryList)) {
            logger.warn("没有找到字典TYPE [{}],CODE [{}]", type, code);
            return null;
        }
        return systemDictionaryList;
    }

    @Override
    public List<SystemDictionary> getDictionaryListByType(String type) {
        return SpringApplicationContextHolder.getBean(SystemDictionaryCacheService.class).getDictionaryListByTypeAndCode(type, null);
    }

}
