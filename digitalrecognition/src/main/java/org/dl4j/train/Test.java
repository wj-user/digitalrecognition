package org.dl4j.train;

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
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;

public class Test implements InitializingBean {
	private static MultiLayerNetwork net;

	public static void main(String[] args) throws Exception {
		net = ModelSerializer.restoreMultiLayerNetwork(new File("/Users/weijian/Downloads/sem2/cloud computing/project/digitalrecognition/digitalrecognition/src/main/webapp/model/minist-model.zip"));
		//4
		String img="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";
		String imagePath= generateImage(img);
		imagePath= zoomImage(imagePath);
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		ImageRecordReader testRR = new ImageRecordReader(28, 28, 1);
		File testData = new File(imagePath);
		FileSplit testSplit = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS);
		testRR.initialize(testSplit);
		DataSetIterator testIter = new RecordReaderDataSetIterator(testRR, 1);
		testIter.setPreProcessor(scaler);
		//INDArray array = testIter.next().getFeatureMatrix();
		INDArray array=testIter.next().getFeatures();
		System.out.println(net.predict(array)[0]);
		//array=net.output(testIter);
		//System.out.println("result: "+net.predict(net.output(testIter)));
		//System.out.println("result: 111");
	}

	private static String generateImage(String img) {
		BASE64Decoder decoder = new BASE64Decoder();
		//String filePath = WebConstant.WEB_ROOT + "upload/"+UUID.randomUUID().toString()+".png";
		String filePath = "/Users/weijian/Downloads/sem2/try1.png";
		//String filePath = "/home/hduser/upload/digitalRecognition/try1.png";
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
	
	private static String zoomImage(String filePath){
		//String imagePath=WebConstant.WEB_ROOT + "upload/"+UUID.randomUUID().toString()+".png";
		String imagePath = "/Users/weijian/Downloads/sem2/try2.png";
		//String imagePath = "/home/hduser/upload/digitalRecognition/try2.png";
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
		BASE64Decoder decoder = new BASE64Decoder();
		//String filePath = WebConstant.WEB_ROOT + "upload/" + UUID.randomUUID().toString() + ".png";
		//String filePath = "/home/hduser/upload/digitalRecognition/upload.png";
		String filePath = "/Users/weijian/Downloads/sem2/upload.png";
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
		return 0;
	}
	public void afterPropertiesSet() throws Exception {
		
	}
}
