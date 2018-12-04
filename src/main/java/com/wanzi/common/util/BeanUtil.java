package com.wanzi.common.util;

import com.alibaba.fastjson.JSON;
import com.wanzi.common.dto.Students;
import com.wanzi.common.dto.StudentsVO;
import com.wanzi.common.entity.ResultEnum;
import com.wanzi.common.exception.XwzServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: common
 * @description: 要求类名以DO为尾的类作为数据库层实体bean，类名以MO为尾的类作为系统传输层实体bean，类名以VO为尾的类作为服务端与前端交互的实体bean。
 * 需要在各个bean直接进行copy数据，除了傻瓜式的set/get or constructor来copy数据外，spring提供了直接copybean的工具类，
 * 原理其实就是通过java反射来根据目标bean的字段名调用其set方法来设值注入。除此之外，项目中还常见map与bean之间的转换
 * @author: zhangchuntao
 * @create: 2018-12-04
 **/
public class BeanUtil {

    /**
     *  只支持自定义实体集合拷贝
     *  应用：dto  vo 转换
     * @param sourse
     * @param target
     */
    public static void copyProperties(Object sourse, Object target){
        BeanUtils.copyProperties(sourse,target);
    }

    /**
     *  拷贝实体集合，sourceList
     *  只支持自定义实体集合拷贝
     *  应用：dto  vo 转换
     * @param sourceList
     * @param targetList
     * @param clazz
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void copyPropertiesList(List sourceList, List targetList, Class clazz) throws InstantiationException,IllegalAccessException {
        if (CollectionUtils.isEmpty(sourceList)) {
            throw new XwzServiceException(ResultEnum.SYSTEM_ERROR.getCode(),ResultEnum.SYSTEM_ERROR.getMsg());
        }
        for (Object items : sourceList) {
            Object target = clazz.newInstance();
            BeanUtils.copyProperties(items, target);
            targetList.add(target);
        }

    }

    /**
     * Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
     * @param map
     * @param obj
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static void transMap2Bean(Map<String, Object> map, Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (map.containsKey(key)) {
                Object value = map.get(key);
                // 得到property对应的setter方法
                Method setter = property.getWriteMethod();
                setter.invoke(obj, value);
            }
        }
    }

    /**
     * Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
     * @param obj
     * @return
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Map<String, Object> transBean2Map(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            // 过滤class属性
            if (!key.equals("class")) {
                // 得到property对应的getter方法
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj);
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        Students students = new Students();
        students.setAge(1);
        students.setClassName("终极一班");
        students.setId(1);
        students.setName("小强");

        StudentsVO studentsVO = new StudentsVO();

        copyProperties(students,studentsVO);
        System.out.println("copybean:::" + JSON.toJSONString(studentsVO));

        List studentsList = new ArrayList();
        studentsList.add(students);
        studentsList.add(students);

        List studentsVOList = new ArrayList();
        try {
            copyPropertiesList(studentsList,studentsVOList,Students.class);
            System.out.println("copyPropertiesList:::" + studentsVOList);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Map map = transBean2Map(students);
            System.out.println("transBean2Map:::" + JSON.toJSONString(map));

            StudentsVO studentsVO1 = new StudentsVO();
            transMap2Bean(map,studentsVO1);
            System.out.println("transMap2Bean:::" + JSON.toJSONString(studentsVO1));
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
