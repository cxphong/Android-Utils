package com.example.dinhtho.fileutils;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by dinhtho on 14/04/2017.
 */

public class FileUtils {
    private static final String TAG = "FileUtils";
    /**
     *
     * @return is Removeable SDCard Exist
     */
    public static boolean isRemoveableSDCardExist() {
        return new File("/mnt/extSdCard").exists();
    }
    /**
     *
     * @return is SDcard Available
     */
    public static boolean isSDcardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
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
        if (isSDcardAvailable()) {
            return Environment.getExternalStorageState();
        } else if (isRemoveableSDCardExist()) {
            return System.getenv("SECONDARY_STORAGE");
        }
        return null;
    }

    /**
     *
     * @param context
     * @return Internal Storage Path
     */
    public static String getInternalStoragePath(Context context) {
        return context.getFilesDir().toString();
    }

    /**
     *
     * @return free size External Storage
     */
    public static long getFreeSDCardSize() {
        if (isSDcardAvailable()) {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else if (isRemoveableSDCardExist()) {
            String secStore = System.getenv("SECONDARY_STORAGE");
            File path = new File(secStore);
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

    public static void createAppFolderInSDcard(Context context) {
        if (isSDcardAvailable()) {
            String extStorageDirectory = Environment
                    .getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "/Android/data/" + context.getPackageName());
            if (!folder.exists()) {
                folder.mkdir();
            }
        } else {
            if (!isRemoveableSDCardExist()) {
                return;
            }
            String secStore = System.getenv("SECONDARY_STORAGE");
            File fp = new File(secStore, "NewFolder");
            fp.mkdir();
            if (!fp.exists()) {
                return;
            }
            File folder = new File(secStore, "/Android/data/" + context.getPackageName());
            if (!folder.exists()) {
                folder.mkdir();
            }
        }
    }

    public static void createFolderInRootSDCard(String nameFolder) {
        if (nameFolder == null || nameFolder.length() == 0) {
            return;
        } else if (isSDcardAvailable()) {
            File f = new File(Environment.getExternalStorageDirectory(), nameFolder);
            if (!f.exists()) {
                f.mkdirs();
            }
        } else {
            if (!isRemoveableSDCardExist()) {
                return;
            }
            String secStore = System.getenv("SECONDARY_STORAGE");
            File fp = new File(secStore, "NewFolder");
            fp.mkdir();
            if (!fp.exists()) {
                return;
            }
            File f = new File(System.getenv("SECONDARY_STORAGE") + "/" + nameFolder);
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }

    public static void createFolderInAppFolder(String nameFolder, Context context) {
        if (nameFolder == null || nameFolder.length() == 0) {
            return;
        }
        File dir = context.getDir(nameFolder, Context.MODE_PRIVATE);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static void createFolder(String nameFolder) {
        if (nameFolder == null || nameFolder.length() == 0) {
            return;
        }
        File f = new File(Environment.getExternalStorageDirectory(), nameFolder);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public static void createFolderInInternalStorage(String nameFolder, Context context) {
        if (nameFolder == null || nameFolder.length() == 0) {
            return;
        }
        File file = new File(context.getFilesDir(), nameFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createFileInAppfolder(String fileName, Context context) {
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
        } else if (isSDcardAvailable()) {
            File f = new File(Environment.getExternalStorageDirectory(), fileName);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (!isRemoveableSDCardExist()) {
                return;
            }
            String secStore = System.getenv("SECONDARY_STORAGE");
            File fp = new File(secStore, "NewFolder");
            fp.mkdir();
            if (!fp.exists()) {
                return;
            }
            File f = new File(System.getenv("SECONDARY_STORAGE") + "/" + fileName);
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
     *
     * @param f
     * @return size file
     */
    public static long getFileSize(File f) {
        if (!f.exists() || !f.isFile()) {
            return 0;
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

    public static void compressZip(String inputFolderPath, String outZipPath) {
        if (!new File(inputFolderPath).exists() || !new File(outZipPath).exists()) {
            return;
        }
        try {
            int BUFFER = 2048;
            BufferedInputStream origin = null;

            FileOutputStream dest = new FileOutputStream(outZipPath);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];
            File srcFile = new File(inputFolderPath);
            if (srcFile.isDirectory()) {
                File[] _files = srcFile.listFiles();

                for (int i = 0; i < _files.length; i++) {
                    Log.v("Compress", "Adding: " + _files[i]);
                    FileInputStream fi = new FileInputStream(_files[i]);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(_files[i].getPath().substring(_files[i].getPath().lastIndexOf("/") + 1));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                    origin.close();
                }
            } else if (srcFile.isFile()) {
                Log.v("Compress", "Adding: " + srcFile);
                FileInputStream fi = new FileInputStream(srcFile);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(srcFile.getPath().substring(srcFile.getPath().lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();

            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void shareFile(File f, Context context) {
        if (!f.exists() || !f.isFile()) {
            return;
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, f);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
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
        if (isSDcardAvailable()) {
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
        if (isSDcardAvailable()) {
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

        } else if (isRemoveableSDCardExist()) {
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
     *
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


