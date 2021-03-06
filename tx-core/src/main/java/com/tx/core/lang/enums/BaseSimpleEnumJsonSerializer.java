/*
 * 描          述:  <描述>
 * 修  改   人:  Administrator
 * 修改时间:  2015年12月1日
 * <修改描述:>
 */
package com.tx.core.lang.enums;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * IndexConfigType的Json转换逻辑
 * <功能详细描述>
 * 
 * @author  Administrator
 * @version  [版本号, 2015年12月1日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Deprecated
public class BaseSimpleEnumJsonSerializer extends
        JsonSerializer<BaseSimpleEnum> {
    
    /**
     * @param paramT
     * @param paramJsonGenerator
     * @param paramSerializerProvider
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public void serialize(BaseSimpleEnum value, JsonGenerator generator,
            SerializerProvider paramSerializerProvider) throws IOException,
            JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName("key");
        generator.writeString(value.getKey());
        generator.writeFieldName("name");
        generator.writeString(value.getName());
        generator.writeEndObject();
    }
    
}
