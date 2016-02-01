package com.drop.box;

import java.io.File;
import java.text.DecimalFormat;

public class ChunkedStream {
	public static final long MB = 1024L * 1024L;

	public static void main(String[] args) {
		File f = new File(
				"C:\\Users\\PankajKumar\\Downloads\\video\\The.Man.from.U.N.C.L.E.2015.1080p.BluRay.x264-Type40.mp4");
		if (f.exists()) {
			double size = (double) f.length() / MB;
			if (size > 150) {
				System.out.println("large");
				long ss = 67108864; // 64
				long s2 = ss;
				long tmp = 0;
				System.out.println("0"+" "+ss);
				while (tmp <= f.length() && (ss + s2) < f.length()) {
					ss++;
					tmp = ss + s2;
					if (tmp < f.length()) {
						System.out.println(ss + "\t" + (tmp)+"\t"+(tmp-ss));
						ss = tmp;
					}
				}
				long s = f.length();
				if((tmp+1)+ss>s)
				System.out.println(tmp + 1 + "\t" + s +"\t"+(tmp-s));
				System.out.println(s);
			}
		} else {
			System.out.println("File does'nt exists");
		}
	}
}
