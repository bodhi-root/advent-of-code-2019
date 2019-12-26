package day08;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Main {

	public static void main(String [] args) {
		try {
			int [] data = loadImageData(new File("files/day08/input.txt"));
			System.out.println(data.length);

			Image image = Image.loadFromData(data, 25, 6);
			int bestLayer = image.findLayerWithFewestMatches(0);
			int countOnes = image.countMatchesInLayer(bestLayer, 1);
			int countTwos = image.countMatchesInLayer(bestLayer, 2);
			System.out.println("Ones times Twos: " + (countOnes * countTwos));
			
			image.render();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	static class Image {
		
		int [][][] data;
		int layers;
		int width;
		int height;
		
		public Image(int [][][] data) {
			this.data = data;
			this.layers = data.length;
			this.height = data[0].length;
			this.width = data[0][0].length;
		}
	
		public int findLayerWithFewestMatches(int value) {
			int bestLayer = -1;
			int bestCount = -1;
			
			for (int i=0; i<layers; i++) {
				int count = countMatchesInLayer(i, value);
				if (bestCount < 0 || count < bestCount) {
					bestCount = count;
					bestLayer = i;
				}
			}
			
			return bestLayer;
		}
		public int countMatchesInLayer(int layerIndex, int value) {
			int count = 0;
						
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					if (data[layerIndex][i][j] == value)
						count++;
				}
			}
			
			return count;
		}
		
		public static Image loadFromData(int [] data, int width, int height) {
		
			int layers = data.length / width / height;
			if (data.length != (layers * width * height))
				throw new IllegalArgumentException("Image length must be a multiple of width x height");
			
			int [][][] resized = new int[layers][height][width];
			int i=0;
			for (int iLayer=0; iLayer<layers; iLayer++) {
				for (int iHeight=0; iHeight<height; iHeight++) {
					for (int iWidth=0; iWidth<width; iWidth++) {
						resized[iLayer][iHeight][iWidth] = data[i++];
					}
				}
			}
			
			return new Image(resized);
		}
		
		/**
		 * Flattens the image and prints it out
		 */
		public void render() {
			int [][] flat = flattenImage();
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					if (flat[i][j] == 0)
						System.out.print(' ');
					else if (flat[i][j] == 1)
						System.out.print('#');
					else
						System.out.print('?');
				}
				System.out.println();
			}
		}
		
		/**
		 * Flattens the image
		 * 0 = black
		 * 1 = white
		 * 2 = transparent
		 */
		protected int [][] flattenImage() {
			int [][] flattenedImage = new int[height][width];
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					for (int iLayer=0; iLayer<layers; iLayer++) {
						flattenedImage[i][j] = 2;	//start with transparent
						int value = data[iLayer][i][j];
						if (value == 0 || value == 1) {
							flattenedImage[i][j] = value;
							break;
						}
					}
				}
			}
			return flattenedImage;
		}
		
	}
	
	public static int [] loadImageData(File file) throws IOException {
		StringBuilder s = new StringBuilder();
		Reader in = new FileReader(file);
		try {
			char [] chars = new char[512];
			int charsRead;
			while ((charsRead = in.read(chars)) > 0)
				s.append(chars, 0, charsRead);
		}
		finally {
			in.close();
		}
		
		String text = s.toString().trim();
		int [] data = new int[text.length()];
		for (int i=0; i<data.length; i++)
			data[i] = Integer.parseInt(text.substring(i, i+1));
		
		return data;
	}
	
}
