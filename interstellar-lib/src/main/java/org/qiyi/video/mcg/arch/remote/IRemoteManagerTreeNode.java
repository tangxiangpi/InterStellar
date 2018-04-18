package org.qiyi.video.mcg.arch.remote;

import java.util.Set;

/**
 * Created by wangallen on 2018/3/27.
 */

public interface
IRemoteManagerTreeNode {
    Set<IRemoteManager> getDescendants();
}
