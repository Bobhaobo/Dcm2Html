package com.bhb.dcm2html.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletResponse;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.imageio.plugins.dcm.DicomImageReadParam;
import org.dcm4che3.util.SafeClose;

public class Dcm2jpgIOStreamOutput {
	private final ImageReader imageReader = ImageIO.getImageReadersByFormatName("DICOM").next();
    private float windowCenter;
    private float windowWidth;
    private boolean autoWindowing = true;
    private int windowIndex;
    private int voiLUTIndex;
    private boolean preferWindow = true;
    private Attributes prState;
    private int overlayActivationMask = 0xffff;
    private int overlayGrayscaleValue = 0xffff;
    private int frame = 1;  

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public void setWindowCenter(float windowCenter) {
		this.windowCenter = windowCenter;
	}

	public void setWindowWidth(float windowWidth) {
		this.windowWidth = windowWidth;
	}

	public void setAutoWindowing(boolean autoWindowing) {
		this.autoWindowing = autoWindowing;
	}

	public void setWindowIndex(int windowIndex) {
		this.windowIndex = windowIndex;
	}

	public void setVoiLUTIndex(int voiLUTIndex) {
		this.voiLUTIndex = voiLUTIndex;
	}

	public void setPreferWindow(boolean preferWindow) {
		this.preferWindow = preferWindow;
	}

	public void setPrState(Attributes prState) {
		this.prState = prState;
	}

	public void setOverlayActivationMask(int overlayActivationMask) {
		this.overlayActivationMask = overlayActivationMask;
	}

	public void setOverlayGrayscaleValue(int overlayGrayscaleValue) {
		this.overlayGrayscaleValue = overlayGrayscaleValue;
	}
	//, float windowWidth, float windowCenter
	public void convert(File src, HttpServletResponse response) throws Exception{
//		this.windowCenter = windowCenter;
//		this.windowWidth = windowWidth;
		Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");
        ImageReader reader = iter.next();      
        ImageInputStream iis = ImageIO.createImageInputStream(src);
        BufferedImage bi;
        OutputStream out = null;
        try{
        	reader.setInput(iis, false);
        	bi = readImage(iis);
        	if (bi == null) {
                System.out.println("\nError: " + src + " - couldn't read!");
                return;
            }
//        	System.out.println("width: " + bi.getWidth());
//        	System.out.println("height: " + bi.getHeight());
//        	System.out.println("type: " + bi.getType());
        	response.setContentType("image/*");
        	out = response.getOutputStream();
        	ImageIO.write(bi, "png", out);
            out.close();
        }finally{
			SafeClose.close(iis);
			SafeClose.close(out);
		}
	}
	
	private ImageReadParam readParam() throws Exception{
		DicomImageReadParam param = (DicomImageReadParam) imageReader.getDefaultReadParam();
	    param.setWindowCenter(windowCenter);
	    param.setWindowWidth(windowWidth);
	    param.setAutoWindowing(autoWindowing);
	    param.setWindowIndex(windowIndex);
	    param.setVOILUTIndex(voiLUTIndex);
	    param.setPreferWindow(preferWindow);
	    param.setPresentationState(prState);
	    param.setOverlayActivationMask(overlayActivationMask);
	    param.setOverlayGrayscaleValue(overlayGrayscaleValue); 
		return param;
	}
	
	private BufferedImage readImage(ImageInputStream iis) throws Exception{
		imageReader.setInput(iis);
		return imageReader.read(frame -1, readParam());
	}
}
