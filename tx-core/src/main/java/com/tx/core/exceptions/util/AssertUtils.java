/*
 * 描          述:  <描述>
 * 修  改   人:  brady
 * 修改时间:  2013-4-2
 * <修改描述:>
 */
package com.tx.core.exceptions.util;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import com.tx.core.exceptions.argument.IllegalArgException;
import com.tx.core.exceptions.argument.NullArgException;

/**
 * 断言工具类<br/>
 * <功能详细描述>
 * 
 * @author  brady
 * @version  [版本号, 2013-4-2]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AssertUtils {
    
    /**
     * 断言对应对象非空(支持：字符串，数组，集合，Map)<br/>
     * <功能详细描述>
     * @param str
     * @param message [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    public static void notEmpty(Object obj, String message,
            String... parameters) {
        //不为空
        notEmpty(obj, message, (Object[]) parameters);
    }
    
    /**
     * 断言对应对象非空(支持：字符串，数组，集合，Map)<br/>
     * <功能详细描述>
     * @param str
     * @param message [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    @SuppressWarnings("rawtypes")
    public static void notEmpty(Object obj, String message, Object[] parameters) {
        //不为空
        notNull(obj, message, parameters);
        if (obj instanceof String
                && StringUtils.isBlank((String) obj)) {
            throw new NullArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        } else if (obj instanceof Collection
                && CollectionUtils.isEmpty((Collection) obj)) {
            throw new NullArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        } else if (obj instanceof Map && MapUtils.isEmpty((Map) obj)) {
            throw new NullArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        } else if (obj instanceof Object[]
                && ArrayUtils.isEmpty((Object[]) obj)) {
            throw new NullArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        }
    }
    
    /**
     * 断言对应对象为空(支持：字符串，数组，集合，Map)<br/>
     * <功能详细描述>
     * @param str
     * @param message [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    public static void isEmpty(Object obj, String message,
            String... parameters) {
        //不为空
        isEmpty(obj, message, (Object[]) parameters);
    }
    
    /**
     * 断言对应字符串不能为空<br/>
     * <功能详细描述>
     * @param str
     * @param message [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    @SuppressWarnings("rawtypes")
    public static void isEmpty(Object obj, String message, Object[] parameters) {
        //不为空
        isNull(obj, message, parameters);
        if (obj instanceof String
                && !StringUtils.isBlank((String) obj)) {
            throw new NullArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        } else if (obj instanceof Collection
                && !CollectionUtils.isEmpty((Collection) obj)) {
            throw new NullArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        } else if (obj instanceof Map && !MapUtils.isEmpty((Map) obj)) {
            throw new NullArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        } else if (obj instanceof Object[]
                && !ArrayUtils.isEmpty((Object[]) obj)) {
            throw new NullArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        }
    }
    
    /**
     * 断言对象不为空<br/>
     * <功能详细描述>
     * @param object
     * @param message [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    public static void notNull(Object object, String message,
            String... parameters) {
        notNull(object, message, (Object[]) parameters);
    }
    
    /**
      * 断言对象不为空<br/>
      * <功能详细描述>
      * @param object [参数说明]
      * 
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void notNull(Object object, String message,
            Object[] parameters) {
        if (object == null) {
            throw new NullArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        }
    }
    
    /**
     * 断言对象为空<br/>
     * <功能详细描述>
     * @param object
     * @param message [参数说明]
     * 
     * @return void [返回类型说明]
     * @exception throws [异常类型] [异常说明]
     * @see [类、类#方法、类#成员]
    */
    public static void isNull(Object object, String message,
            String... parameters) {
        notNull(object, message, (Object[]) parameters);
    }
    
    /**
      * 断言对象为空<br/>
      * <功能详细描述>
      * @param object [参数说明]
      * 
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void isNull(Object object, String message, Object[] parameters) {
        if (object != null) {
            throw new IllegalArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        }
    }
    
    /**
      * 断言表达式是为真<br/>
      *     如果不为真，抛出参数非法异常IllegalArgException<br/>
      * <功能详细描述>
      * @param expression
      * @param message [参数说明]
      * 
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void isTrue(boolean expression, String message,
            String... parameters) {
        isTrue(expression, message, (Object[]) parameters);
    }
    
    /**
      * 断言表达式是为真<br/>
      *     如果不为真，抛出参数非法异常IllegalArgException<br/>
      * <功能详细描述>
      * @param expression [参数说明]
      * 
      * @return void [返回类型说明]
      * @exception throws [异常类型] [异常说明]
      * @see [类、类#方法、类#成员]
     */
    public static void isTrue(boolean expression, String message,
            Object[] parameters) {
        if (!expression) {
            throw new IllegalArgException(MessageFormatter.arrayFormat(message,
                    parameters).getMessage());
        }
    }
    
    /**
     * Assert that the provided object is an instance of the provided class.
     * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
     * @param clazz the required class
     * @param obj the object to check
     * @throws IllegalArgumentException if the object is not an instance of clazz
     * @see Class#isInstance
     */
    public static void isInstanceOf(Class<?> clazz, Object obj) {
        isInstanceOf(clazz, obj, "");
    }
    
    /**
     * Assert that the provided object is an instance of the provided class.
     * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
     * @param type the type to check against
     * @param obj the object to check
     * @param message a message which will be prepended to the message produced by
     * the function itself, and which may be used to provide context. It should
     * normally end in a ": " or ". " so that the function generate message looks
     * ok when prepended to it.
     * @throws IllegalArgumentException if the object is not an instance of clazz
     * @see Class#isInstance
     */
    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(message + ". Object of class ["
                    + (obj != null ? obj.getClass().getName() : "null")
                    + "] must be an instance of " + type);
        }
    }
    
    /**
     * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
     * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
     * @param superType the super type to check
     * @param subType the sub type to check
     * @throws IllegalArgumentException if the classes are not assignable
     */
    public static void isAssignable(Class superType, Class subType) {
        isAssignable(superType, subType, "");
    }
    
    /**
     * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
     * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
     * @param superType the super type to check against
     * @param subType the sub type to check
     * @param message a message which will be prepended to the message produced by
     * the function itself, and which may be used to provide context. It should
     * normally end in a ": " or ". " so that the function generate message looks
     * ok when prepended to it.
     * @throws IllegalArgumentException if the classes are not assignable
     */
    public static void isAssignable(Class<?> superType, Class<?> subType,
            String message) {
        notNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message + subType
                    + " is not assignable to " + superType);
        }
    }
    
    /**
     * Assert a boolean expression, throwing {@code IllegalStateException}
     * if the test result is {@code false}. Call isTrue if you wish to
     * throw IllegalArgumentException on an assertion failure.
     * <pre class="code">Assert.state(id == null, "The id property must not already be initialized");</pre>
     * @param expression a boolean expression
     * @param message the exception message to use if the assertion fails
     * @throws IllegalStateException if expression is {@code false}
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }
    
    /**
     * Assert a boolean expression, throwing {@link IllegalStateException}
     * if the test result is {@code false}.
     * <p>Call {@link #isTrue(boolean)} if you wish to
     * throw {@link IllegalArgumentException} on an assertion failure.
     * <pre class="code">Assert.state(id == null);</pre>
     * @param expression a boolean expression
     * @throws IllegalStateException if the supplied expression is {@code false}
     */
    public static void state(boolean expression) {
        state(expression,
                "[Assertion failed] - this state invariant must be true");
    }
}