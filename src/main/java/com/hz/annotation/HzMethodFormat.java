package com.hz.annotation;

import java.lang.annotation.*;

/**
 * @author HUANG  ZHI
 * @description/note xxx
 * @descriptionDetail/noteDetail xxx
 * @copyright
 * @createDate 2021/1/21
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HzMethodFormat {
}
