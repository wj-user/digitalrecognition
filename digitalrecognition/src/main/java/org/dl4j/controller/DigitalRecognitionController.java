package org.dl4j.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.dl4j.constant.WebConstant;
import org.dl4j.jdbc.ImageDaoImp;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;

@RequestMapping("/digitalRecognition")
@Controller
public class DigitalRecognitionController implements InitializingBean {
	private MultiLayerNetwork net;
	private MultiLayerNetwork netFood;
	ApplicationContext context= new ClassPathXmlApplicationContext("org/dl4j/jdbc/Beans.xml");
	ImageDaoImp imageDaoImp = (ImageDaoImp) context.getBean("ImageDaoImp");
	@ResponseBody
	@RequestMapping("/predict")
	public int predict(@RequestParam(value = "img") String img) throws Exception {
		String imagePath= generateImage(img);
		org.dl4j.jdbc.Image image=new org.dl4j.jdbc.Image(imagePath);
		imageDaoImp.addImage(image);
		imagePath= zoomImage(imagePath);
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		ImageRecordReader testRR = new ImageRecordReader(28, 28, 1);
		File testData = new File(imagePath);
		FileSplit testSplit = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS);
		testRR.initialize(testSplit);
		DataSetIterator testIter = new RecordReaderDataSetIterator(testRR, 1);
		testIter.setPreProcessor(scaler);
		//INDArray array = testIter.next().getFeatureMatrix();
		INDArray array = testIter.next().getFeatures();
		return net.predict(array)[0];
	}

	private String generateImage(String img) {
		BASE64Decoder decoder = new BASE64Decoder();
		String filePath ="/home/hduser/upload/"+UUID.randomUUID().toString()+".png";
		//String filePath = "/Users/weijian/Downloads/sem2/try1.png";
		//String filePath = "/home/hduser/upload/digitalRecognition/try1.png";
		//String filePath = WebConstant.WEB_ROOT + "/upload/"+UUID.randomUUID().toString()+".png";
		try {
			byte[] b = decoder.decodeBuffer(img);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(filePath);
			out.write(b);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filePath;
	}
	
	private String zoomImage(String filePath){
		String imagePath="/home/hduser/upload/"+UUID.randomUUID().toString()+".png";
		//String imagePath = "/Users/weijian/Downloads/sem2/try2.png";
		//String imagePath = "/home/hduser/upload/digitalRecognition/try2.png";
		//String imagePath = WebConstant.WEB_ROOT + "/upload/"+UUID.randomUUID().toString()+".png";
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(filePath));
			Image image = bufferedImage.getScaledInstance(28, 28, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
			ImageIO.write(tag, "png",new File(imagePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imagePath;
	}
	
	@ResponseBody
	@RequestMapping("/sendImage")
	public int sendImage(@RequestParam(value = "img") String img) throws Exception {
		String imagePath= generateImage(img);
		org.dl4j.jdbc.Image image=new org.dl4j.jdbc.Image(imagePath);
		imageDaoImp.addImage(image);
		imagePath= zoomImage(imagePath);
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		ImageRecordReader testRR = new ImageRecordReader(28, 28, 1);
		File testData = new File(imagePath);
		FileSplit testSplit = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS);
		testRR.initialize(testSplit);
		DataSetIterator testIter = new RecordReaderDataSetIterator(testRR, 1);
		testIter.setPreProcessor(scaler);
		//INDArray array = testIter.next().getFeatureMatrix();
		INDArray array = testIter.next().getFeatures();
		return netFood.predict(array)[0];
	}
	
	public void afterPropertiesSet() throws Exception {
//		net = ModelSerializer.restoreMultiLayerNetwork(new File(WebConstant.WEB_ROOT + "model/minist-model2.zip"));
//		netFood = ModelSerializer.restoreMultiLayerNetwork(new File(WebConstant.WEB_ROOT + "model/minist-model4.zip"));
		net = ModelSerializer.restoreMultiLayerNetwork(new File("/home/hduser/github/digitalrecognition/digitalrecognition/src/main/webapp/model/minist-model2.zip"));
		netFood = ModelSerializer.restoreMultiLayerNetwork(new File("/home/hduser/github/digitalrecognition/digitalrecognition/src/main/webapp/model/minist-model4.zip"));
	}

}
