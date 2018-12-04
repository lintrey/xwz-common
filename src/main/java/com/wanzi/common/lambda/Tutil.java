package com.wanzi.common.lambda;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @program: common
 * @description: ${description}
 * @author: zhangchuntao
 * @create: 2018-09-10
 **/
@Component
@Data
public class Tutil {

    private Tutil.IMath iMath;

    public interface IMath {
        int operation(int a,int b);
    }


}
