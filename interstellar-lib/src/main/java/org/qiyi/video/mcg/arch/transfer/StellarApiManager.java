package org.qiyi.video.mcg.arch.transfer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangallen on 2018/4/7.
 */

public class StellarApiManager {

    private static StellarApiManager sInstance;

    public static StellarApiManager getInstance(){
        if(null==sInstance){
            synchronized (StellarApiManager.class){
                if(null==sInstance){
                    sInstance=new StellarApiManager();
                }

            }
        }
        return sInstance;
    }

    private StellarApiManager(){}

    private Map<String,StellarApi>apiCache=new HashMap<>();

    public synchronized StellarApi getStellarApi(String serviceInterfaceName){
        if(null==serviceInterfaceName){
            return null;
        }
        StellarApi api=apiCache.get(serviceInterfaceName);
        if(api==null){
            api=new StellarApi(serviceInterfaceName);
            apiCache.put(serviceInterfaceName,api);
        }
        return api;
    }

}
