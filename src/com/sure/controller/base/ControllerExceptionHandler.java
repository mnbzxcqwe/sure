package com.sure.controller.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sure.exception.MyException;
import com.sure.utils.Const;

public class ControllerExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3) {
		arg1.setCharacterEncoding("utf-8");
		
		JSONObject json = new JSONObject();
		
		if(arg3 instanceof MyException){
			json.put(Const.AJAX_ERROR_MSG, arg3.getMessage());
			json.put(Const.AJAX_ERROR_EXCEPTION, arg3.getMessage());
		}else{
			json.put(Const.AJAX_ERROR_MSG, "发生错误");
			json.put(Const.AJAX_ERROR_EXCEPTION, arg3.getMessage());
		}
		
		try {
			arg1.getWriter().write(json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
