package com.ct.soa.web.framework.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import com.ct.soa.web.framework.model.PageParam;

/**
 * mybaits分页拦截器
 * 
 * @author liuqs
 *
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }),
		@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class MybatisPageInterceptor implements Interceptor {
	private static final String DELE_MAPPEDSTMT = "delegate.mappedStatement";
	private static final String DELE_BOUNDSQL = "delegate.boundSql";

	/**
	 * 拦截逻辑
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();

		if (target instanceof StatementHandler) {
			StatementHandler stmtHandler = (StatementHandler) target;

			MetaObject mb = SystemMetaObject.forObject(stmtHandler);
			MappedStatement ms = (MappedStatement) mb.getValue(DELE_MAPPEDSTMT);

			BoundSql boundSql = (BoundSql) mb.getValue(DELE_BOUNDSQL);
			
			Object parameterObject = boundSql.getParameterObject();
			
			if(parameterObject instanceof PageParam){
				PageParam pi = (PageParam)parameterObject;
				
				String sql = boundSql.getSql();

				// 生成查询sql为分页sql
				String finalPageSql = genPageSql(sql, pi.offset(), pi.size());

				if (pi.isUseCount()) {
					// 生成分页总数sql
					String finalCountSql = genCountSql(sql);

					// 执行查询记录数sql
					int count = execCountSql(invocation, boundSql, ms, finalCountSql);
					pi.setCount(count);
				}
				// 更改原始sql为分页sql
				mb.setValue("delegate.boundSql.sql", finalPageSql);
			}
		}

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties arg0) {
	}

	/**
	 * 执行记录数查询sql
	 * 
	 * @param invocation
	 * @param boundSql
	 * @param ms
	 * @param sql
	 * @param pp
	 */
	private int execCountSql(Invocation invocation, BoundSql boundSql, MappedStatement ms, String sql) {
		Connection connection = (Connection) invocation.getArgs()[0];

		PreparedStatement ps = null;
		ResultSet rs = null;

		int ret = 0;
		try {
			// 获取原始参数信息
			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
			Object parameterObject = boundSql.getParameterObject();

			// 初始化查询sql中的配置，进行sql参数赋值
			BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), sql, parameterMappings, parameterObject);
			ParameterHandler parameterHandler = new DefaultParameterHandler(ms, parameterObject, countBoundSql);
			
			// 执行查询
			ps = connection.prepareStatement(sql);
			parameterHandler.setParameters(ps);

			rs = ps.executeQuery();
			if (rs.next()) {
				ret = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
		}

		return ret;
	}

	/**
	 * 生成分页总数sql
	 * 
	 * @param sql
	 * @return
	 */
	private String genCountSql(String sql) {
		int idx = sql.indexOf(" from ");
		if (idx<=0) {
			throw new IllegalStateException("分页找不到[from]关键词，请使用小写from");
		}
		StringBuffer sb = new StringBuffer("select count(*) from ");
		if (sql.lastIndexOf("order") > sql.lastIndexOf(")")) {
			sb.append(sql.substring(sql.indexOf(" from ") + 6, sql.lastIndexOf("order")));
		} else {
			sb.append(sql.substring(sql.indexOf(" from ") + 6));
		}

		return sb.toString();
	}

	/**
	 * 生成当前查询sql为分页sql
	 * 
	 * @param sql
	 *            原始sql
	 * @param start
	 *            开始索引
	 * @param size
	 *            分页大小
	 * @return
	 */
	private String genPageSql(String sql, int start, int size) {
		StringBuffer sb = new StringBuffer();
		sb.append(sql).append(" limit ").append(start).append(" , ").append(size);

		return sb.toString();
	}
}
