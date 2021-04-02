package com.lhk.auction.manage.util;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.net.URLDecoder;

public class PmsUploadUtil {

    private TrackerClient trackerClient=null;
    private TrackerServer trackerServer=null;
    private StorageServer storageServer=null;
    private StorageClient1 storageClient=null;

    public PmsUploadUtil() throws Exception {
        String conf=this.getClass().getResource("/tracker.conf").getPath();
        if(conf.contains("classpath")) {
            String path= URLDecoder.decode(getClass().getProtectionDomain().getCodeSource().getLocation().toString(), "UTF-8");
            path=path.substring(6);
            conf=conf.replace("classpath:", URLDecoder.decode(path, "UTF-8"));

        }
        ClientGlobal.init(conf);
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getTrackerServer();
        storageServer = null;
        storageClient = new StorageClient1(trackerServer,storageServer);
    }

    public String uploadFile(String fileName, String extName, NameValuePair[] metas) {
        String result=null;
        try {
            result = storageClient.upload_file1(fileName,extName,metas);
        } catch (IOException | MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    public String uploadFile(String fileName) {
        return uploadFile(fileName,null,null);
    }

    public String uploadFile(String fileName,String extName) {
        return uploadFile(fileName, extName,null);
    }

    public String uploadFile(byte[] fileContent,String extName,NameValuePair[] metas) {
        String result=null;
        try {
            result=storageClient.upload_file1(fileContent, extName, metas);
        } catch (IOException | MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public String uploadFile(byte[] fileContent) throws Exception {
        return uploadFile(fileContent,null,null);
    }

    public String uploadFile(byte[] fileContent,String extName) {
        return uploadFile(fileContent,extName,null);
    }

}
