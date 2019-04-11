package org.dl4j.train;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
//import org.deeplearning4j.nn.conf.LearningRatePolicy;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;

import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class MnistClassifier {

	  private static final Logger log = LoggerFactory.getLogger(MnistClassifier.class);
	  private static final String basePath = "/Users/weijian/Downloads/sem2/cloud computing/project";

	  public static void main(String[] args) throws Exception {
	    int height = 28;
	    int width = 28;
	    int channels = 1; // 这里有没有复杂的识别，没有分成红绿蓝三个通道
	    int outputNum = 10; // 有十个数字，所以输出为10
	    int batchSize = 54;//每次迭代取54张小批量来训练，可以查阅神经网络的mini batch相关优化，也就是小批量求平均梯度
	    int nEpochs = 1;//整个样本集只训练一次
	    int iterations = 1;

	    int seed = 1234;
	    Random randNumGen = new Random(seed);

	    File trainData = new File(basePath + "/mnist_png/training");
	    FileSplit trainSplit = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
	    ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator(); //以父级目录名作为分类的标签名
	    ImageRecordReader trainRR = new ImageRecordReader(height, width, channels, labelMaker);//构造图片读取类
	    trainRR.initialize(trainSplit);
	    DataSetIterator trainIter = new RecordReaderDataSetIterator(trainRR, batchSize, 1, outputNum);

	    // 把像素值区间 0-255 压缩到0-1 区间
	    DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
	    scaler.fit(trainIter);
	    trainIter.setPreProcessor(scaler);
	    

	    // 向量化测试集
	    File testData = new File(basePath + "/mnist_png/testing");
	    FileSplit testSplit = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
	    ImageRecordReader testRR = new ImageRecordReader(height, width, channels, labelMaker);
	    testRR.initialize(testSplit);
	    DataSetIterator testIter = new RecordReaderDataSetIterator(testRR, batchSize, 1, outputNum);
	    testIter.setPreProcessor(scaler); // same normalization for better results

	    log.info("Network configuration and training...");
	    Map<Integer, Double> lrSchedule = new HashMap<Integer, Double>();//设定动态改变学习速率的策略，key表示小批量迭代到几次
	    lrSchedule.put(0, 0.06); 
	    lrSchedule.put(200, 0.05);
	    lrSchedule.put(600, 0.028);
	    lrSchedule.put(800, 0.0060);
	    lrSchedule.put(1000, 0.001);

	   /* MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
	        .seed(seed)
	        .iterations(iterations)
	        .regularization(true).l2(0.0005)
	        .learningRate(.01)
	        .learningRateDecayPolicy(LearningRatePolicy.Schedule)
	        .learningRateSchedule(lrSchedule) 
	        .weightInit(WeightInit.XAVIER)
	        .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
	        .updater(Updater.NESTEROVS)
	        .list()
	        .layer(0, new ConvolutionLayer.Builder(5, 5)
	            .nIn(channels)
	            .stride(1, 1)
	            .nOut(20)
	            .activation(Activation.IDENTITY)
	            .build())
	        .layer(1, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
	            .kernelSize(2, 2)
	            .stride(2, 2)
	            .build())
	        .layer(2, new ConvolutionLayer.Builder(5, 5)
	            .stride(1, 1) 
	            .nOut(50)
	            .activation(Activation.IDENTITY)
	            .build())
	        .layer(3, new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
	            .kernelSize(2, 2)
	            .stride(2, 2)
	            .build())
	        .layer(4, new DenseLayer.Builder().activation(Activation.RELU)
	            .nOut(500).build())
	        .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
	            .nOut(outputNum)
	            .activation(Activation.SOFTMAX)
	            .build())
	        .setInputType(InputType.convolutionalFlat(28, 28, 1)) 
	        .backprop(true).pretrain(false).build();

	    MultiLayerNetwork net = new MultiLayerNetwork(conf);
	    net.init();
	    net.setListeners(new ScoreIterationListener(10));
	    log.debug("Total num of params: {}", net.numParams());

	    // 评估测试集
	    for (int i = 0; i < nEpochs; i++) {
	      net.fit(trainIter);
	      Evaluation eval = net.evaluate(testIter);
	      log.info(eval.stats());
	      trainIter.reset();
	      testIter.reset();
	    }
	    ModelSerializer.writeModel(net, new File(basePath + "/minist-model.zip"), true);//保存训练好的网络
*/	  }
	}