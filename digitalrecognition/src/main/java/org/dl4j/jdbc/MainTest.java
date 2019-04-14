package org.dl4j.jdbc;
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
		
		
		System.out.println("开始查询");
		images=imageDaoImp.getLast10();
		for(Image image2:images) {
			image2.display();
		}
		
	}

}
