package internetFinal;


import internetFinal.SimpleChatClient.SendButtonListener;
import internetFinal.videoClient.binaryReadIn.UpdateWorker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;
import com.xuggle.xuggler.io.XugglerIO;

public class videoClient {
	JButton terminateButton;
	PrintWriter writer;
	BufferedReader reader;
	String[] fileList = null;
	static boolean downloadOrNot = false;
	
	static boolean isLogIn = false;
	
	private VideoImage mScreen = null;
	
	int currentStatus = 1;
	int countBytes = 0;
//	int num2 = 0;
//	int num = 0;

	static int port = 5000;
	static String remoteHost = "127.0.0.1";
	Socket skt;
	
	public static void main(String[] args) {
		videoClient client = new videoClient();
		client.go();
	}
	public void go(){
		String message;
		setUpNetworking();
		
		Thread writerThread = new Thread(new outgoingWriter());
		writerThread.start();
		
		//reader part
		try{
			while((message = reader.readLine()) != null){
				System.out.println("read " + message);
				
				//對server給的資料做判斷
				if(message.startsWith("getFilePortNumForAd")){
					//切割portNum出來
					System.out.println("in getFilePortNumForAd now~");
					String[] stringArray = message.split(" ");
					String getPortNum = stringArray[1].toString();	//port號
					int getPortNumInt = Integer.parseInt(getPortNum);
					
					//file size
					String fileSizeAns = stringArray[2].toString();	//file size
					int fileSizeInt = Integer.parseInt(fileSizeAns);
					System.out.println("(ad)fileSizeInt: "+ fileSizeInt);
					
					//file name
					String fileName = stringArray[3].toString();	//file name
					System.out.println("(ad)fileName: "+ fileName);
					
					//get file data here
					Thread adBinaryFileThread = new Thread(new adBinaryReadIn(getPortNumInt,fileSizeInt,fileName));
					adBinaryFileThread.start();
				}
				else if(message.startsWith("getFilePortNum")){
					System.out.println("in getFilePortNum now~");
					//切割portNum出來
					String[] stringArray = message.split(" ");
					String getPortNum = stringArray[1].toString();	//port號
					int getPortNumInt = Integer.parseInt(getPortNum);
					
					//file size
					String fileSizeAns = stringArray[2].toString();	//file size
					int fileSizeInt = Integer.parseInt(fileSizeAns);
					System.out.println("(b)fileSizeInt: "+ fileSizeInt);
					
					//get file data here
					Thread binaryFileThread = new Thread(new binaryReadIn(getPortNumInt,fileSizeInt));
					binaryFileThread.start();
				}
				
				else if(message.startsWith("loginSuccess")){
					System.out.println("登入成功");
					isLogIn = true;
				}
				else if(message.startsWith("loginFailed")){
					System.out.println("登入失敗");
				}
				else if(message.startsWith("registerSuccess")){
					System.out.println("註冊成功");
				}
				else if(message.startsWith("registerFailed")){
					System.out.println("註冊失敗");
				}
				else if(message.startsWith("fileListData")){
					System.out.println(message);
					String[] stringArray = message.split(" ");
					
					fileList = new String[stringArray.length];
					
					for(int i= 0; i < stringArray.length; i++){
//						System.out.println(i);
//						System.out.println(stringArray[i]);
						fileList[i] = stringArray[i];
					}
					for(int i= 0; i < stringArray.length; i++){
						System.out.println(i);
						System.out.println(fileList[i]);
//						fileList[i] = stringArray[i];
					}			
				}
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		

	}
	private void setUpNetworking(){
		try { 
	        System.out.println("簡易檔案傳送...");
	
//	        String remoteHost = "127.0.0.1";
//	        int port = 5000;
	        
	        System.out.printf("遠端主機: %s%n", remoteHost); 
            System.out.printf("遠端主機連接埠: %d%n", port);
//            System.out.printf("傳送檔案: %s%n", file.getName());

            skt = new Socket(remoteHost, port); 

            System.out.println("連線成功！嘗試接收檔案....");
            
            InputStreamReader streamReader = new InputStreamReader(skt.getInputStream());
			reader = new BufferedReader(streamReader);
	        
            writer = new PrintWriter(skt.getOutputStream());
	        
		}
		catch(Exception e) { 
	        e.printStackTrace(); 
	    } 
	}
	public String getFlvOfFile(String fileName){
		int fileIndex = 0;
		for(int i = 0; i< fileList.length; i++){
			if(fileList[i].equals(fileName)){
				fileIndex = i;
			}
		}
		return Integer.toString(fileIndex)+".flv";
	}
    
    public class outgoingWriter implements Runnable{
		public void run() {
			//tell what I needed first!
//			writer.println("fileData IMG_2964.flv ");
//			writer.flush();
//			
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			
			writer.println("adFileData fileName.flv ");
  			writer.flush();
			
//			writer.println("fileList");
//  			writer.flush();
  			
//			while(true){
//				writer.println("register ");
//				writer.flush();
//			}
			
//			writer.println("fileData youtube_h264_mp3.flv ");
//			writer.flush();
					
		}
		
	}
    public class adBinaryReadIn implements Runnable{
		int portForConnect;
		int thisFileSize;
		int num;
		String thisFileName;
		
		Socket skt_b;
		IContainer container;
		
		/**
		   * The audio line we'll output sound to; it'll be the default audio device on your system if available
		   */
		  private SourceDataLine mLine;

		  /**
		   * The window we'll draw the video on.
		   * 
		   */
//		  private VideoImage mScreen = null;

		  private long mSystemVideoClockStartTime;

		  private long mFirstVideoTimestampInStream;
		  
		
    	public adBinaryReadIn(int portNum, int fileSizeAns, String fileName){
    		portForConnect = portNum;
    		thisFileSize = fileSizeAns;
//    		System.out.println("thisFileSize: "+thisFileSize);
    		thisFileName = fileName;
//    		mScreen = null;
//    		num = 0;
		}
    	public void run() {
			//connect with server with portNum and remoteHost
    		System.out.printf("遠端主機: %s%n", remoteHost); 
            System.out.printf("遠端主機連接埠: %d%n", portForConnect);
            try {
				skt_b = new Socket(remoteHost, portForConnect);
				System.out.println("連線成功！嘗試接收檔案....");
				
//				writer = new PrintWriter(skt.getOutputStream());	//暫時，拿來跟server說我要哪個
				
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
          
            
            //reader part
//    		IContainer container;
    		try{
    			// 取得檔案名稱
//              String filesize = new BufferedReader(
//                                  new InputStreamReader(
//                                    skt.getInputStream())).readLine();
//              System.out.printf("檔案大小： %s ...\n", filesize); 
//    			int countByte = skt.getInputStream().read();
    			
              
              //返回接收到filesize了
//              writer.println("stop");
//              writer.flush();
    			
//    		  //tell what I needed first!
//    		  writer.println("fileData IMG_2964.flv");
//    		  writer.flush();
              
    		  String filename = "flv";
              System.out.printf("接收檔案 %s ...\n", filename);

//                if (args.length <= 0)
//                    throw new IllegalArgumentException("must pass in a filename as the first argument");
//                  
//                  String filename = args[0];
                  
                  // Let's make sure that we can actually convert video pixel formats.
                  if (!IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))
                    throw new RuntimeException("you must install the GPL version of Xuggler (with IVideoResampler support) for this demo to work");
                  
//                  Thread.sleep(3000);
                  // Create a Xuggler container object
                  container = IContainer.make();
                  
                  // Open up the container
                  int a;
//                  IContainerFormat format = IContainerFormat.make();
//                  format.setOutputFormat("mp4", "http://118.166.84.41/final/music/test.mp4", "video/mp4");
                  

                  if ((a = container.open(skt_b.getInputStream(), null)) < 0){
//                  if ((a =container.open(XugglerIO.map(aa,skt.getInputStream()), 
//                		     IContainer.Type.READ, null)) < 0){
//                	  System.out.println(a);
//                	  System.out.println(container.isOpened());
//                	  System.out.println(skt_b);
//                	  System.out.println(skt_b.getInputStream());
                	  throw new IllegalArgumentException("could not open InputStream");
                  }
                  
                  System.out.println("可以打開InputStream了！");
                  // query how many streams the call to open found
                  int numStreams = container.getNumStreams();
//                  System.out.println("numStreams: "+numStreams);
                  long fileSize = container.getFileSize();
//                  System.out.println("fileSize: "+fileSize);
                  
                  // and iterate through the streams to find the first audio stream
                  int videoStreamId = -1;
                  IStreamCoder videoCoder = null;
                  int audioStreamId = -1;
                  IStreamCoder audioCoder = null;
                  for(int i = 0; i < numStreams; i++)
                  {
                    // Find the stream object
                    IStream stream = container.getStream(i);
                    // Get the pre-configured decoder that can decode this stream;
                    IStreamCoder coder = stream.getStreamCoder();
                    
                    if (videoStreamId == -1 && coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
                    {
                      videoStreamId = i;
                      videoCoder = coder;
                    }
                    else if (audioStreamId == -1 && coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
                    {
                      audioStreamId = i;
                      audioCoder = coder;
                    }
                  }
                  if (videoStreamId == -1 && audioStreamId == -1)
                    throw new RuntimeException("could not find audio or video stream in container: "+filename);
                  
                  /*
                   * Check if we have a video stream in this file.  If so let's open up our decoder so it can
                   * do work.
                   */
                  IVideoResampler resampler = null;
                  if (videoCoder != null)
                  {
                    if(videoCoder.open() < 0)
                      throw new RuntimeException("could not open audio decoder for container: "+filename);
                  
                    if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24)
                    {
                      // if this stream is not in BGR24, we're going to need to
                      // convert it.  The VideoResampler does that for us.
                      resampler = IVideoResampler.make(600, 500, IPixelFormat.Type.BGR24,
                          videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
                      if (resampler == null)
                        throw new RuntimeException("could not create color space resampler for: " + filename);
                    }
                    /*
                     * And once we have that, we draw a window on screen
                     */
                    openJavaVideo();
                  }
                  
                  if (audioCoder != null)
                  {
                    if (audioCoder.open() < 0)
                      throw new RuntimeException("could not open audio decoder for container: "+filename);
                    
                    /*
                     * And once we have that, we ask the Java Sound System to get itself ready.
                     */
                    try
                    {
                      openJavaSound(audioCoder);
                    }
                    catch (LineUnavailableException ex)
                    {
                      throw new RuntimeException("unable to open sound device on your system when playing back container: "+filename);
                    }
                  }
                  
                  
                  /*
                   * Now, we start walking through the container looking at each packet.
                   */
                  IPacket packet = IPacket.make();
                  mFirstVideoTimestampInStream = Global.NO_PTS;
                  mSystemVideoClockStartTime = 0;
                  
                  
//                  System.out.println(mScreen.getLayout());
//                  mScreen.setLayout(null);
                  
                  
                  JPanel jp = new JPanel();
                  JPanel tjp = new JPanel();
                  JPanel tjp2 = new JPanel();
                  tjp.setVisible(false);
                  tjp2.setVisible(false);
//                  jp.setSize(100,600);
                  
                  jp.setLayout(new GridLayout(6,0));
                  jp.add(tjp);
                  jp.add(tjp2);
                  
                  
//                  System.out.println(jp.getLayout());
                  
                  JButton sendButton = new JButton(new ImageIcon("1435509837_play.png"));
                  sendButton.setOpaque(false);
                  sendButton.setContentAreaFilled(false);
                  sendButton.setBorderPainted(false);
                  
                  JButton pauseButton = new JButton(new ImageIcon("1435509845_pause.png"));
                  pauseButton.setOpaque(false);
                  pauseButton.setContentAreaFilled(false);
                  pauseButton.setBorderPainted(false);
                  pauseButton.addActionListener(new StopButtonListener());
                  
                  sendButton.addActionListener(new SendButtonListener());
//                  sendButton.setSize(100, 100);
                  jp.add(sendButton);
                  jp.add(pauseButton);
                  
                  terminateButton = new JButton(new ImageIcon("1435509855_stop.png"));
                  terminateButton.setOpaque(false);
                  terminateButton.setContentAreaFilled(false);
                  terminateButton.setBorderPainted(false);
                  
                  JProgressBar progressBar = new JProgressBar();  
                  progressBar.setValue(0);  
                  progressBar.setStringPainted(true);
                  progressBar.setSize(100, 20);
//                  progressBar.setBackground(Color.re);
                  UIManager.put("ProgressBar.background", Color.ORANGE);
                  UIManager.put("ProgressBar.foreground", Color.RED);
                  UIManager.put("ProgressBar.selectionBackground", Color.BLUE);
                  UIManager.put("ProgressBar.selectionForeground", Color.gray);
                  
                  mScreen.add(progressBar, BorderLayout.SOUTH);
                  UpdateWorker updateWorker = new UpdateWorker(progressBar);  
                  updateWorker.execute();
                  
                  terminateButton.addActionListener(new terminateButtonListener());
//                  terminateButton.setSize(100, 100);
                  jp.add(terminateButton);
                  mScreen.setDefaultCloseOperation(mScreen.EXIT_ON_CLOSE);
                  jp.setBackground(Color.gray);
                  mScreen.add(jp,BorderLayout.EAST);
                  
                  while(container.readNextPacket(packet) >= 0)
                  {
                	  while(currentStatus == 0){
                		  System.out.print("InWhile");
                	  }
//                	  System.out.println();
                    /*
                     * Now we have a packet, let's see if it belongs to our video stream
                     */
                    if (packet.getStreamIndex() == videoStreamId)
                    {
                      /*
                       * We allocate a new picture to get the data out of Xuggler
                       */
                      IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
                          videoCoder.getWidth(), videoCoder.getHeight());
                      
                      /*
                       * Now, we decode the video, checking for any errors.
                       * 
                       */
                      int bytesDecoded = videoCoder.decodeVideo(picture, packet, 0);
                      num = num + bytesDecoded;
//                      System.out.println("sum of bytesDecoded: "+num);
                      if (bytesDecoded < 0)
                        throw new RuntimeException("got error decoding audio in: " + filename);

                      /*
                       * Some decoders will consume data in a packet, but will not be able to construct
                       * a full video picture yet.  Therefore you should always check if you
                       * got a complete picture from the decoder
                       */
                      if (picture.isComplete())
                      {
                        IVideoPicture newPic = picture;
//                        num = (int)videoCoder.getExtraDataSize(); //picture.getSize();
//                        System.out.println("num: "+num);
                        /*
                         * If the resampler is not null, that means we didn't get the video in BGR24 format and
                         * need to convert it into BGR24 format.
                         */
                        if (resampler != null)
                        {
                          // we must resample
                          newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), 600, 500);
                          if (resampler.resample(newPic, picture) < 0)
                            throw new RuntimeException("could not resample video from: " + filename);
                        }
                        if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
                          throw new RuntimeException("could not decode video as BGR 24 bit data in: " + filename);

                        long delay = millisecondsUntilTimeToDisplay(newPic);
                        // if there is no audio stream; go ahead and hold up the main thread.  We'll end
                        // up caching fewer video pictures in memory that way.
                        try
                        {
                          if (delay > 0)
                            Thread.sleep(delay);
                        }
                        catch (InterruptedException e)
                        {
                          return;
                        }

                        // And finally, convert the picture to an image and display it
//                        newPic.
                        
                        mScreen.setImage(Utils.videoPictureToImage(newPic));
                        
                        mScreen.setSize(650, 520);
//                        JButton sendButton = new JButton("暫停");
//                        sendButton.addActionListener(new SendButtonListener());
//                        sendButton.setSize(100, 100);
////                        sendButton.setLocation(500, 500);
//                        mScreen.add(sendButton, BorderLayout.WEST);
//                        
//                        
//                        terminateButton = new JButton("結束");
//                        terminateButton.addActionListener(new terminateButtonListener());
//                        terminateButton.setSize(100, 100);
////                        terminateButton.setLocation(300, 500);
//                        mScreen.add(terminateButton, BorderLayout.EAST);
//                        mScreen.setDefaultCloseOperation(mScreen.EXIT_ON_CLOSE);
                        
                        
                      }
                    }
                    else if (packet.getStreamIndex() == audioStreamId)
                    {	
                      /*
                       * We allocate a set of samples with the same number of channels as the
                       * coder tells us is in this buffer.
                       * 
                       * We also pass in a buffer size (1024 in our example), although Xuggler
                       * will probably allocate more space than just the 1024 (it's not important why).
                       */
                      IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());
                      
                      /*
                       * A packet can actually contain multiple sets of samples (or frames of samples
                       * in audio-decoding speak).  So, we may need to call decode audio multiple
                       * times at different offsets in the packet's data.  We capture that here.
                       */
                      int offset = 0;
                      
                      /*
                       * Keep going until we've processed all data
                       */
                      while(offset < packet.getSize())
                      {
                        int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
                        num = num + bytesDecoded;
                        if (bytesDecoded < 0)
                          throw new RuntimeException("got error decoding audio in: " + filename);
                        offset += bytesDecoded;
                        /*
                         * Some decoder will consume data in a packet, but will not be able to construct
                         * a full set of samples yet.  Therefore you should always check if you
                         * got a complete set of samples from the decoder
                         */
                        if (samples.isComplete())
                        {
                          // note: this call will block if Java's sound buffers fill up, and we're
                          // okay with that.  That's why we have the video "sleeping" occur
                          // on another thread.
                          playJavaSound(samples);
                        }
                      }
                    }
                    else
                    {
                      /*
                       * This packet isn't part of our video stream, so we just silently drop it.
                       */
                      do {} while(false);
                    }
                    
                  }
                  /*
                   * Technically since we're exiting anyway, these will be cleaned up by 
                   * the garbage collector... but because we're nice people and want
                   * to be invited places for Christmas, we're going to show how to clean up.
                   */
                  if (videoCoder != null)
                  {
                    videoCoder.close();
                    videoCoder = null;
                  }
                  if (audioCoder != null)
                  {
                    audioCoder.close();
                    audioCoder = null;
                  }
                  if (container !=null)
                  {
                    container.close();
                    container = null;
                  }
                  closeJavaSound();
                  closeJavaVideo();
                
                  
                //temp~~~~~~~
                writer.println("fileData "+thisFileName+" ");
      		  	writer.flush();
//              
      		  	mScreen.remove(progressBar);
      		  	mScreen.repaint();
      		  	System.out.println("\n檔案接收完畢！(adBinaryReadIn)");
              
      			Thread.sleep(1000);
                
                
                skt_b.close();
    	        
    		}catch(Exception ex){
    			ex.printStackTrace();
    		}
            
		}
    	private long millisecondsUntilTimeToDisplay(IVideoPicture picture)
        {
          /**
           * We could just display the images as quickly as we decode them, but it turns
           * out we can decode a lot faster than you think.
           * 
           * So instead, the following code does a poor-man's version of trying to
           * match up the frame-rate requested for each IVideoPicture with the system
           * clock time on your computer.
           * 
           * Remember that all Xuggler IAudioSamples and IVideoPicture objects always
           * give timestamps in Microseconds, relative to the first decoded item.  If
           * instead you used the packet timestamps, they can be in different units depending
           * on your IContainer, and IStream and things can get hairy quickly.
           */
          long millisecondsToSleep = 0;
          if (mFirstVideoTimestampInStream == Global.NO_PTS)
          {
            // This is our first time through
            mFirstVideoTimestampInStream = picture.getTimeStamp();
            // get the starting clock time so we can hold up frames
            // until the right time.
            mSystemVideoClockStartTime = System.currentTimeMillis();
            millisecondsToSleep = 0;
          } else {
            long systemClockCurrentTime = System.currentTimeMillis();
            long millisecondsClockTimeSinceStartofVideo = systemClockCurrentTime - mSystemVideoClockStartTime;
            // compute how long for this frame since the first frame in the stream.
            // remember that IVideoPicture and IAudioSamples timestamps are always in MICROSECONDS,
            // so we divide by 1000 to get milliseconds.
            long millisecondsStreamTimeSinceStartOfVideo = (picture.getTimeStamp() - mFirstVideoTimestampInStream)/1000;
            final long millisecondsTolerance = 1000; // and we give ourselfs 50 ms of tolerance
            millisecondsToSleep = (millisecondsStreamTimeSinceStartOfVideo -
                (millisecondsClockTimeSinceStartofVideo+millisecondsTolerance));
          }
          return millisecondsToSleep;
        }

        /**
         * Opens a Swing window on screen.
         */
        private void openJavaVideo()
        {
        	if(mScreen == null){
        		mScreen = new VideoImage();
        		mScreen.setSize(650, 520);
        	}
          
        }

        /**
         * Forces the swing thread to terminate; I'm sure there is a right
         * way to do this in swing, but this works too.
         */
        private void closeJavaVideo()
        {
//          System.exit(0);
        }

        private void openJavaSound(IStreamCoder aAudioCoder) throws LineUnavailableException
        {
          AudioFormat audioFormat = new AudioFormat(aAudioCoder.getSampleRate(),
              (int)IAudioSamples.findSampleBitDepth(aAudioCoder.getSampleFormat()),
              aAudioCoder.getChannels(),
              true, /* xuggler defaults to signed 16 bit samples */
              false);
          DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
          mLine = (SourceDataLine) AudioSystem.getLine(info);
          /**
           * if that succeeded, try opening the line.
           */
          mLine.open(audioFormat);
          /**
           * And if that succeed, start the line.
           */
          mLine.start();
          
          
        }

        private void playJavaSound(IAudioSamples aSamples)
        {
          /**
           * We're just going to dump all the samples into the line.
           */
          byte[] rawBytes = aSamples.getData().getByteArray(0, aSamples.getSize());
          mLine.write(rawBytes, 0, aSamples.getSize());
        }

        private void closeJavaSound()
        {
          if (mLine != null)
          {
            /*
             * Wait for the line to finish playing
             */
            mLine.drain();
            /*
             * Close the line.
             */
            mLine.close();
            mLine=null;
          }
        }
        public class UpdateWorker extends SwingWorker   
        {  
            JProgressBar bar = null;  
//            JFrame f=null;  
            public UpdateWorker(JProgressBar bar)  
            {  
                this.bar = bar;  
//                this.f = f;  
            }         
              
            protected String doInBackground() throws Exception {  
//                Random rdm = new Random();  
                int pv = 0;
                while(pv<100)  
                {  
                	Thread.sleep(1000);
//                    if((num/thisFileSize)*100 )
                	pv = (int)((num*100)/thisFileSize);
//                	System.out.println("pv: "+pv);
                	bar.setValue(pv);
                }  
//                bar.setValue(100);
//                pv = 0;
//                bar.setVisible(false);
                return null;  
            }   
              
            protected void done()   
            {   
//            	bar.setValue(0);
//            	mScreen.repaint();
//            	System.out.println("in done!");
//                f.setVisible(false);      
//                f.dispose();  
            }  
        }
        public class terminateButtonListener implements ActionListener{
    		public void actionPerformed(ActionEvent e) {
    			System.out.println("in terminate button~");
    			try {
    				skt_b.close();
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    			mScreen.dispose();
    		}	
    	}
        	
	}
    public class binaryReadIn implements Runnable{
		int portForConnect;
		int thisFileSize;
		Socket skt_b;
		IContainer container;
		int num2=0;
		
		
		/**
		   * The audio line we'll output sound to; it'll be the default audio device on your system if available
		   */
		  private SourceDataLine mLine;

		  /**
		   * The window we'll draw the video on.
		   * 
		   */
//		  private VideoImage mScreen = null;

		  private long mSystemVideoClockStartTime;

		  private long mFirstVideoTimestampInStream;
		  
		
    	public binaryReadIn(int portNum,int fileSizeAns){
    		portForConnect = portNum;
    		thisFileSize = fileSizeAns;
    		System.out.println("(b)thisFileSize: "+thisFileSize);
//    		num2 = 0;
		}
    	public void run() {
			//connect with server with portNum and remoteHost
    		System.out.printf("遠端主機: %s%n", remoteHost); 
            System.out.printf("遠端主機連接埠: %d%n", portForConnect);
            try {
				skt_b = new Socket(remoteHost, portForConnect);
				System.out.println("連線成功！嘗試接收檔案....");
				
//				writer = new PrintWriter(skt.getOutputStream());	//暫時，拿來跟server說我要哪個
				
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
          
            
            //reader part
//    		IContainer container;
    		try{
    			// 取得檔案名稱
//              String filesize = new BufferedReader(
//                                  new InputStreamReader(
//                                    skt.getInputStream())).readLine();
//              System.out.printf("檔案大小： %s ...\n", filesize); 
//    			int countByte = skt.getInputStream().read();
    			
              
              //返回接收到filesize了
//              writer.println("stop");
//              writer.flush();
    			
//    		  //tell what I needed first!
//    		  writer.println("fileData IMG_2964.flv");
//    		  writer.flush();
              
    		  String filename = "flv";
              System.out.printf("接收檔案 %s ...\n", filename);

//                if (args.length <= 0)
//                    throw new IllegalArgumentException("must pass in a filename as the first argument");
//                  
//                  String filename = args[0];
                  
                  // Let's make sure that we can actually convert video pixel formats.
                  if (!IVideoResampler.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION))
                    throw new RuntimeException("you must install the GPL version of Xuggler (with IVideoResampler support) for this demo to work");
                  
//                  Thread.sleep(3000);
                  // Create a Xuggler container object
                  container = IContainer.make();
                  IMediaWriter mWriter = null;
                  if(downloadOrNot == true){
                	  mWriter = ToolFactory.makeWriter("fileName.flv");
                  }
                  
                  // Open up the container
                  int a;
//                  IContainerFormat format = IContainerFormat.make();
//                  format.setOutputFormat("mp4", "http://118.166.84.41/final/music/test.mp4", "video/mp4");
                  

                  if ((a = container.open(skt_b.getInputStream(), null)) < 0){
//                  if ((a =container.open(XugglerIO.map(aa,skt.getInputStream()), 
//                		     IContainer.Type.READ, null)) < 0){
                	  System.out.println(a);
                	  System.out.println(container.isOpened());
                	  System.out.println(skt_b);
                	  System.out.println(skt_b.getInputStream());
                	  throw new IllegalArgumentException("could not open InputStream");
                  }
                  
                  System.out.println("可以打開InputStream了！");
                  // query how many streams the call to open found
                  int numStreams = container.getNumStreams();
                  System.out.println("numStreams: "+numStreams);
                  long fileSize = container.getFileSize();
                  System.out.println("fileSize: "+fileSize);
                  
                  // and iterate through the streams to find the first audio stream
                  int videoStreamId = -1;
                  IStreamCoder videoCoder = null;
                  int audioStreamId = -1;
                  IStreamCoder audioCoder = null;
                  for(int i = 0; i < numStreams; i++)
                  {
                    // Find the stream object
                    IStream stream = container.getStream(i);
                    // Get the pre-configured decoder that can decode this stream;
                    IStreamCoder coder = stream.getStreamCoder();
                    
                    if (videoStreamId == -1 && coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO)
                    {
                      videoStreamId = i;
                      videoCoder = coder;
                    }
                    else if (audioStreamId == -1 && coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
                    {
                      audioStreamId = i;
                      audioCoder = coder;
                    }
                  }
                  if (videoStreamId == -1 && audioStreamId == -1)
                    throw new RuntimeException("could not find audio or video stream in container: "+filename);
                  
                  /*
                   * Check if we have a video stream in this file.  If so let's open up our decoder so it can
                   * do work.
                   */
                  IVideoResampler resampler = null;
                  if (videoCoder != null)
                  {
                    if(videoCoder.open() < 0)
                      throw new RuntimeException("could not open audio decoder for container: "+filename);
                  
                    if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24)
                    {
                      // if this stream is not in BGR24, we're going to need to
                      // convert it.  The VideoResampler does that for us.
                      resampler = IVideoResampler.make(600, 500, IPixelFormat.Type.BGR24,
                          videoCoder.getWidth(), videoCoder.getHeight(), videoCoder.getPixelType());
                      if (resampler == null)
                        throw new RuntimeException("could not create color space resampler for: " + filename);
                    }
                    /*
                     * And once we have that, we draw a window on screen
                     */
                    openJavaVideo();
                  }
                  
                  if (audioCoder != null)
                  {
                    if (audioCoder.open() < 0)
                      throw new RuntimeException("could not open audio decoder for container: "+filename);
                    
                    /*
                     * And once we have that, we ask the Java Sound System to get itself ready.
                     */
                    try
                    {
                      openJavaSound(audioCoder);
                    }
                    catch (LineUnavailableException ex)
                    {
                      throw new RuntimeException("unable to open sound device on your system when playing back container: "+filename);
                    }
                  }
                  
                  
                  /*
                   * Now, we start walking through the container looking at each packet.
                   */
                  IPacket packet = IPacket.make();
                  if(downloadOrNot == true){
	                  mWriter.addAudioStream(1, 0, audioCoder.getChannels(), audioCoder.getSampleRate());
	                  mWriter.addVideoStream(0, 0, videoCoder.getWidth(), videoCoder.getHeight());
                  }
                  mFirstVideoTimestampInStream = Global.NO_PTS;
                  mSystemVideoClockStartTime = 0;
                  
                  
                  System.out.println(mScreen.getLayout());
//                  mScreen.setLayout(null);
                  
                  
//                  JButton sendButton = new JButton("暫停");
//                  sendButton.addActionListener(new SendButtonListener());
////                  sendButton.setSize(100, 100);
////                  sendButton.setLocation(500, 500);
//                  mScreen.add(sendButton,BorderLayout.EAST);
//                  
//                  terminateButton = new JButton("結束");
//                  terminateButton.addActionListener(new terminateButtonListener());
////                  terminateButton.setSize(100, 100);
////                  terminateButton.setLocation(300, 500);
//                  mScreen.add(terminateButton,BorderLayout.WEST);
//                  mScreen.setDefaultCloseOperation(mScreen.EXIT_ON_CLOSE);
//                  GridLayout gl = new GridLayout(2,1);
                  
                  
                  JPanel jp = new JPanel();
                  JPanel tjp = new JPanel();
                  JPanel tjp2 = new JPanel();
//                  jp.setSize(100,600);
                  tjp.setVisible(false);
                  tjp2.setVisible(false);
                  
                  jp.setLayout(new GridLayout(6,0));
                  jp.add(tjp);
                  jp.add(tjp2);
                  
                  
                  
                  System.out.println(jp.getLayout());
                  
                  JButton sendButton = new JButton(new ImageIcon("1435509837_play.png"));
                  sendButton.setOpaque(false);
                  sendButton.setContentAreaFilled(false);
                  sendButton.setBorderPainted(false);
                  
                  sendButton.addActionListener(new SendButtonListener());
//                  sendButton.setSize(100, 100);
                  jp.add(sendButton);
                  
                  JButton pauseButton = new JButton(new ImageIcon("1435509845_pause.png"));
                  pauseButton.setOpaque(false);
                  pauseButton.setContentAreaFilled(false);
                  pauseButton.setBorderPainted(false);
                  pauseButton.addActionListener(new StopButtonListener());
                  jp.add(pauseButton);
                  
                  terminateButton = new JButton(new ImageIcon("1435509855_stop.png"));
                  terminateButton.setOpaque(false);
                  terminateButton.setContentAreaFilled(false);
                  terminateButton.setBorderPainted(false);
                  
                  JProgressBar progressBar = new JProgressBar();  
                  progressBar.setValue(0);  
                  progressBar.setStringPainted(true);
                  progressBar.setSize(100, 20);
                  
                  UIManager.put("ProgressBar.background", Color.ORANGE);
                  UIManager.put("ProgressBar.foreground", Color.RED);
                  UIManager.put("ProgressBar.selectionBackground", Color.YELLOW);
                  UIManager.put("ProgressBar.selectionForeground", Color.gray);
                  
                  mScreen.add(progressBar, BorderLayout.SOUTH);
                  mScreen.invalidate();
                  mScreen.validate();
                  UpdateWorker updateWorker = new UpdateWorker(progressBar);  
                  updateWorker.execute();
                  
                  terminateButton.addActionListener(new terminateButtonListener());
//                  terminateButton.setSize(100, 100);
                  jp.add(terminateButton);
                  mScreen.setDefaultCloseOperation(mScreen.EXIT_ON_CLOSE);
                  jp.setBackground(Color.gray);
                  mScreen.add(jp,BorderLayout.EAST);
                  
                  
                  while(container.readNextPacket(packet) >= 0)
                  {
                	  while(currentStatus == 0){
                		  System.out.print("InWhile");
                	  }
//                	  System.out.println();
                    /*
                     * Now we have a packet, let's see if it belongs to our video stream
                     */
                    if (packet.getStreamIndex() == videoStreamId)
                    {
                      /*
                       * We allocate a new picture to get the data out of Xuggler
                       */
                      IVideoPicture picture = IVideoPicture.make(videoCoder.getPixelType(),
                          videoCoder.getWidth(), videoCoder.getHeight());
                      
                      /*
                       * Now, we decode the video, checking for any errors.
                       * 
                       */
                      int bytesDecoded = videoCoder.decodeVideo(picture, packet, 0);
                      num2 = num2 + bytesDecoded;
//                      System.out.println("sum of bytesDecoded: "+num2);
                      if (bytesDecoded < 0)
                        throw new RuntimeException("got error decoding audio in: " + filename);

                      /*
                       * Some decoders will consume data in a packet, but will not be able to construct
                       * a full video picture yet.  Therefore you should always check if you
                       * got a complete picture from the decoder
                       */
                      if (picture.isComplete())
                      {
                    	if(downloadOrNot == true){
                    		mWriter.encodeVideo(0, picture);
                    	}
                        IVideoPicture newPic = picture;
//                        num = (int)videoCoder.getExtraDataSize();
//                        System.out.println("num: "+num);
                        /*
                         * If the resampler is not null, that means we didn't get the video in BGR24 format and
                         * need to convert it into BGR24 format.
                         */
                        if (resampler != null)
                        {
                          // we must resample
                          newPic = IVideoPicture.make(resampler.getOutputPixelFormat(), 600, 500);
                          if (resampler.resample(newPic, picture) < 0)
                            throw new RuntimeException("could not resample video from: " + filename);
                        }
                        if (newPic.getPixelType() != IPixelFormat.Type.BGR24)
                          throw new RuntimeException("could not decode video as BGR 24 bit data in: " + filename);

                        long delay = millisecondsUntilTimeToDisplay(newPic);
                        // if there is no audio stream; go ahead and hold up the main thread.  We'll end
                        // up caching fewer video pictures in memory that way.
                        try
                        {
                          if (delay > 0)
                            Thread.sleep(delay);
                        }
                        catch (InterruptedException e)
                        {
                          return;
                        }

                        // And finally, convert the picture to an image and display it
//                        newPic.
                        
                        mScreen.setImage(Utils.videoPictureToImage(newPic));
                        
                        mScreen.setSize(650, 520);
//                        JButton sendButton = new JButton("暫停");
//                        sendButton.addActionListener(new SendButtonListener());
//                        sendButton.setSize(100, 100);
////                        sendButton.setLocation(500, 500);
//                        mScreen.add(sendButton, BorderLayout.WEST);
//                        
//                        
//                        terminateButton = new JButton("結束");
//                        terminateButton.addActionListener(new terminateButtonListener());
//                        terminateButton.setSize(100, 100);
////                        terminateButton.setLocation(300, 500);
//                        mScreen.add(terminateButton, BorderLayout.EAST);
//                        mScreen.setDefaultCloseOperation(mScreen.EXIT_ON_CLOSE);
                        
                        
                      }
                    }
                    else if (packet.getStreamIndex() == audioStreamId)
                    {	
                      /*
                       * We allocate a set of samples with the same number of channels as the
                       * coder tells us is in this buffer.
                       * 
                       * We also pass in a buffer size (1024 in our example), although Xuggler
                       * will probably allocate more space than just the 1024 (it's not important why).
                       */
                      IAudioSamples samples = IAudioSamples.make(1024, audioCoder.getChannels());
                      
                      /*
                       * A packet can actually contain multiple sets of samples (or frames of samples
                       * in audio-decoding speak).  So, we may need to call decode audio multiple
                       * times at different offsets in the packet's data.  We capture that here.
                       */
                      int offset = 0;
                      
                      /*
                       * Keep going until we've processed all data
                       */
                      while(offset < packet.getSize())
                      {
                        int bytesDecoded = audioCoder.decodeAudio(samples, packet, offset);
                        num2 = num2 + bytesDecoded;
                        if (bytesDecoded < 0)
                          throw new RuntimeException("got error decoding audio in: " + filename);
                        offset += bytesDecoded;
                        /*
                         * Some decoder will consume data in a packet, but will not be able to construct
                         * a full set of samples yet.  Therefore you should always check if you
                         * got a complete set of samples from the decoder
                         */
                        if (samples.isComplete())
                        {
                          if(downloadOrNot == true){
                        	  mWriter.encodeAudio(1, samples);
                          }
                          // note: this call will block if Java's sound buffers fill up, and we're
                          // okay with that.  That's why we have the video "sleeping" occur
                          // on another thread.
                          playJavaSound(samples);
                        }
                      }
                    }
                    else
                    {
                      /*
                       * This packet isn't part of our video stream, so we just silently drop it.
                       */
                      do {} while(false);
                    }
                    
                  }
                  /*
                   * Technically since we're exiting anyway, these will be cleaned up by 
                   * the garbage collector... but because we're nice people and want
                   * to be invited places for Christmas, we're going to show how to clean up.
                   */
                  if (videoCoder != null)
                  {
                    videoCoder.close();
                    videoCoder = null;
                  }
                  if (audioCoder != null)
                  {
                    audioCoder.close();
                    audioCoder = null;
                  }
                  if (container !=null)
                  {
                    container.close();
                    container = null;
                  }
                  if( mWriter != null)
                  {
                	mWriter.close();
                  }
                  closeJavaSound();
                  closeJavaVideo();
                
                  
//                //temp~~~~~~~
//                writer.println("fileData IMG_2964.flv ");
//      			writer.flush();
//              
      			
      			mScreen.remove(progressBar);
                System.out.println("\n檔案接收完畢！");
      			Thread.sleep(1000);
                
      			
      			skt_b.close();
                
                
                
    	        
    		}catch(Exception ex){
    			ex.printStackTrace();
    		}
            
		}
    	private long millisecondsUntilTimeToDisplay(IVideoPicture picture)
        {
          /**
           * We could just display the images as quickly as we decode them, but it turns
           * out we can decode a lot faster than you think.
           * 
           * So instead, the following code does a poor-man's version of trying to
           * match up the frame-rate requested for each IVideoPicture with the system
           * clock time on your computer.
           * 
           * Remember that all Xuggler IAudioSamples and IVideoPicture objects always
           * give timestamps in Microseconds, relative to the first decoded item.  If
           * instead you used the packet timestamps, they can be in different units depending
           * on your IContainer, and IStream and things can get hairy quickly.
           */
          long millisecondsToSleep = 0;
          if (mFirstVideoTimestampInStream == Global.NO_PTS)
          {
            // This is our first time through
            mFirstVideoTimestampInStream = picture.getTimeStamp();
            // get the starting clock time so we can hold up frames
            // until the right time.
            mSystemVideoClockStartTime = System.currentTimeMillis();
            millisecondsToSleep = 0;
          } else {
            long systemClockCurrentTime = System.currentTimeMillis();
            long millisecondsClockTimeSinceStartofVideo = systemClockCurrentTime - mSystemVideoClockStartTime;
            // compute how long for this frame since the first frame in the stream.
            // remember that IVideoPicture and IAudioSamples timestamps are always in MICROSECONDS,
            // so we divide by 1000 to get milliseconds.
            long millisecondsStreamTimeSinceStartOfVideo = (picture.getTimeStamp() - mFirstVideoTimestampInStream)/1000;
            final long millisecondsTolerance = 50; // and we give ourselfs 50 ms of tolerance
            millisecondsToSleep = (millisecondsStreamTimeSinceStartOfVideo -
                (millisecondsClockTimeSinceStartofVideo+millisecondsTolerance));
          }
          return millisecondsToSleep;
        }

        /**
         * Opens a Swing window on screen.
         */
        private void openJavaVideo()
        {
        	if(mScreen == null){
        		mScreen = new VideoImage();
        	}
//          mScreen.setSize(1000, 1000);
        }

        /**
         * Forces the swing thread to terminate; I'm sure there is a right
         * way to do this in swing, but this works too.
         */
        private void closeJavaVideo()
        {
//          System.exit(0);
        }

        private void openJavaSound(IStreamCoder aAudioCoder) throws LineUnavailableException
        {
          AudioFormat audioFormat = new AudioFormat(aAudioCoder.getSampleRate(),
              (int)IAudioSamples.findSampleBitDepth(aAudioCoder.getSampleFormat()),
              aAudioCoder.getChannels(),
              true, /* xuggler defaults to signed 16 bit samples */
              false);
          DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
          mLine = (SourceDataLine) AudioSystem.getLine(info);
          /**
           * if that succeeded, try opening the line.
           */
          mLine.open(audioFormat);
          /**
           * And if that succeed, start the line.
           */
          mLine.start();
          
          
        }

        private void playJavaSound(IAudioSamples aSamples)
        {
          /**
           * We're just going to dump all the samples into the line.
           */
          byte[] rawBytes = aSamples.getData().getByteArray(0, aSamples.getSize());
          mLine.write(rawBytes, 0, aSamples.getSize());
        }

        private void closeJavaSound()
        {
          if (mLine != null)
          {
            /*
             * Wait for the line to finish playing
             */
            mLine.drain();
            /*
             * Close the line.
             */
            mLine.close();
            mLine=null;
          }
        }
        public class UpdateWorker extends SwingWorker   
        {  
            JProgressBar bar = null;  
//            JFrame f=null;  
            public UpdateWorker(JProgressBar bar)  
            {  
                this.bar = bar;  
//                this.f = f;  
            }         
              
            protected String doInBackground() throws Exception {  
                Random rdm = new Random();  
                int pv = 0;  
                while(pv<100)  
                {  
                	Thread.sleep(1000);
//                    if((num/thisFileSize)*100 )
                	
                	pv = (int)((num2*100)/thisFileSize);
//                	System.out.println("num2: "+num2);
//                	System.out.println("(b)thisFileSize: "+thisFileSize);
//                	System.out.println("pv: "+pv);
                	bar.setValue(pv);
                }  
                bar.setValue(100);
//                bar.setVisible(false);
                return null;  
            }   
              
            protected void done()
            {   
//            	mScreen.remove(bar);
            	bar.setValue(0);
            	mScreen.repaint();
            	System.out.println("in done!");
//                f.setVisible(false);      
//                f.dispose();  
            }  
        }
        public class terminateButtonListener implements ActionListener{
    		public void actionPerformed(ActionEvent e) {
    			System.out.println("in terminate button~");
    			try {
    				skt_b.close();
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    			mScreen.dispose();
    		}	
    	}
        	
	}
    public class SendButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			System.out.println("ererer");
			currentStatus = 1;
		}
		
	}
    public class StopButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			System.out.println("ererer");
			currentStatus = 0;
		}
		
	}
//    public class terminateButtonListener implements ActionListener{
//		public void actionPerformed(ActionEvent e) {
//			System.out.println("in terminate button~");
////			try {
//////				skt_b.close();
////			} catch (IOException e1) {
////				// TODO Auto-generated catch block
////				e1.printStackTrace();
////			}
//			mScreen.dispose();
//		}	
//	}
     
    
}
