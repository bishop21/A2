Notes about this program:

It's a little flawed- for some reason (for loop indices, probably)-- the last two frames of the video file are missing in the 
reuncompressed file. 

It uses differential coding over time (temporal) in order to achieve better compression

It uses a static aritmetic encoder that ignores the first frame (t=0)

The file produces many intermediate files as it does its work. The stream of data is shown below:
  FILE NAME                     | DESCRIPTION
1. out.dat                      | the original video file
|     makeFrameDiff() is called here
2. differentials.dat            | the video file with each frame represented as the difference from the previous frame
|     staticACEncodeTextFile() is called here
3. static-compressed-video.dat  | the compressed file with all the stuff you need to decompress into the original
|     staticACDecodeTextFile() is called here
4. to-be-undifferentiated.dat   | the video file with each frame represented as the difference from the previous frame
|     unmakeFrameDiff() is called here
5. reuncompressed.dat           | the original video file
