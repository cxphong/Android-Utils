package common.android.fiot.androidcommon;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
     *
     * @param context
     * @return is Internal Storage Available
     */
    public static boolean isInternalStorageAvailable(Context context) {
        return new File(context.getFilesDir().toString()).exists();
    }

    /**
     *
     * @return SDCard RootPath
     */
    public static String getSDCardRootPath() {
        if (isSDCardAvailable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        return null;
    }

    /**
     *
     * @param context
     * @return Internal Storage Path
     */
    public static String getInternalStoragePath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     *
     * @return free size External Storage
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
     *
     * @param context
     * @return  free size Internal Storage
     */
    public static long getFreeInternalAvailableSize(Context context) {
        long freeBytesInternal = new File(context.getFilesDir().getAbsoluteFile().toString()).getFreeSpace();
        return freeBytesInternal;
    }

    public static String getAppFolderInSDCardPath(Context context) {
        return getSDCardRootPath() + "/" + context.getResources().getString(R.string.app_name);
    }

    /**
     * Create folder with name is app's name
     * @param context
     */
    public static void createAppFolderInSDCard(Context context) {
        if (isSDCardAvailable()) {
            String extStorageDirectory = getSDCardRootPath();
            File folder = new File(extStorageDirectory +  "/" + context.getResources().getString(R.string.app_name));

            Log.i(TAG, "createAppFolderInSDcard: " + folder.getAbsolutePath());
            if (!folder.exists()) {
                Log.i(TAG, "createAppFolderInSDcard: dd");
                folder.mkdir();
            }
        }
    }

    /**
     * Create folder in Root path of SDCard
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
     * @param nameFolder
     * @param context
     */
    public static void createFolderInAppFolder(String nameFolder, Context context) {
        if (nameFolder == null || nameFolder.length() == 0) {
            return;
        }

        if (!isExternalStorageWritable()) return;

        createAppFolderInSDCard(context);
        createFolder(getAppFolderInSDCardPath(context) + "/" + nameFolder);
    }

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

    public static boolean createFolderInInternalStorage(String nameFolder, Context context) {
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

    public static void createFileInAppFolder(String fileName, Context context) {
        if (fileName == null || fileName.length() == 0) {
            return;
        }
        File f = context.getDir(fileName, Context.MODE_PRIVATE);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createFileInRootSDCard(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return;
        } else if (isSDCardAvailable()) {
            File f = new File(Environment.getExternalStorageDirectory(), fileName);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
     *
     * @param f
     * @return kind of f
     */
    public static String getFileKind(File f) {
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(f.getPath());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static long getFileModified(File f) {
        if (f.isFile() && f.exists()) {
            return f.lastModified();
        }
        return 0;
    }

    /**
     *
     * @param f
     * @return Extension of f
     */
    public static String getFileExtension(File f) {
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        String fileName = f.getName();
        String tail = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        if (tail.length() > 0) {
            return tail;
        }

        return null;
    }

    /**
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

    public static void copyfile(File file, File dir) {
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

    public static void douplicateFile(File file) {
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
     *
     * @param f
     * @return list files from folder
     */
    public static File[] listFolder(File f) {
        if (!f.exists() || !f.isDirectory()) {
            return null;
        }
        return f.listFiles();
    }

    public static void deleteFile(File f) {
        if (!f.exists()) {
            return;
        }
        f.delete();
    }

    public static void moveFile(File file, File dir) {
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
     *
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
        if(!f.exists()){
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

    /**
     *
     * @param f
     * @return bytes array
     * @throws IOException
     */
    public static byte[] readBinaryFile(File f) throws IOException {
        if (!f.exists() || !f.isFile()) {
            return null;
        }
        InputStream inputStream = new FileInputStream(f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] data = new byte[4096];
        int count = inputStream.read(data);
        while (count != -1) {
            dos.write(data, 0, count);
            count = inputStream.read(data);
        }

        return baos.toByteArray();
    }

    public static void saveStringToSDCard(String data, String nameFile) {
        if (nameFile == null || nameFile.length() == 0 || data == null) {
            return;
        }
        if (isSDCardAvailable()) {
            File log = new File(Environment.getExternalStorageDirectory(), nameFile);
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            try {

                myOutWriter.write(data);
                myOutWriter.close();
                fOut.close();
            } catch (Exception e) {
                Log.e(TAG, "Error opening Log.", e);
            }

        } else {
            File fp = new File(System.getenv("SECONDARY_STORAGE").toString(), "NewFolder");
            fp.mkdir();
            if (!fp.exists()) {
                return;
            }
            String secStore = System.getenv("SECONDARY_STORAGE").toString();
            File log = new File(secStore, nameFile);
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(log);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            try {

                myOutWriter.write(data);
                myOutWriter.close();
                fOut.close();
            } catch (Exception e) {
                Log.e(TAG, "Error opening Log.", e);
            }
        }

    }

    public static void saveBinaryToSDCard(byte[] data, String nameFile) {
        if (nameFile == null || nameFile.length() == 0 || data == null) {
            return;
        }
        if (isSDCardAvailable()) {
            File log = new File(Environment.getExternalStorageDirectory(), nameFile);
            FileOutputStream fOut = null;
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

    /**
     *
     * @param dir
     * @return size folder
     */
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

    /**
     * @param f
     * @return epochtime
     */
    public static long getFolderModified(File f) {
        if (f.exists() && f.isDirectory()) {
            return f.lastModified();
        }
        return 0;
    }

}


