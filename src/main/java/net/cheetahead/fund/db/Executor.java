package net.cheetahead.fund.db;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Column;

public class Executor {

    @Resource
    private static JdbcTemplate jdbcTemplate;

    public static int change(String sql, Object[] params) throws Exception {
        return jdbcTemplate.update(sql, params);
    }

    public static <T> List<T> fetch(String sql, Object[] params, Class<T> model) throws Exception {
        return jdbcTemplate.query(sql, params, new RowMapper<T>() {

            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (rs != null) {
                    rs.absolute(rowNum);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    Field[] fields = model.getDeclaredFields();
                    try {
                        T bean = model.newInstance();
                        for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
                            String columnName = rsmd.getColumnName(_iterator + 1);
                            Object columnValue = rs.getObject(_iterator + 1);
                            for (Field field : fields) {
                                if (field.isAnnotationPresent(Column.class)) {
                                    Column column = field.getAnnotation(Column.class);
                                    if (column.name().equalsIgnoreCase(columnName) && columnValue != null) {
                                        BeanUtils.setProperty(bean, field.getName(), columnValue);
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    // throw some error
                }
                return null;
            }
        });
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
