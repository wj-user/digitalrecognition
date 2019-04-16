package org.dl4j.jdbc;
import java.io.File;
import org.dl4j.controller.DigitalRecognitionController;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class MainTest {

	public static void main(String[] args) {
		ApplicationContext context= new ClassPathXmlApplicationContext("org/dl4j/jdbc/Beans.xml");
		ImageDaoImp imageDaoImp = (ImageDaoImp) context.getBean("ImageDaoImp");
		Image image=new Image("test ImagePath");
		List<Image> images=null;
		
		//System.out.println("开始插入");
		//imageDaoImp.addImage(image);
		
		
		/*System.out.println("开始查询");
		images=imageDaoImp.getLast10();
		for(Image image2:images) {
			image2.display();
		}*/
		List<org.dl4j.jdbc.Image> results = imageDaoImp.getLast10();
    	HashMap<String, String> map = new HashMap<String, String>();
    	for(int i=0; i<10;i++)
    	{
    		String temp = "Image" + String.valueOf(i);
    		//map.put(temp, results.get(i).getImagePath());
    		map.put(temp, DigitalRecognitionController.encodeImgageToBase64(new File(results.get(i).getImagePath())));
    	}
    	System.out.println(111);
		
	}

}
