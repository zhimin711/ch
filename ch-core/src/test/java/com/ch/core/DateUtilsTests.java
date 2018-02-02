package com.ch.core;

import com.ch.utils.DateUtils;
import org.junit.Test;

public class DateUtilsTests {

    @Test
    public void testWeek(){
        System.out.println(DateUtils.convertWorkdays("13",","));
    }
}
