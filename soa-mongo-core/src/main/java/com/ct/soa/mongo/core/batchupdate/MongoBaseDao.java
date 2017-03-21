package com.ct.soa.mongo.core.batchupdate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.ct.soa.mongo.core.document.SequenceId;
import com.ct.soa.mongo.core.model.Page;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

@Component
public class MongoBaseDao<T> {
	
	private Logger logger = LoggerFactory.getLogger(MongoBaseDao.class);
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	
	public T save(final T entity) {
		if(entity != null) {
            ReflectionUtils.doWithFields(entity.getClass(), new ReflectionUtils.FieldCallback() {
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field);
                    //设置自增ID
                    field.set(entity, getNextId(entity.getClass().getSimpleName()));
                 }
            });
            mongoTemplate.insert(entity);
        }
        return entity;  
    }
	
	/**
	  * 获取下一个自增ID
	  * @author yinjihuan
	  * @param collName  集合名
	  * @return
	  */
	 private Long getNextId(String collName) {
	     Query query = new Query(Criteria.where("collName").is(collName));
	     Update update = new Update();
	     update.inc("seqId", 1);
	     FindAndModifyOptions options = new FindAndModifyOptions();
	     options.upsert(true);
	     options.returnNew(true);
	     SequenceId seqId = mongoTemplate.findAndModify(query, update, options, SequenceId.class);
	     return seqId.getSeqId();
	 }
  
    public T findById(String id) {  
        return mongoTemplate.findById(id, this.getEntityClass());  
    }  
  
    public T findById(String id, String collectionName) {  
        return mongoTemplate.findById(id, this.getEntityClass(), collectionName);  
    }  
  
    public List<T> findAll() {  
        return mongoTemplate.findAll(this.getEntityClass());  
    }  
  
    public List<T> findAll(String collectionName) {  
        return mongoTemplate.findAll(this.getEntityClass(), collectionName);  
    }  
  
    public List<T> find(Query query) {  
        return mongoTemplate.find(query, this.getEntityClass());  
    }  
  
    public T findOne(Query query) {  
        return mongoTemplate.findOne(query, this.getEntityClass());  
    }  
  
    public Page<T> findPage(Page<T> page, Query query) {  
        //如果没有条件 则所有全部  
        query=query==null?new Query(Criteria.where("_id").exists(true)):query;  
        long count = this.count(query);  
        // 总数  
        page.setTotalCount((int) count);  
        int currentPage = page.getCurrentPage();  
        int pageSize = page.getPageSize();  
        query.skip((currentPage - 1) * pageSize).limit(pageSize);  
        List<T> rows = this.find(query);  
        page.build(rows);  
        return page;  
    }  
  
    public long count(Query query) {  
        return mongoTemplate.count(query, this.getEntityClass());  
    }  
  
    public WriteResult update(Query query, Update update) {  
        if (update==null) {  
            return null;  
        }  
        return mongoTemplate.updateMulti(query, update, this.getEntityClass());  
    }  
  
    public T updateOne(Query query, Update update) {  
        if (update==null) {  
            return null;  
        }  
        return mongoTemplate.findAndModify(query, update, this.getEntityClass());  
    }  
  
    public WriteResult update(T entity) {  
        Field[] fields = this.getEntityClass().getDeclaredFields();  
        if (fields == null || fields.length <= 0) {  
            return null;  
        }  
        Field idField = null;  
        // 查找ID的field  
        for (Field field : fields) {  
            if (field.getName() != null  
                    && "id".equals(field.getName().toLowerCase())) {  
                idField = field;  
                break;  
            }  
        }  
        if (idField == null) {  
            return null;  
        }  
        idField.setAccessible(true);  
        String id=null;  
        try {  
            id = (String) idField.get(entity);  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        }  
        if (id == null || "".equals(id.trim()))  
            return null;  
        // 根据ID更新  
        Query query = new Query(Criteria.where("_id").is(id));  
        Update update = getUpdateObj(entity);  
        if (update == null) {  
            return null;  
        }  
        return mongoTemplate.updateFirst(query, update, getEntityClass());  
    }  
  
    public void remove(Query query) {  
        mongoTemplate.remove(query, this.getEntityClass());  
    }
    
	/**
	 * 批量更新
	 * @param ordered 如果为true,一条语句更新失败，剩下的语句将不再执。如果为false,一条语句更新失败，剩下的将继续执行。默认为true。
	 * @return
	 */
	public int bathUpdate(String collName, List<BathUpdateOptions> options, boolean ordered) {
		return doBathUpdate(mongoTemplate.getCollection(collName), collName, options, ordered);
	}
	
	public int bathUpdate(DBCollection dbCollection, String collName, List<BathUpdateOptions> options, boolean ordered) {
		return doBathUpdate(dbCollection, collName, options, ordered);
	}
	
	public int bathUpdate(Class<?> entityClass, List<BathUpdateOptions> options, boolean ordered) {
		String collectionName = determineCollectionName(entityClass);
		return doBathUpdate(mongoTemplate.getCollection(collectionName), collectionName, options, ordered);
	}
	
	public int bathUpdate(DBCollection dbCollection, Class<?> entityClass, List<BathUpdateOptions> options, boolean ordered) {
		return doBathUpdate(dbCollection, determineCollectionName(entityClass), options, ordered);
	}
	
	
	public int bathUpdate(String collName, List<BathUpdateOptions> options) {
		return doBathUpdate(mongoTemplate.getCollection(collName), collName, options, true);
	}
	
	public int bathUpdate(DBCollection dbCollection, String collName, List<BathUpdateOptions> options) {
		return doBathUpdate(dbCollection, collName, options, true);
	}
	
	public int bathUpdate(Class<?> entityClass, List<BathUpdateOptions> options) {
		String collectionName = determineCollectionName(entityClass);
		return doBathUpdate(mongoTemplate.getCollection(collectionName), collectionName, options, true);
	}
	
	public int bathUpdate(DBCollection dbCollection, Class<?> entityClass, List<BathUpdateOptions> options) {
		return doBathUpdate(dbCollection, determineCollectionName(entityClass), options, true);
	}
	
	/** 
     * 获得泛型类 
     */  
    @SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {  
        return getSuperClassGenricType(getClass(),0);  
    } 
    
    /** 
     * 根据对象获得mongodb Update语句 
     * 除id字段以外，所有被赋值的字段都会成为修改项 
     */  
    private Update getUpdateObj(final Object obj) {  
        if (obj == null)  
            return null;  
        Field[] fields = obj.getClass().getDeclaredFields();  
        Update update = null;  
        boolean isFirst = true;  
        for (Field field : fields) {  
            field.setAccessible(true);  
            try {  
                Object value = field.get(obj);  
                if (value != null) {  
                    if ("id".equals(field.getName().toLowerCase())|| "serialversionuid".equals(field.getName().toLowerCase()))  
                        continue;  
                    if (isFirst) {  
                        update = Update.update(field.getName(),value);  
                        isFirst = false;  
                    } else {  
                        update = update.set(field.getName(), value);  
                    }  
                }  
  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            }  
        }  
        return update;  
    }  
    
    @SuppressWarnings("rawtypes")  
    private Class getSuperClassGenricType(final Class clazz,  
            final int index) {  
  
        Type genType = clazz.getGenericSuperclass();  
  
        if (!(genType instanceof ParameterizedType)) {  
            logger.warn(clazz.getSimpleName()  
                    + "'s superclass not ParameterizedType");  
            return Object.class;  
        }  
  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();  
  
        if (index >= params.length || index < 0) {  
            logger.warn("Index: " + index + ", Size of "  
                    + clazz.getSimpleName() + "'s Parameterized Type: "  
                    + params.length);  
            return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {  
            logger.warn(clazz.getSimpleName()  
                    + " not set the actual class on superclass generic parameter");  
            return Object.class;  
        }  
  
        return (Class) params[index];  
    }
    
	private int doBathUpdate(DBCollection dbCollection, String collName, List<BathUpdateOptions> options, boolean ordered) {
		DBObject command = new BasicDBObject();
		command.put("update", collName);
		List<BasicDBObject> updateList = new ArrayList<BasicDBObject>();
		for (BathUpdateOptions option : options) {
			BasicDBObject update = new BasicDBObject();
			update.put("q", option.getQuery().getQueryObject());
			update.put("u", option.getUpdate().getUpdateObject());
			update.put("upsert", option.isUpsert());
			update.put("multi", option.isMulti());
			updateList.add(update);
		}
		command.put("updates", updateList);
		command.put("ordered", ordered);
		CommandResult commandResult = dbCollection.getDB().command(command);
		return Integer.parseInt(commandResult.get("n").toString());
	}
	
	private String determineCollectionName(Class<?> entityClass) {
		if (entityClass == null) {
			throw new InvalidDataAccessApiUsageException(
					"No class parameter provided, entity collection can't be determined!");
		}
		String collName = entityClass.getSimpleName();
		if(entityClass.isAnnotationPresent(Document.class)) {
			Document document = entityClass.getAnnotation(Document.class);
			collName = document.collection();
		} else {
			collName = collName.replaceFirst(collName.substring(0, 1),collName.substring(0, 1).toLowerCase()) ;
		}
		return collName;
	}
}