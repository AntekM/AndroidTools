package com.mysliborski.tools.helper;

import com.mysliborski.tools.exception.ConnectionProblemException;
import com.mysliborski.tools.exception.ServiceException;

import java.io.*;

import static com.mysliborski.tools.helper.LogHelper.log;

/**
 * Created by antonimysliborski on 03/09/2013.
 */
public class IOHelper {

    public static int copyToStream(InputStream inputStream, OutputStream outputStream) throws ServiceException {
        BufferedOutputStream bfos = null;
        BufferedInputStream bfin = null;
        try {
            int t=0;
            try {
                bfos = new BufferedOutputStream(outputStream);
                bfin = new BufferedInputStream(inputStream);
                byte[] buf = new byte[8192];
                int r=0;
                do {
                    r = bfin.read(buf);
                    if (r > 0) {
                        bfos.write(buf, 0, r);
                        t+=r;
                    }
                } while (r > 0);
            } finally {
                bfos.close();
                bfin.close();
            }
            return t;
        } catch (IOException e) {
            throw new ConnectionProblemException();
        }
    }

    public static byte[] getInputStreamContent(InputStream inputStream) throws ServiceException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        copyToStream(inputStream, bos);
        return bos.toByteArray();
    }


}
