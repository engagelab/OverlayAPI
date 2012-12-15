/* POIProxy. A proxy service to retrieve POIs from public services
 *
 * Copyright (C) 2011 Alberto Romeu.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * For more information, contact:
 *
 *   Prodevelop, S.L.
 *   Pza. Don Juan de Villarrasa, 14 - 5
 *   46001 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   aromeu@prodevelop.es
 *   http://www.prodevelop.es
 *   
 *   2011.
 *   author Alberto Romeu aromeu@prodevelop.es  
 *   
 */

package external;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;



/**
 * Utility class to download files throug an http connection
 * 
 * @author albertoromeu
 * 
 */
public class Downloader {

	private byte[] xmldata;
	private DownloadListener listener;

	/**
	 * Downloads a file from an http url
	 * 
	 * @param mapURL
	 *            The url string
	 * @param cancellable
	 *            A {@link Cancellable} object to cancel the download
	 * @throws Exception
	 */
	public void downloadFromUrl(String mapURL)
			throws Exception {
		xmldata = null;
		InputStream in = null;
		OutputStream out = null;
		//BufferedInputStream bis = null;
		if (mapURL == null)
			throw new Exception("Error null url");
		try {

			//long startTime = System.currentTimeMillis();

			in = new BufferedInputStream(openConnection(mapURL),
					Constants.IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, Constants.IO_BUFFER_SIZE);
			byte[] b = new byte[8 * 1024];
			int read;
			@SuppressWarnings("unused")
			int total = 0;
			while ((read = in.read(b)) != -1) {
				total += read;
				out.write(b, 0, read);
				if (listener != null) {
					listener.onBytesDownloaded(read);
				}
				// System.gc();
			}
			out.flush();
			xmldata = dataStream.toByteArray();

		} catch (IOException e) {

			//xmldata = "Instagram Internal Error"
			new Exception(e);
			String error = e.getMessage();
			xmldata = error.getBytes();
			//throw new Exception(e);
		} finally {
			Constants.closeStream(in);
			Constants.closeStream(out);
		}
	}



	/**
	 * Returns the data downloaded. Call this method after one of the download
	 * methods finishes
	 * 
	 * @return An array of bytes with the data downloaded
	 */
	public byte[] getData() {
		return xmldata;
	}

	/**
	 * Opens an http connection. By default the connection and read timeouts is
	 * set to 15 seconds
	 * 
	 * @param query
	 *            The url to connect
	 * @return An InputStream
	 * @throws IOException
	 */
	public static InputStream openConnection(String query) throws IOException {
		final URL url = new URL(query.replace(" ", "%20"));
		URLConnection urlconnec = url.openConnection();
		urlconnec.setRequestProperty("Accept", "application/json");
		urlconnec.setConnectTimeout(15000);
		urlconnec.setReadTimeout(15000);
		return urlconnec.getInputStream();
	}

	public void setDownloadListener(DownloadListener listener) {
		this.listener = listener;
	}
}