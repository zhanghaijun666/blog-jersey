package com.tools;

import com.verhas.licensor.License;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author zhanghaijun
 */
public class LicenseFileTool {

    public static void main(String[] args) {
        String fileName = "./store/demo.txt";

        File licenseFile = new File(fileName);
        File licenseOutputFile = new File(changeExt(fileName));

        if (!licenseFile.exists()) {
            return;
        }
        try {
            if (!licenseOutputFile.exists()) {
                // license 文件生成
                OutputStream os = new FileOutputStream(licenseOutputFile);
                os.write(new License()
                        // license 的原文
                        .setLicense(licenseFile)
                        // 私钥与之前生成密钥时产生的USER-ID
                        .loadKey(LicenseFileTool.class.getResourceAsStream("/config/secring.gpg"), "bjtxra (Bedrock Cloud License) <bjtxra@bjtxra.com>")
                        // 生成密钥时输入的密码
                        .encodeLicense("bjtxra").getBytes("utf-8"));
                os.close();
            } else {
                // licence 文件验证
                License license = new License();
                
                if (license
                        .loadKeyRing(LicenseFileTool.class.getResourceAsStream("/config/pubring.gpg"), null)
                        .setLicenseEncodedFromFile(licenseOutputFile.getPath()).isVerified()) {
                    System.out.println(license.getFeature("edition"));
                    System.out.println(license.getFeature("valid-until"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static String changeExt(String name) {
        if (name.contains(".")) {
            name = name.substring(0, name.lastIndexOf("."));
        }
        return name + ".lic";
    }
}
