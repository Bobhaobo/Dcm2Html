package com.bhb.dcm2html.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bhb.dcm2html.tools.Dcm2jpgIOStreamOutput;

@Controller
@RequestMapping("/get")
public class GetImageFromDicomController {
	///{width}/{height}
	@RequestMapping(value="/getImage")
	//, @PathVariable("width") String width, @PathVariable("height") String height
	public void getImage(HttpServletResponse httpServletResponse) throws IOException {
		//float windowWidth = Float.parseFloat(width);
		//float windowCenter = Float.parseFloat(height);
		try{
			File src = new File("D:\\000000.dcm");
			Dcm2jpgIOStreamOutput dcm2jpg = new Dcm2jpgIOStreamOutput();
			dcm2jpg.convert(src,httpServletResponse);//,windowWidth,windowCenter
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
