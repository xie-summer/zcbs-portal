package com.zcbspay.platform.portal.common.constant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月13日 上午9:32:56
 * @since
 */
public class Constant {

    private static final Logger log = LoggerFactory.getLogger(Constant.class);

   
    private boolean canRun;
    private String refresh_interval;
    private static Constant constant;

    public static synchronized Constant getInstance() {
        if (constant == null) {
            constant = new Constant();
        }
        return constant;
    }

    private Constant() {
        refresh();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (canRun) {
                    try {
                        refresh();
                        int interval = NumberUtils.toInt(refresh_interval, 60) * 1000;// 刷新间隔，单位：秒
                       // log.info("refresh Constant datetime:" + DateUtil.getCurrentDateTime());
                        Thread.sleep(interval);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void refresh() {
        try {
            String path = "/home/web/trade/";
            File file = new File(path + "hz_batch.properties");
            if (!file.exists()) {
                path = getClass().getResource("/").getPath();
                file = null;
            }
            Properties prop = new Properties();
            InputStream stream = null;
            stream = new BufferedInputStream(new FileInputStream(new File(path + "hz_batch.properties")));
            prop.load(stream);
           
            canRun = true;
            refresh_interval = prop.getProperty("refresh_interval");
        }
        catch (FileNotFoundException e) {
            log.error("FileNotFoundException", e);
        }
        catch (IOException e) {
            log.error("IOException", e);
        }
    }

	public boolean isCanRun() {
        return canRun;
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    public String getRefresh_interval() {
        return refresh_interval;
    }

    public void setRefresh_interval(String refresh_interval) {
        this.refresh_interval = refresh_interval;
    }
}
