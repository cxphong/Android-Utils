package common.android.fiot.androidcommon;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * Created by dinhtho on 14/04/2017.
 */

public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * Check SDCard is available
     */
    public static boolean isSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * Checks if external storage is available for read and write
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @return is Internal Storage Available
     */
    public static boolean isInternalStorageAvailable(Context context) {
        return new File(context.getFilesDir().toString()).exists();
    }

    /**
     * @return SDCard RootPath
     */
    public static String getSDCardRootPath() {
        if (isSDCardAvailable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        return null;
    }

    /**
     * @param context
     * @return Internal Storage Path
     */
    public static String getInternalStoragePath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * @return free size External Storage in bytes
     */
    public static long getFreeSDCardSize() {
        if (isSDCardAvailable()) {
            File path = new File(getSDCardRootPath());
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        }

        return 0;
    }

    /**
     * @param context
     * @return free size Internal Storage in bytes
     */
    public static long getFreeInternalAvailableSize(Context context) {
        long freeBytesInternal = new File(context.getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        return freeBytesInternal;
    }

    /**
     * Get Path of folder of the app
     *
     * @param context
     * @return NULL if folder is not exist
     */
    public static String getAppFolderInSDCardPath(Context context) {
        String extStorageDirectory = getSDCardRootPath();
        File folder = new File(extStorageDirectory + "/" + context.getResources().getString(R.string.app_name));

        if (folder.exists()) {
            return folder.getAbsolutePath();
        }

        return null;
    }

    /**
     * Create folder with name is app's name
     *
     * @param context
     */
    public static void createAppFolderInSDCard(Context context) {
        if (isSDCardAvailable()) {
            String extStorageDirectory = getSDCardRootPath();
            File folder = new File(extStorageDirectory + "/" + context.getResources().getString(R.string.app_name));

            if (!folder.exists()) {
                folder.mkdir();
            }
        }
    }

    /**
     * Create folder in Root path of SDCard
     *
     * @param nameFolder
     */
    public static void createFolderInRootSDCard(String nameFolder) {
        if (nameFolder == null || nameFolder.length() == 0) {
            return;
        } else if (isSDCardAvailable() && isExternalStorageWritable()) {
            File f = new File(Environment.getExternalStorageDirectory(), nameFolder);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }

    /**
     * Create folder in app folder
     *
     * @param nameFolder
     * @param context
     */
    public static void createFolderInAppFolder(Context context, String nameFolder) {
        if (nameFolder == null || nameFolder.length() == 0) {
            return;
        }

        if (!isExternalStorageWritable()) return;

        createAppFolderInSDCard(context);
        createFolder(getAppFolderInSDCardPath(context) + "/" + nameFolder);
    }

    /**
     * @param folderPath
     * @return
     */
    public static boolean createFolder(String folderPath) {
        if (folderPath == null || folderPath.length() == 0) return false;
        if (!isExternalStorageWritable()) return false;

        File f = new File(folderPath);
        if (f.exists() && f.isDirectory()) {
            return true;
        } else {
            return f.mkdirs();
        }
    }

    /**
     * @param nameFolder
     * @return
     */
    public static boolean createFolderInInternalStorage(Context context, String nameFolder) {
        if (nameFolder == null || nameFolder.length() == 0) {
            return false;
        }

        if (!isInternalStorageAvailable(context)) return false;

        File file = new File(context.getFilesDir(), nameFolder);
        if (file.exists() && file.isDirectory()) {
            return true;
        } else {
            return file.mkdirs();
        }
    }

    /**
     * @param fileName
     * @param context
     */
    public static void createFileInAppFolder(String fileName, Context context) {
        if (fileName == null || fileName.length() == 0) {
            return;
        }

        File f = new File(getAppFolderInSDCardPath(context) + "/" + fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param fileName
     */
    public static void createFileInRootSDCard(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return;
        } else if (isSDCardAvailable()) {
            File f = new File(getSDCardRootPath(), fileName);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param filename
     * @param context
     */
    public static void createFileInternal(String filename, Context context) {
        if (filename == null || filename.length() == 0) {
            return;
        }
        File file = new File(context.getFilesDir(), filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get size of a file in byte
     *
     * @param f
     * @return size file
     */
    public static long getFileSize(File f) throws FileNotFoundException {
        if (!f.exists() || !f.isFile()) {
            throw new FileNotFoundException(null);
        }

        return f.length();
    }

    /**
     * @param f
     * @return kind of f
     */
    public static String getFileKind(File f) throws FileNotFoundException {
        if (!f.exists() || !f.isFile()) {
            throw new FileNotFoundException(null);
        }
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(f.getPath());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    /**
     * @param f
     * @return epoch
     */
    public static long getFileModified(File f) throws FileNotFoundException {
        if (f.isFile() && f.exists()) {
            return f.lastModified();
        }


        throw new FileNotFoundException(null);
    }

    /**
     * @param f
     * @return Extension of f
     */
    public static String getFileExtension(File f) throws FileNotFoundException {
        if (!f.exists() || !f.isFile()) {
            throw new FileNotFoundException(null);
        }
        String fileName = f.getName();
        int index = fileName.lastIndexOf(".");

        if (index == -1) {
            return null;
        }

        String tail = fileName.substring(index + 1, fileName.length());
        if (tail.length() > 0) {
            return tail;
        }

        return null;
    }

    /**
     * (NOT TEST)
     *
     * @param f
     * @return checksum file
     */
    public static String checksumFile(File f) {
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        String returnVal = "";
        try {
            InputStream input = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            MessageDigest md5Hash = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while (numRead != -1) {
                numRead = input.read(buffer);
                if (numRead > 0) {
                    md5Hash.update(buffer, 0, numRead);
                }
            }
            input.close();

            byte[] md5Bytes = md5Hash.digest();
            for (int i = 0; i < md5Bytes.length; i++) {
                returnVal += Integer.toString((md5Bytes[i] & 0xff) + 0x100, 16).substring(1);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return returnVal.toUpperCase();
    }

    /**
     * @param filePath
     * @param folderPath
     * @throws FileNotFoundException
     */
    public static void copyFile(String filePath, String folderPath) throws FileNotFoundException {
        File file = new File(filePath);
        File dir = new File(folderPath);

        if (!file.exists() || !file.isFile() || !dir.exists() || !dir.isDirectory()) {
            throw new FileNotFoundException(null);
        }
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputChannel != null) try {
                inputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (outputChannel != null) try {
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param filePath
     */
    public static void duplicateFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            return;
        }
        int index = 1;
        String tail = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
        String body = file.getName().substring(0, file.getName().lastIndexOf("."));
        String filename = body + "(" + index + ")." + tail;
        File f = new File(String.valueOf(file.getParentFile()));
        for (int i = 0; i < f.listFiles().length; i++) {
            if (filename.equalsIgnoreCase(f.listFiles()[i].getName())) {
                index++;
                filename = body + "(" + index + ")." + tail;
                i = 0;

            }
        }
        File newFile = new File(file.getParentFile(), filename);
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputChannel != null) try {
                inputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (outputChannel != null) try {
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param f
     * @return list files from folder
     */
    public static File[] listFolder(File f) {
        if (!f.exists() || !f.isDirectory()) {
            return null;
        }
        return f.listFiles();
    }

    /**
     * @param path
     * @throws FileNotFoundException
     */
    public static void deleteFile(String path) throws FileNotFoundException {
        File f = new File(path);

        if (!f.exists()) {
            throw new FileNotFoundException(null);
        }

        f.delete();
    }

    /**
     * @param path
     * @return
     */
    public static boolean deleteDirectory(String path) {
        File directory = new File(path);

        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i].getAbsolutePath());
                    } else {
                        files[i].delete();
                    }
                }
            }
        }

        return (directory.delete());
    }

    /**
     * @param filePath
     * @param folderPath
     */
    public static void moveFile(String filePath, String folderPath) {
        File file = new File(filePath);
        File dir = new File(folderPath);

        if (!file.exists() || !file.isFile() || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        File newFile = new File(dir, file.getName());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
            file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputChannel != null) try {
                inputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (outputChannel != null) try {
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param f
     * @return string in f
     */
    public static String readFileString(File f) {

        if (!f.exists() || !f.isFile()) {
            return null;
        }
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String ret = null;
        try {
            ret = convertStreamToString(fin);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static void appendFileString(String data, File f) {
        if (data == null) {
            return;
        }
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        Boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                sb.append(line);
                firstLine = false;
            } else {
                sb.append("\n").append(line);
            }
        }
        reader.close();
        return sb.toString();
    }

    public static byte[] readBinaryFile(File f) throws IOException {
        if (!f.exists() || !f.isFile()) {
            throw new FileNotFoundException(null);
        }

        byte[] fileData = new byte[(int) f.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(f));
        dis.readFully(fileData);
        dis.close();

        return fileData;
    }

    /**
     *
     * @param data
     * @param nameFile
     */
    public static void saveStringToFile(String data, String nameFile) {
        if (nameFile == null || nameFile.length() == 0 || data == null) {
            return;
        }

        File log = new File(nameFile);
        if (!log.exists()) {
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Writer writer = new BufferedWriter(new FileWriter(log));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            Log.w("eztt", e.getMessage(), e);
        }
    }

    /**
     *
     * @param data
     * @param nameFile
     */
    public static void saveBinaryToFile(byte[] data, String nameFile) {
        if (nameFile == null || nameFile.length() == 0 || data == null) {
            return;
        }
        if (isSDCardAvailable()) {
            File log = new File(nameFile);

            FileOutputStream fOut;
            try {
                fOut = new FileOutputStream(log);
                fOut.write(data, 0, data.length);
                fOut.flush();
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static long getFolderSize(File dir) {

        if (dir.exists() && dir.isDirectory()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    result += getFolderSize(fileList[i]);
                } else {
                    result += fileList[i].length();
                }
            }
            return result;
        }
        return 0;
    }


    public static long getFolderModified(File f) {
        if (f.exists() && f.isDirectory()) {
            return f.lastModified();
        }
        return 0;
    }

}


