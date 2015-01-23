package com.wyp.seewifipwd;

/**
 * Created by WYP on 2015/1/21.
 */
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public final class RootCmd {
    // 执行linux命令并且输出结果
    public static DataInputStream execRootCmd(String paramString) {
        // String result = "result : ";
        try {
            Process process = Runtime.getRuntime().exec("su");// 经过Root处理的android系统即有su命令
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            InputStream localInputStream = process.getInputStream();
            DataInputStream localDataInputStream = new DataInputStream(
                    localInputStream);
            String str = paramString + "\n";
            dataOutputStream.writeBytes(str);
            dataOutputStream.flush();
            // String str3 = null;
            // while ((str3 = localDataInputStream.readLine().trim()) != null) {
            // Log.d("result", str3);
            // }

            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            return localDataInputStream;
        } catch (Exception localException) {
            localException.printStackTrace();
            return null;
        }
    }

    // 执行linux命令但不关注结果输出
    protected static int execRootCmdSilent(String paramString) {
        try {
            Process localProcess = Runtime.getRuntime().exec("su");
            Object localObject = localProcess.getOutputStream();
            DataOutputStream localDataOutputStream = new DataOutputStream(
                    (OutputStream) localObject);
            String str = String.valueOf(paramString);
            localObject = str + "\n";
            localDataOutputStream.writeBytes((String) localObject);
            localDataOutputStream.flush();
            localDataOutputStream.writeBytes("exit\n");
            localDataOutputStream.flush();
            localProcess.waitFor();
            int result = localProcess.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }

    // 判断机器Android是否已经root，即是否获取root权限
    public static boolean haveRoot() {

        int i = execRootCmdSilent("echo test"); // 通过执行测试命令来检测
        if (i != -1) {
            return true;
        }
        return false;
    }

    /**
     * 判断机器是否root(或是否允许root)
     *
     * @return
     */
    public static synchronized boolean getRootAhth()
    {
        Process process = null;
        DataOutputStream os = null;
        try
        {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0)
            {
                return true;
            } else
            {
                return false;
            }
        } catch (Exception e)
        {
            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "
                    + e.getMessage());
            return false;
        } finally
        {
            try
            {
                if (os != null)
                {
                    os.close();
                }
                process.destroy();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private final static int kSystemRootStateUnknow = -1;
    private final static int kSystemRootStateDisable = 0;
    private final static int kSystemRootStateEnable = 1;
    private static int systemRootState = kSystemRootStateUnknow;

    /**
     * 在Android中，虽然我们可以通过Runtime.getRuntime().exec("su")的方式来判断一个手机是否Root,
     * 但是该方式会弹出对话框让用户选择是否赋予该应用程序Root权限，有点不友好。
     * 其实我们可以在环境变量$PATH所列出的所有目录中查找是否有su文件来判断一个手机是否Root。
     * 当然即使有su文件，也并不能完全表示手机已经Root，但是实际使用中作为一个初略的判断已经很好了。
     * 另外出于效率的考虑，我们可以在代码中直接把$PATH写死。
     * (装了app以后再root，会提示没有root，得root以后在装app才行)
     * @return
     */
    public static boolean isRootSystem()
    {
        if (systemRootState == kSystemRootStateEnable)
        {
            return true;
        }
        else if (systemRootState == kSystemRootStateDisable)
        {

            return false;
        }
        File f = null;
        final String kSuSearchPaths[] = {
                "/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"
        };
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++)
            {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists())
                {
                    systemRootState = kSystemRootStateEnable;
                    return true;
                }
            }
        } catch (Exception e)
        {
        }
        systemRootState = kSystemRootStateDisable;
        return false;
    }

}
