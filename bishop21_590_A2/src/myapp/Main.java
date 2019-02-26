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

public class Main {
	public static void main(String[] args) throws InsufficientBitsLeftException, IOException {
		//this code computes the difference in pixel values for all frames
		//System.out.println((int)'s');
		
		//FrameDiff.makeFrameDiff();
		
		/*
		 * the EncodeTextFile code still works with modifications. Instead of encoding
		 *  8-bit chars, we're encoding 8-bit integers that represent the difference 
		 *  between two pixels in the same location over time.
		 *  Our range of values increased from [0, 255] to [-255,255], in theory.
		 *  To remedy this, I have shifted all bits up by 115 in frames after the first
		 *  This arbitrary value leaves me with only positive integers in the range [0,255]
		 *  In reality, the differentials range from [-115,131], so I shifted accordingly
		 */
		
		//StaticACEncodeTextFile.encode();
		
		//the parameters for EncodeTextFile such as the file names have been modified from 
		//their original implementations
		//FILE LOCATIONS- all are in the /dat subfolder
		//original video: out.dat
		//frame-by-frame differentials: differentials.dat
		//compressed video: static-compressed-video.dat
		//reuncompressed video: reuncompressed-video.dat
		
		//StaticACDecodeTextFile.decode();
		
		//since the first frame is unable to be effectively compressed using differentials
		//I have skipped over it for both encoding and decoding steps
		//in both the encode and decode functions, I simply copy the first frame in as 
		//raw, uncompressed data-- as part of the header
		
		FrameDiff.unmakeFrameDiff();
		
	}
}

