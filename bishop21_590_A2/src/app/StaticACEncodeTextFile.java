package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import ac.ArithmeticEncoder;
import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;
import io.OutputStreamBitSink;

public class StaticACEncodeTextFile {

	//public static void main(String[] args) throws IOException {
	public static void encode() throws IOException, InsufficientBitsLeftException{
		String input_file_name = "data/differentials.dat";
		String output_file_name = "data/static-compressed-video.dat";
		InputStreamBitSource isbs = new InputStreamBitSource(new FileInputStream(new File("data/out.dat")));
		int range_bit_width = 40;
		
		System.out.println("Encoding video file: " + input_file_name);
		System.out.println("(by using a static aritmetic coder)");
		System.out.println("Output file: " + output_file_name);
		System.out.println("Range Register Bit Width: " + range_bit_width);

		int num_symbols = (int) new File(input_file_name).length();
		
		
		System.out.println("Symbols to encode: " + num_symbols);
		// Analyze file for frequency counts
		
		FileInputStream fis = new FileInputStream(input_file_name);

		int[] symbol_counts = new int[256];
		
		int next_byte = fis.read();	

		while (next_byte != -1) {
			symbol_counts[next_byte]++;
			next_byte = fis.read();
		}
		fis.close();
		
		//******
		//Uncomment for verbose prints of each differential and its probability 
		for(int i=0;i<256;i++) {
			//System.out.println("Differential of " + (i-115) + " occurs " + symbol_counts[i] + " times.");
		}
		Integer[] symbols = new Integer[256];
		for (int i=0; i<256; i++) {
			symbols[i] = i;
		}

		// Create new model with analyzed frequency counts
		FreqCountIntegerSymbolModel model = new FreqCountIntegerSymbolModel(symbols, symbol_counts);

		ArithmeticEncoder<Integer> encoder = new ArithmeticEncoder<Integer>(range_bit_width);

		FileOutputStream fos = new FileOutputStream(output_file_name);
		OutputStreamBitSink bit_sink = new OutputStreamBitSink(fos);


		
		// First 256 * 4 bytes are the frequency counts 
		for (int i=0; i<256; i++) {
			bit_sink.write(symbol_counts[i], 32);
		}

		// Next 4 bytes are the number of symbols encoded
		bit_sink.write(num_symbols, 32);		

		// Next byte is the width of the range registers
		bit_sink.write(range_bit_width, 8);

		//**********MODIFIED
		//  Now we add the first frame
		//	We have to use the for loop to read in ints that are 32 bits long
		// 1024*32 = 32768 bits / 8 = 4096 bytes (size of one frame)
		
		for(int i=0;i<1024;i++) {
			bit_sink.write(isbs.next(32), 32);
		}
		
		
		// Now encode the input
		fis = new FileInputStream(input_file_name);
		/*
		for(int i=0;i<4096;i++) {
			bit_sink.write(fis.read(), 8);
		}
		*/
		for (int i=0; i<num_symbols; i++) {
			int next_symbol = fis.read();
			encoder.encode(next_symbol, model, bit_sink);
		}
		fis.close();

		// Finish off by emitting the middle pattern 
		// and padding to the next word
		
		encoder.emitMiddle(bit_sink);
		bit_sink.padToWord();
		fos.close();
		
		System.out.println("Done");
	}
}
