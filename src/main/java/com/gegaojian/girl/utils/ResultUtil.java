package com.gegaojian.girl.utils;

import com.gegaojian.girl.domain.Result;

public class ResultUtil {

    /**
     * 创建有返回结果的成果结果对象
     * @param object
     * @return
     */
    public static Result success(Object object){
        Result result = new Result();

        result.setCode(0);
        result.setMsg("成功");
        result.setData(object);

        return result;
    }

    /**
     * 创建无返回结果的成功结果对象
     * @return
     */
    public static Result success(){
        return success(null);
    }

    /**
     * 创建失败结果对象
     * @param code
     * @param msg
     * @return
     */
    public static Result error(Integer code, String msg){
        Result result = new Result();

        result.setCode(code);
        result.setMsg(msg);

        return result;
    }
}
