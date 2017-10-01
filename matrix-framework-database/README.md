使用说明：
1、此公共包依赖springboot请参考pox.xml文件。

2、JDBC配置及驱动类
   pox文件配置：
		<dependency>
			<groupId>com.oracle</groupId>
			<artifactId>ojdbc6</artifactId>
			<version>11.2.0.4.0-atlassian-hosted</version>
		</dependency>
   驱动配置：
   driverClassName=oracle.jdbc.OracleDriver

3、项目配置中资源目录下必须有数据源文件（db.properties），该文件中标注数据源名称。
   如：names=universesun_common,universesun_special,sso_account
   
4、默认数据源名称是UNIVERSESUN_COMMON，如果使用其它数据源，必须继承BaseService类，并在Iint函数中设置数据源名称。
	@Autowired
	private XXXXDao xXXXDao;

	@Override
	public void Init() {
		xXXXDao.setDataSchema(DataSchema.XX);
	}
   注：BaseDao不能使用注解方式，如果使用BaseDao，则上述代码修改为private BaseDao baseDao = new BaseDaoImpl();其中Init函数同上处理。
   
5、事务处理需要在DAO实现类中手动控制。如：
	private UsTransactionFactory factory = new UsTransactionFactory();

	@Override
	public Integer batchInsert(BaseEntity... entity) throws SQLException {
		UsTransactionDao transactionDao = (UsTransactionDao) factory.creatProxyInstance(new UsTransactionDao() {
			private Connection connect;

			@Override
			public int aopService(BaseEntity... BaseEntity) throws SQLException {
				return 0;
			}
		}, DBPool.getInstance().getDataSource(DataSchema.UNIVERSESUN_COMMON));

		return transactionDao.aopService(entity);
	}
	
6、日志处理默认采用logback，按日滚动。日志名称、路径及级别在个工程中设置。默认设置如下：
	app.name=undefined
	log.path=./logs
	log.level=debug

	





