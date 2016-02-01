package com.drop.box;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxFiles;
import com.dropbox.core.v2.DbxFiles.CommitInfo;
import com.dropbox.core.v2.DbxFiles.FileMetadata;
import com.dropbox.core.v2.DbxFiles.UploadException;
import com.dropbox.core.v2.DbxFiles.UploadSessionAppendBuilder;
import com.dropbox.core.v2.DbxFiles.UploadSessionCursor;
import com.dropbox.core.v2.DbxFiles.UploadSessionStartResult;

public class UrlUploadMain {
	public static final long MB = 1024L * 1024L;
	static final String ACCESS_TOKEN = "<access_token>";
	static final String url_d = "<your url here>";
	
	public static void main(String[] args) {
		// Create Dropbox client
		long buffer = 67108864; // 64
		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial",
				"en_US");
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
		try {
			URL url = new URL(url_d);
			String sessionId = "";
			// Upload Files to Dropbox
			String fileName = url_d.substring(url_d.lastIndexOf('/') + 1,url_d.length());
			HttpURLConnection hc0 = (HttpURLConnection) url.openConnection();
			hc0.connect();
			long size = hc0.getContentLengthLong();
			// Start
			HttpURLConnection hc = (HttpURLConnection) url.openConnection();
			hc.addRequestProperty("Range", "bytes=0-"+buffer);
			hc.connect();
			DbxFiles.UploadSessionStartUploader re = client.files.uploadSessionStart();
			re.getBody().write(IOUtils.toByteArray(hc.getInputStream()));
			UploadSessionStartResult sa = re.finish();
			sessionId = sa.sessionId;
			hc.disconnect();
			if (size > 0) {
				System.out.println("large");
				
				long s2 = buffer;
				long tmp = 0;
				System.out.println("0"+" "+buffer);
				while (tmp <= size && (buffer + s2) < size) {
					buffer++;
					tmp = buffer + s2;
					if (tmp < size) {
						System.out.println(buffer + "\t" + (tmp)+"\t"+(tmp-buffer));
						// Append
						HttpURLConnection hc1 = (HttpURLConnection) url.openConnection();
						hc1.addRequestProperty("Range", "bytes="+buffer+"-"+tmp);
						hc1.connect();
						UploadSessionAppendBuilder re1 = client.files.uploadSessionAppendBuilder(sessionId, buffer);
						re1.run(hc1.getInputStream());
						hc1.disconnect();
						buffer = tmp;
					}
				}
				if((tmp+1)+buffer>size){
				System.out.println(tmp + 1 + "\t" + size +"\t"+(tmp-size));
				// finish
				HttpURLConnection hc2 = (HttpURLConnection) url.openConnection();
				hc2.addRequestProperty("Range", "bytes="+(tmp+1)+"-"+size);
				hc2.connect();
				UploadSessionCursor usc = new UploadSessionCursor(sessionId, (tmp+1));
				FileMetadata nn = client.files.uploadSessionFinishBuilder(
						usc,
						new CommitInfo("/" + fileName, DbxFiles.WriteMode.add,
								false, new Date(), false))
						.run(hc2.getInputStream());
				// End
				System.out.println(nn.toStringMultiline());
				}
			}
			
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UploadException e) {
			e.printStackTrace();
		} catch (DbxException e) {
			e.printStackTrace();
		}
	}
}
