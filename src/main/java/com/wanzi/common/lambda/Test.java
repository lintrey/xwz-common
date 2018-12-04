package com.wanzi.common.lambda;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-09-10
 **/
public class Test {
    @Autowired
    private Tutil tutil = tutil = new Tutil();

    public  void o() {

        tutil.setIMath(
                (int a,int b) -> { return  a + b;}
        );
        Tutil.IMath iMath = tutil.getIMath();

        System.out.println(iMath.operation(1,2));


    }


    public void testLocalDateTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.toString());
    }


    public static void main(String[] args) {
        //lambda
        Test test = new Test();
        test.o();

        //localdatetime
        test.testLocalDateTime();
    }
}
