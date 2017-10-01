package com.ab.us.framework.db.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ab.us.framework.db.entity.BaseEntity;
import com.ab.us.framework.db.entity.DataSchema;
import com.ab.us.framework.db.util.PageInfo;

/**
 * @ClassName: BaseDao
 * @Description: TODO(dao公共接口)
 * @author xusisheng
 * @date 2017-01-23 10:35:41
 * @note 调用基础BaseDao的情况下，必须设置数据源，否则出现异常错误
 */
public interface BaseDao {

	public void setDataSource(DataSource dataSource);

	public void setDataSchema(DataSchema dataSchema);

	/**
	 * 添加数据入库
	 *
	 * @param entity
	 * @return
	 */
	public long insert(BaseEntity entity);

	/**
	 * 插入数据并返回主键
	 *
	 * @param entity
	 * @return
	 */
	public Integer insertForKeys(BaseEntity entity);

	/**
	 * 更新数据记录 ,按主键更新
	 *
	 * @param entity
	 * @return
	 */
	public long updateByPk(BaseEntity entity);

	/**
	 * 更新数据记录
	 *
	 * @param entity
	 * @param isPk 是否为主键
	 * @return
	 */
	public long update(BaseEntity entity, boolean isPk);

	/**
	 * 更新数据记录
	 *
	 * @param sql 
	 * @return
	 */
	public long update(String sql);

	/**
	 * 更新数据记录
	 *
	 * @param sql
	 * @param args
	 * @return
	 */
	public long update(String sql, Object... args);

	/**
	 * 批次更新
	 *
	 * @param sql
	 * @param objs
	 * @return
	 */
	public int[] batchUpdate(String sql, Object[][] objs);

	/**
	 * 删除数据
	 *
	 * @param entity
	 * @return
	 * @note 按主键删除数据
	 */
	public long delete(BaseEntity entity);

	/**
	 * 删除数据
	 *
	 * @param entity
	 * @param reqPk 是否按主键删除
	 * @return
	 */
	public long delete(BaseEntity entity, boolean reqPk);

	/**
	 * 查询对应的数据
	 *
	 * @param entity
	 * @return
	 */
	public List<? extends BaseEntity> queryByEntity(BaseEntity entity);

	/**
	 * 根据查询域查出对应的对象
	 *
	 * @param entity
	 * @return
	 */
	public <T> BaseEntity queryEntity(BaseEntity entity);

	/**
	 * 根据查询域查出对应的对象
	 *
	 * @param entity
	 * @return
	 */
	public Map<String, Object> queryMapEntity(BaseEntity entity);

	/**
	 * 自定义SQL查出对应的对象
	 *
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> T queryEntity(Class<T> clazz, String sql, Object[] params);

	/**
	 * 查询统计
	 *
	 * @param entity
	 * @return
	 */
	public long queryCount(BaseEntity entity);

	/**
	 * 插入并返回自增主键
	 *
	 * @param sql
	 * @param args
	 * @return
	 */
	public int insertForKeys(String sql, Object... args);

	/**
	 * 返回查询数量
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	public long count(String sql, Object... params);

	/**
	 * 分页查询
	 *
	 * @param sql
	 * @param page 第几页，从1开始
	 * @param count 每页条数
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findPage(String sql, int page, int count, Object... params);

	/**
	 * 获取分页信息
	 *
	 * @param entity 实体信息
	 * @param page 第几页，从1开始
	 * @param count 每页条数
	 */
	public <T> PageInfo<T> paginate(Class<T> clazz, BaseEntity entity, int page, int count);

	/**
	 * 自定义SQL获取分页信息
	 *
	 * @param clazz
	 * @param sql 自定义SQL
	 * @param page 第几页，从1开始
	 * @param count 每页条数
	 * @param params
	 * @return
	 */
	public <T> PageInfo<T> paginate(Class<T> clazz, String sql, int page, int count, Object... params);

	/**
	 * 查询SQL执行
	 *
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T> List<T> query(Class<T> clazz, String sql, Object... params);

	/**
	 * 用于自定义查询SQL执行
	 *
	 * @param sql 自定义查询SQL
	 * @param params 参数信息
	 *
	 */
	public Map<String, Object> query(String sql, Object... params);

	/**
	 * 用于自定义查询SQL执行
	 *
	 * @param sql 自定义查询SQL
	 * @param params 参数信息
	 *
	 */
	public List<Map<String, Object>> queryList(String sql, Object... params);

	/**
	 * 执行sql
	 *
	 * @param sql
	 * @param args
	 * @return
	 */
	public Boolean execute(String sql);

}
