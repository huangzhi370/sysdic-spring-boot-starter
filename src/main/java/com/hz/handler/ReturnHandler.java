package com.hz.handler;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hz.annotation.HzFiledFormat;
import com.hz.annotation.HzMethodFormat;
import com.hz.resp.ResponseData;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author huangzhi
 */
@Component
public class ReturnHandler extends RequestResponseBodyMethodProcessor implements HandlerMethodReturnValueHandler {

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException {
        if(returnValue.getClass().getName().indexOf("ResponseData")!=-1) {
            ResponseData<Object> data = new ResponseData<>();
            BeanUtils.copyProperties(returnValue,data);
            Object dataInfo = data.getData();
            if(dataInfo instanceof IPage){
                IPage<Object> pageData=(IPage<Object>)dataInfo;
                List<Object> listData=pageData.getRecords();
                sysListDicInfo(listData);
            }else if(dataInfo instanceof List){
                List<Object> listData=(List<Object>)dataInfo;
                sysListDicInfo(listData);
            }else if(dataInfo instanceof Map){
                Map<Object,Object> mapData=(Map<Object,Object>)dataInfo;
                Collection<Object> values =  mapData.values();
                for(Object obj:values){
                    sysDicInfo(obj);
                }
            }
        }
        super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

    public void sysListDicInfo(List<Object> listData){
        listData.stream().forEach(obj->{
            sysDicInfo(obj);
        });
    }

    public void sysDicInfo(Object obj){
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field:fields){
            HzFiledFormat fieldAnnotation = field.getDeclaredAnnotation(HzFiledFormat.class);
            if(null!=fieldAnnotation){
                setFiledValue(fields,field,obj,fieldAnnotation);
            }

        }
    }

    public void setFiledValue(Field[] fields,Field field,Object obj,HzFiledFormat fieldAnnotation){
        for (Field field1:fields){
            if(field1.getName().equals(fieldAnnotation.sysDicName())){
                try {
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.makeAccessible(field1);
                    ReflectionUtils.setField(field1,obj,field.get(obj).equals("1")?"男":"女");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        Method method = returnType.getMethod();
        if (method == null || !method.isAnnotationPresent(HzMethodFormat.class)
                || method.getReturnType().isAssignableFrom(Exception.class)) {
            return false;
        }
        return true;
    }

    public ReturnHandler(List<HttpMessageConverter<?>> converters) {
        super(converters);
    }

}