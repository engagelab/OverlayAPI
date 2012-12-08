package external;
/* gvSIG Mini. A free mobile phone viewer of free maps.
 *
 * Copyright (C) 2010 Prodevelop.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
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
 *   prode@prodevelop.es
 *   http://www.prodevelop.es
 *
 *   gvSIG Mini has been partially funded by IMPIVA (Instituto de la PequeÔøΩa y
 *   Mediana Empresa de la Comunidad Valenciana) &
 *   European Union FEDER funds.
 *   
 *   2010.
 *   author Alberto Romeu aromeu@prodevelop.es
 */


import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Constants {

	public final static String SERVER_NAME = "http://129.240.213.227";
	public final static String LAYERS_VERSION = "v0.2.2";
	public static String ROOT_DIR = ".";
	public final static String APP_DIR = "gvSIG";
	public final static String MAPS_DIR = "maps";
	public final static String CONFIG_DIR = "config";
	public final static String LAYERS_DIR = "layers";
	public final static String LOG_DIR = "logs";
	public static int BUFFER_SIZE = 2;
	public static int ROTATE_BUFFER_SIZE = 2;
	public final static int COMPASS_ACCURACY = 1;
	public final static int MIN_ROTATION = 10;
	public final static int REPAINT_TIME = 200;	
	
	public final static String URL_STRING = "URL_SRING";
	public final static String EXTENT_STRING = "EXTENT_STRING";
	public final static String NAME_STRING = "NAME_STRING";

	public static final int IO_BUFFER_SIZE = 8 * 1024;
	
	public static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	public static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {

			}
		}
	}

	//		

	public static int getNextSquareNumberAbove(final double factor) {
		int out = 0;
		int cur = 1;
		int i = 1;
		while (true) {
			if (cur > factor) {
				return out;
			}

			out = i;
			cur *= 2;
			i++;
		}
	}

}
