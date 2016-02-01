package com.drop.box;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.dropbox.core.v2.DbxFiles.WriteMode;

public class Drop {

	static final String ACCESS_TOKEN = "<>";
	static final String url_d = "<>";

	// magnet:?xt=urn:btih:0CCA4ED45B20F4BADADF02552D297BA2642E1009&dn=jquery+gems+the+easy+guide+to+the+javascript+library+for+beginners&tr=udp%3A%2F%2Ftracker.publicbt.com%2Fannounce&tr=udp%3A%2F%2Fglotorrents.pw%3A6969%2Fannounce
	// http://onto.herokuapp.com/download/jQuery%20Gems%20The%20easy%20guide%20to%20the%20JavaScript%20library%20for%20beginners/jQueryGems.tgz
	public static void main(String args[]) {
		// Create Dropbox client
		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial",
				"en_US");
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
		try {
			URL url = new URL(url_d);
			// Upload Files to Dropbox
			String fileName = url_d.substring(url_d.lastIndexOf('/') + 1,
					url_d.length());
			HttpURLConnection hc0 = (HttpURLConnection) url.openConnection();
			hc0.connect();
			long l = hc0.getContentLengthLong();
			System.out.println(l);
			// Start
			HttpURLConnection hc = (HttpURLConnection) url.openConnection();
			hc.addRequestProperty("Range", "bytes=0-256");
			hc.connect();
			DbxFiles.UploadSessionStartUploader re = client.files.uploadSessionStart();
			re.getBody().write(IOUtils.toByteArray(hc.getInputStream()));
			UploadSessionStartResult sa = re.finish();
			String ss = sa.sessionId;
			System.out.println(ss);
			hc.disconnect();
			// Append
			HttpURLConnection hc1 = (HttpURLConnection) url.openConnection();
			hc1.addRequestProperty("Range", "bytes=257-512");
			hc1.connect();
			UploadSessionAppendBuilder re1 = client.files.uploadSessionAppendBuilder(ss, 257);
			re1.run(hc1.getInputStream());
			hc1.disconnect();
			// finish
			HttpURLConnection hc2 = (HttpURLConnection) url.openConnection();
			hc2.addRequestProperty("Range", "bytes=513-");
			hc2.connect();
			UploadSessionCursor usc = new UploadSessionCursor(ss, 513);
			FileMetadata nn = client.files.uploadSessionFinishBuilder(
					usc,
					new CommitInfo("/" + fileName, DbxFiles.WriteMode.add,
							false, new Date(), false))
					.run(hc2.getInputStream());
			// End
			System.out.println(nn.toStringMultiline());
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
