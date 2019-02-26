package myapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import app.StaticACDecodeTextFile;
import app.StaticACEncodeTextFile;
import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;
import io.OutputStreamBitSink;

public class FrameDiff {
	public static void makeFrameDiff() throws InsufficientBitsLeftException, IOException {
		//MakeStaticTableHeader.makeHeader();
		String fileLoc = "data/out.dat";
		InputStreamBitSource isbs = new InputStreamBitSource(new FileInputStream(new File(fileLoc)));
		OutputStreamBitSink osbs = new OutputStreamBitSink(new FileOutputStream(new File("data/differentials.dat")));
		System.out.println("Reading video file...");
		//each frame is 64*64 bytes, so a three-dimensional array would work to hold 
		//frames over time.
		//we can also convert each frame into a frame full of differentials
		//the first frame will be raw (as if the previous (h==[-1]) frame were full of 0's), 
		//and all successive frames will be differential

		int [][][] frame= new int[300][64][64];
		int [][][] frameDiff = new int[299][64][64];
		for(int h=0;h<300;h++) {				//300 frames
			for(int i=0;i<64;i++) {				//each row
				for(int j=0;j<64;j++) {			//each column
					frame[h][i][j] = isbs.next(8);
					if(h==0) {
						osbs.write(frame[h][i][j], 8);
					}
					if(h>0) {
						frameDiff[h-1][i][j] = frame[h][i][j]-frame[h-1][i][j]+115; 
						osbs.write(frameDiff[h-1][i][j], 8);
						if(frameDiff[h-1][i][j]>255)
							{System.out.println("offset is too high");}
						if(frameDiff[h-1][i][j]<0)
							{System.out.println("offset is too low");}
					}
				
				}
			}
		}
		System.out.println(frame[23][18][44]);
		System.out.println(frame[0][0][1]);
		System.out.println(frame[0][0][2]);
		System.out.println("Video file has been parsed into differentials, stored in data/differentials.dat");
	}	
	
	public static void unmakeFrameDiff() throws InsufficientBitsLeftException, IOException {
		InputStreamBitSource isbs = new InputStreamBitSource(new FileInputStream(new File("data/to-be-undifferentiated.dat")));
		OutputStreamBitSink osbs = new OutputStreamBitSink(new FileOutputStream(new File("data/reuncompressed.dat")));
		System.out.println("Un-Differentiating video file...");
		/*
		for(int i=0;i<257;i++) {
			isbs.next(32);	//4 bytes at a time
		}
		isbs.next(8);		//one last byte
		*/
		
		int [][][] frame= new int[300][64][64];
		int [][][] frameDiff = new int [299][64][64];
		for(int i=0;i<64;i++) {
			for(int j=0;j<64;j++) {
				frame[0][i][j] = isbs.next(8);
			}
		}
		for(int h=1;h<300;h++) {				//300 frames
			for(int i=0;i<64;i++) {				//each row
				for(int j=0;j<64;j++) {			//each column
					/*
					if(h==0) { 
						//int temp = isbs.next(8);
						frame[h][i][j] = isbs.next(8);	//grabs first frame
						//frame[h][i][j] = temp;
						//System.out.println((char)(temp));
						osbs.write(frame[h][i][j], 8);	//writes first frame to file
					}
					*/
					if(h!=300) {
						frameDiff[h][i][j] = isbs.next(8)-115;
						//System.out.println(frameDiff[h][i][j]);
						frame[h][i][j] = frame[h-1][i][j]+frameDiff[h-1][i][j];//-115; 
						//System.out.println(frame[h][i][j]);
						osbs.write(frame[h][i][j], 8);
					//}
					//*/
					}
				}
			}
		}
		
		System.out.println(frame[0][0][0] == 102);
		System.out.println(frame[0][0][63] == 101);
		System.out.println(frame[0][63][0] == 68);
		System.out.println(frame[0][63][63] == 185);
		System.out.println(frame[1][0][0] == 102);
		System.out.println(frame[23][18][44]); //== 157);
		System.out.println(frame[2][0][0]); //== 98);
		
		for(int i=0;i<50;i++) {
			//System.out.println(frame[0][0][i]);
		}
		System.out.println("Done! Reuncompressed file can be found in /data/reuncompressed.dat");
	}
	
	
	
	
	
	
	
	
	
	
	
	
}