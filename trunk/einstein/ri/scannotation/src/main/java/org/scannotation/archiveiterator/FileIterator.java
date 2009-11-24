package org.scannotation.archiveiterator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@org.contract4j5.contract.Contract
public class FileIterator implements StreamIterator {
    private final ArrayList files;
    private int index = 0;

    public FileIterator(File file, Filter filter) {
        files = new ArrayList();
        try {
            create(files, file, filter);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void create(List list, File dir, Filter filter) throws Exception {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                create(list, file, filter);
            } else {
                if (filter == null || filter.accepts(file.getAbsolutePath())) {
                    list.add(file);
                }
            }
        }
    }

    public InputStream next() {
        if (index >= files.size()) return null;
        File fp = (File) files.get(index++);
        try {
            return new FileInputStream(fp);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
    }
}
